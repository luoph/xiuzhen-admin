package org.jeecg.common.system.util;

import cn.hutool.core.io.FileUtil;
import cn.youai.basics.utils.StringUtils;
import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.oConvertUtils;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.entity.enmus.ExcelType;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author luopeihuan
 * @version 1.0
 * @date 2019-12-16.
 */
@Slf4j
public class ExcelUtils {

    /**
     * 获取对象ID
     *
     * @return
     */
    public static <T> String getId(T item) {
        try {
            return PropertyUtils.getProperty(item, "id").toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static <T> ModelAndView exportXls(IPage<T> pageList, String selections, Class<T> clazz, String title) {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        return ExcelUtils.exportXls(sysUser.getRealname(), pageList.getRecords(), selections, clazz, title);
    }

    /**
     * 导出excel
     */
    public static <T> ModelAndView exportXls(String userName, List<T> pageList, String selections, Class<T> clazz, String title) {
        List<T> exportList = null;
        // 过滤选中数据
        if (oConvertUtils.isNotEmpty(selections)) {
            List<String> selectionList = StringUtils.split2List(selections);
            exportList = pageList.stream().filter(item -> selectionList.contains(getId(item))).collect(Collectors.toList());
        } else {
            exportList = pageList;
        }

        // Step.3 AutoPoi 导出Excel
        ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
        // 此处设置的filename无效 ,前端会重更新设置一下
        mv.addObject(NormalExcelConstants.FILE_NAME, title);
        mv.addObject(NormalExcelConstants.CLASS, clazz);
        ExportParams exportParams = new ExportParams(title + "报表", "导出人:" + userName, title);
        exportParams.setType(ExcelType.XSSF);
        mv.addObject(NormalExcelConstants.PARAMS, exportParams);
        mv.addObject(NormalExcelConstants.DATA_LIST, exportList);
        return mv;
    }


    /**
     * 通过excel导入数据
     *
     * @param service
     * @param request
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> Result<?> importExcel(IService<T> service, HttpServletRequest request, Class<T> clazz) {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
        for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
            // 获取上传文件对象
            MultipartFile file = entity.getValue();
            ImportParams params = new ImportParams();
            params.setTitleRows(2);
            params.setHeadRows(1);
            params.setNeedSave(true);
            try {
                List<T> list = ExcelImportUtil.importExcel(file.getInputStream(), clazz, params);
                //update-begin-author:taoyan date:20190528 for:批量插入数据
                long start = System.currentTimeMillis();
                service.saveBatch(list);
                //400条 saveBatch消耗时间1592毫秒  循环插入消耗时间1947毫秒
                //1200条  saveBatch消耗时间3687毫秒 循环插入消耗时间5212毫秒
                log.info("消耗时间" + (System.currentTimeMillis() - start) + "毫秒");
                //update-end-author:taoyan date:20190528 for:批量插入数据
                return Result.ok("文件导入成功！数据行数：" + list.size());
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                return Result.error("文件导入失败:" + e.getMessage());
            } finally {
                try {
                    file.getInputStream().close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return Result.error("文件导入失败！");
    }

    public static <T> List<T> readExcel(String filename, Class<T> clazz) {
        return EasyExcel.read(filename).head(clazz).sheet(0).doReadSync();
    }

    public static <T> List<T> readExcel(String filename, String sheetName, Class<T> clazz) {
        return EasyExcel.read(filename).head(clazz).sheet(sheetName).doReadSync();
    }

    public static <T> List<T> readExcel(InputStream inputStream, String sheetName, Class<T> clazz) {
        return EasyExcel.read(inputStream).head(clazz).sheet(sheetName).doReadSync();
    }

    public static <T> List<T> readExcel(InputStream inputStream, Class<T> clazz) {
        return EasyExcel.read(inputStream).head(clazz).sheet().doReadSync();
    }

    private static <T> List<T> readExcel(String filename, Class<T> clazz, int headRowCount) {
        return EasyExcel.read(filename).head(clazz).sheet().headRowNumber(headRowCount).doReadSync();
    }

    public static <T> List<T> readExcel(InputStream inputStream, Class<T> clazz, int headRowCount) {
        return EasyExcel.read(inputStream).head(clazz).sheet().headRowNumber(headRowCount).doReadSync();
    }

    public static <T> List<T> readExcel(InputStream inputStream, String sheetName, Class<T> clazz, int headRowCount) {
        return EasyExcel.read(inputStream).head(clazz).sheet(sheetName).headRowNumber(headRowCount).doReadSync();
    }


    /**
     * 解析从excel复制的文本
     *
     * @param text         文本
     * @param tempFileName 临时文件地址
     * @param clazz        解析出的目标类型
     * @param <T>          模版类型
     * @return 对象列表
     */
    public static <T> List<T> importFromExcelText(String text, String tempFileName, Class<T> clazz) {
        File excelFile = new File(tempFileName);
        if (excelFile.exists()) {
            FileUtil.del(excelFile);
        }

        FileUtil.mkParentDirs(excelFile);

        char[] chars = text.toCharArray();
        boolean hasQuotation = false;
        int prevIndex = 0;

        if (chars[chars.length - 1] != '\n') {
            chars = (text + '\n').toCharArray();
        }

        List<String> cellLine = new ArrayList<>();
        List<List<String>> textLine = new ArrayList<>();

        for (int i = 0; i < chars.length; i++) {
            if (chars[i] == '"') {
                // 是否与上一个引号匹配
                hasQuotation = !hasQuotation;
            } else if (chars[i] == '\n') {
                if (!hasQuotation) {
                    textLine.add(cellLine);
                    cellLine.add(text.substring(prevIndex, i));
                    prevIndex = i + 1;

                    // 新的行
                    cellLine = new ArrayList<>();
                }
            } else if (chars[i] == '\t') {
                if (!hasQuotation) {
                    cellLine.add(text.substring(prevIndex, i));
                    prevIndex = i + 1;
                }
            }
        }

//        log.info("textLine:{}", textLine);

        // 保存成临时文件
        try {
            Workbook workbook = new HSSFWorkbook();
            Sheet sheet = workbook.createSheet("Sheet1");
            int rowNum = 0;
            for (List<String> line : textLine) {
                Row row = sheet.createRow(rowNum++);
                int cellNum = 0;
                for (String value : line) {
                    Cell cell = row.createCell(cellNum++);
                    // 去掉双引号
                    if (value.startsWith("\"") && value.endsWith("\"")) {
                        value = value.substring(1, value.length() - 1);
                    }
                    cell.setCellValue(value);
                }
            }

            FileOutputStream fileOut = new FileOutputStream(tempFileName);
            workbook.write(fileOut);
            fileOut.close();

        } catch (IOException e) {
            log.error("importFromExcelText error, text:" + text + ", clazz:" + clazz.getSimpleName(), e);
        }

        return readExcel(tempFileName, clazz);
    }
}

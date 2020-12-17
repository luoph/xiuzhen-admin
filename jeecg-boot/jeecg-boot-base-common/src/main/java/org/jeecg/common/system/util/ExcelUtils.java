package org.jeecg.common.system.util;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.extension.service.IService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.PropertyUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.util.oConvertUtils;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
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

    /**
     * 导出excel
     *
     * @param userName
     * @param pageList
     * @param selections
     * @param clazz
     * @param title
     * @param <T>
     * @return
     */
    public static <T> ModelAndView exportXls(String userName, List<T> pageList, String selections, Class<T> clazz, String title) {
        List<T> exportList = null;
        // 过滤选中数据
        if (oConvertUtils.isNotEmpty(selections)) {
            List<String> selectionList = Arrays.asList(selections.split(","));
            exportList = pageList.stream().filter(item -> selectionList.contains(getId(item))).collect(Collectors.toList());
        } else {
            exportList = pageList;
        }

        // Step.3 AutoPoi 导出Excel
        ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
        // 此处设置的filename无效 ,前端会重更新设置一下
        mv.addObject(NormalExcelConstants.FILE_NAME, title);
        mv.addObject(NormalExcelConstants.CLASS, clazz);
        mv.addObject(NormalExcelConstants.PARAMS, new ExportParams(title + "报表", "导出人:" + userName, title));
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
        return EasyExcel.read(filename).head(clazz).sheet().doReadSync();
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

    private static <T> List<T> readExcel(InputStream inputStream, Class<T> clazz, int headRowCount) {
        return EasyExcel.read(inputStream).head(clazz).sheet().headRowNumber(headRowCount).doReadSync();
    }

    private static <T> List<T> readExcel(InputStream inputStream, String sheetName, Class<T> clazz, int headRowCount) {
        return EasyExcel.read(inputStream).head(clazz).sheet(sheetName).headRowNumber(headRowCount).doReadSync();
    }

}

package org.jeecg.modules.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.ImportExcelUtil;
import org.jeecg.modules.system.entity.SysPosition;
import org.jeecg.modules.system.service.ISysPositionService;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @Description: 职务表
 * @Author: jeecg-boot
 * @Date: 2019-09-19
 * @Version: V1.0
 */
@Slf4j
@Api(tags = "职务表")
@RestController
@RequestMapping("/sys/position")
public class SysPositionController extends JeecgController<SysPosition, ISysPositionService> {

    @Autowired
    private ISysPositionService sysPositionService;

    /**
     * 分页列表查询
     *
     * @param sysPosition
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "职务表-分页列表查询")
    @ApiOperation(value = "职务表-分页列表查询", notes = "职务表-分页列表查询")
    @GetMapping(value = "/list")
    public Result<IPage<SysPosition>> queryPageList(SysPosition sysPosition,
                                                    @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                    @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                    HttpServletRequest req) {
        Result<IPage<SysPosition>> result = new Result<IPage<SysPosition>>();
        QueryWrapper<SysPosition> queryWrapper = QueryGenerator.initQueryWrapper(sysPosition, req.getParameterMap());
        Page<SysPosition> page = new Page<SysPosition>(pageNo, pageSize);
        IPage<SysPosition> pageList = sysPositionService.page(page, queryWrapper);
        result.setSuccess(true);
        result.setResult(pageList);
        return result;
    }

    /**
     * 添加
     *
     * @param sysPosition
     * @return
     */
    @AutoLog(value = "职务表-添加")
    @ApiOperation(value = "职务表-添加", notes = "职务表-添加")
    @PostMapping(value = "/add")
    public Result<SysPosition> add(@RequestBody SysPosition sysPosition) {
        Result<SysPosition> result = new Result<SysPosition>();
        try {
            sysPositionService.save(sysPosition);
            result.success("添加成功！");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            result.error500("操作失败");
        }
        return result;
    }

    /**
     * 编辑
     *
     * @param sysPosition
     * @return
     */
    @AutoLog(value = "职务表-编辑")
    @ApiOperation(value = "职务表-编辑", notes = "职务表-编辑")
    @RequestMapping(value = "/edit", method = {RequestMethod.PUT, RequestMethod.POST})
    public Result<SysPosition> edit(@RequestBody SysPosition sysPosition) {
        Result<SysPosition> result = new Result<SysPosition>();
        SysPosition sysPositionEntity = sysPositionService.getById(sysPosition.getId());
        if (sysPositionEntity == null) {
            result.error500("未找到对应实体");
        } else {
            boolean ok = sysPositionService.updateById(sysPosition);
            //TODO 返回false说明什么？
            if (ok) {
                result.success("修改成功!");
            }
        }

        return result;
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "职务表-通过id删除")
    @ApiOperation(value = "职务表-通过id删除", notes = "职务表-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        try {
            sysPositionService.removeById(id);
        } catch (Exception e) {
            log.error("删除失败", e.getMessage());
            return Result.error("删除失败!");
        }
        return Result.ok("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "职务表-批量删除")
    @ApiOperation(value = "职务表-批量删除", notes = "职务表-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<SysPosition> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        Result<SysPosition> result = new Result<SysPosition>();
        if (ids == null || "".equals(ids.trim())) {
            result.error500("参数不识别！");
        } else {
            this.sysPositionService.removeByIds(Arrays.asList(ids.split(",")));
            result.success("删除成功!");
        }
        return result;
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "职务表-通过id查询")
    @ApiOperation(value = "职务表-通过id查询", notes = "职务表-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<SysPosition> queryById(@RequestParam(name = "id", required = true) String id) {
        Result<SysPosition> result = new Result<SysPosition>();
        SysPosition sysPosition = sysPositionService.getById(id);
        if (sysPosition == null) {
            result.error500("未找到对应实体");
        } else {
            result.setResult(sysPosition);
            result.setSuccess(true);
        }
        return result;
    }

    /**
     * 导出excel
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(SysPosition sysPosition, HttpServletRequest request) {
        return super.exportXls(request, sysPosition, SysPosition.class, "职务列表");
    }

    /**
     * 通过excel导入数据
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) throws IOException {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
        // 错误信息
        List<String> errorMessage = new ArrayList<>();
        int successLines = 0, errorLines = 0;
        for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
            // 获取上传文件对象
            MultipartFile file = entity.getValue();
            ImportParams params = new ImportParams();
            params.setTitleRows(2);
            params.setHeadRows(1);
            params.setNeedSave(true);
            try {
                List<Object> listSysPositions = ExcelImportUtil.importExcel(file.getInputStream(), SysPosition.class, params);
                List<String> list = ImportExcelUtil.importDateSave(listSysPositions, ISysPositionService.class, errorMessage, CommonConstant.SQL_INDEX_UNIQ_CODE);
                errorLines += list.size();
                successLines += (listSysPositions.size() - errorLines);
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
        return ImportExcelUtil.imporReturnRes(errorLines, successLines, errorMessage);
    }

    /**
     * 通过code查询
     *
     * @param code
     * @return
     */
    @AutoLog(value = "职务表-通过code查询")
    @ApiOperation(value = "职务表-通过code查询", notes = "职务表-通过code查询")
    @GetMapping(value = "/queryByCode")
    public Result<SysPosition> queryByCode(@RequestParam(name = "code", required = true) String code) {
        Result<SysPosition> result = new Result<SysPosition>();
        QueryWrapper<SysPosition> queryWrapper = new QueryWrapper<SysPosition>();
        queryWrapper.eq("code", code);
        SysPosition sysPosition = sysPositionService.getOne(queryWrapper);
        if (sysPosition == null) {
            result.error500("未找到对应实体");
        } else {
            result.setResult(sysPosition);
            result.setSuccess(true);
        }
        return result;
    }
}

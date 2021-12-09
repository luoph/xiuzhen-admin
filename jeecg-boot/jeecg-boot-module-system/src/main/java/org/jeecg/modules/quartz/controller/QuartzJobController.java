package org.jeecg.modules.quartz.controller;

import cn.hutool.core.date.DateUtil;
import cn.youai.server.utils.DateUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.exception.JeecgBootException;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.game.constant.CoreStatisticType;
import org.jeecg.modules.game.service.IGameDataCountService;
import org.jeecg.modules.quartz.entity.QuartzJob;
import org.jeecg.modules.quartz.service.IQuartzJobService;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 定时任务在线管理
 * @date 2019-01-02
 */
@RestController
@RequestMapping("/sys/quartzJob")
@Slf4j
@Api(tags = "定时任务接口")
public class QuartzJobController {
    @Autowired
    private IQuartzJobService quartzJobService;
    @Autowired
    private Scheduler scheduler;
    @Autowired
    private IGameDataCountService gameDataCountService;

    /**
     * 分页列表查询
     *
     * @param quartzJob
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public Result<?> queryPageList(QuartzJob quartzJob, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo, @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, HttpServletRequest req) {
        QueryWrapper<QuartzJob> queryWrapper = QueryGenerator.initQueryWrapper(quartzJob, req.getParameterMap());
        Page<QuartzJob> page = new Page<>(pageNo, pageSize);
        IPage<QuartzJob> pageList = quartzJobService.page(page, queryWrapper);
        return Result.ok(pageList);

    }

    /**
     * 添加定时任务
     *
     * @param quartzJob
     * @return
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public Result<?> add(@RequestBody QuartzJob quartzJob) {
        List<QuartzJob> list = quartzJobService.findByJobClassName(quartzJob.getJobClassName());
        if (list != null && list.size() > 0) {
            return Result.error("该定时任务类名已存在");
        }
        quartzJobService.saveAndScheduleJob(quartzJob);
        return Result.ok("创建定时任务成功");
    }

    /**
     * 更新定时任务
     *
     * @param quartzJob
     * @return
     */
    @RequestMapping(value = "/edit", method = RequestMethod.PUT)
    public Result<?> eidt(@RequestBody QuartzJob quartzJob) {
        try {
            quartzJobService.editAndScheduleJob(quartzJob);
        } catch (SchedulerException e) {
            log.error(e.getMessage(), e);
            return Result.error("更新定时任务失败!");
        }
        return Result.ok("更新定时任务成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public Result<?> delete(@RequestParam(name = "id") String id) {
        QuartzJob quartzJob = quartzJobService.getById(id);
        if (quartzJob == null) {
            return Result.error("未找到对应实体");
        }
        quartzJobService.deleteAndStopJob(quartzJob);
        return Result.ok("删除成功!");

    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @RequestMapping(value = "/deleteBatch", method = RequestMethod.DELETE)
    public Result<?> deleteBatch(@RequestParam(name = "ids") String ids) {
        if (ids == null || "".equals(ids.trim())) {
            return Result.error("参数不识别！");
        }
        for (String id : Arrays.asList(ids.split(","))) {
            QuartzJob job = quartzJobService.getById(id);
            quartzJobService.deleteAndStopJob(job);
        }
        return Result.ok("删除定时任务成功!");
    }

    /**
     * 暂停定时任务
     *
     * @param jobClassName
     * @return
     */
    @GetMapping(value = "/pause")
    @ApiOperation(value = "暂停定时任务")
    public Result<Object> pauseJob(@RequestParam(name = "jobClassName") String jobClassName) {
        QuartzJob job = null;
        try {
            job = quartzJobService.getOne(new LambdaQueryWrapper<QuartzJob>().eq(QuartzJob::getJobClassName, jobClassName));
            if (job == null) {
                return Result.error("定时任务不存在！");
            }
            scheduler.pauseJob(JobKey.jobKey(jobClassName.trim()));
        } catch (SchedulerException e) {
            throw new JeecgBootException("暂停定时任务失败");
        }
        job.setStatus(CommonConstant.STATUS_DISABLE);
        quartzJobService.updateById(job);
        return Result.ok("暂停定时任务成功");
    }

    /**
     * 启动定时任务
     *
     * @param jobClassName
     * @return
     */
    @GetMapping(value = "/resume")
    @ApiOperation(value = "恢复定时任务")
    public Result<Object> resumeJob(@RequestParam(name = "jobClassName") String jobClassName) {
        QuartzJob job = quartzJobService.getOne(new LambdaQueryWrapper<QuartzJob>().eq(QuartzJob::getJobClassName, jobClassName));
        if (job == null) {
            return Result.error("定时任务不存在！");
        }
        quartzJobService.resumeJob(job);
        //scheduler.resumeJob(JobKey.jobKey(job.getJobClassName().trim()));
        return Result.ok("恢复定时任务成功");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/queryById", method = RequestMethod.GET)
    public Result<?> queryById(@RequestParam(name = "id") String id) {
        QuartzJob quartzJob = quartzJobService.getById(id);
        return Result.ok(quartzJob);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param quartzJob
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, QuartzJob quartzJob) {
        // Step.1 组装查询条件
        QueryWrapper<QuartzJob> queryWrapper = QueryGenerator.initQueryWrapper(quartzJob, request.getParameterMap());
        // Step.2 AutoPoi 导出Excel
        ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
        List<QuartzJob> pageList = quartzJobService.list(queryWrapper);
        // 导出文件名称
        mv.addObject(NormalExcelConstants.FILE_NAME, "定时任务列表");
        mv.addObject(NormalExcelConstants.CLASS, QuartzJob.class);
        mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("定时任务列表数据", "导出人:Jeecg", "导出信息"));
        mv.addObject(NormalExcelConstants.DATA_LIST, pageList);
        return mv;
    }

    /**
     * 通过excel导入数据
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
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
                List<QuartzJob> listQuartzJobs = ExcelImportUtil.importExcel(file.getInputStream(), QuartzJob.class, params);
                for (QuartzJob quartzJobExcel : listQuartzJobs) {
                    quartzJobService.save(quartzJobExcel);
                }
                return Result.ok("文件导入成功！数据行数：" + listQuartzJobs.size());
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                return Result.error("文件导入失败！");
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

    /**
     * 手动执行定时任务 [开始日期,结束日期]
     *
     * @param jobType   执行任务类型
     * @param startDate 开始日期
     * @param endDate   结束日期
     */
    @GetMapping(value = "/doQuartzJobByType")
    public Result<?> runCoreStatisticManually(@RequestParam(value = "jobType", defaultValue = "0") int jobType, @RequestParam(value = "startDate", defaultValue = "") String startDate, @RequestParam(value = "endDate", defaultValue = "") String endDate) {
        if (jobType <= 0) {
            return Result.error("未设置任务类型");
        }

        if (StringUtils.isEmpty(startDate) || StringUtils.isEmpty(endDate)) {
            return Result.error("未设置开始和结束时间");
        }

        Date startDay = DateUtils.dateOnly(DateUtil.parseDate(startDate));
        Date endDay = DateUtils.dateOnly(DateUtil.parseDate(endDate));

        int daysBetween = DateUtils.daysBetween(startDay, endDay);
        if (daysBetween < 0) {
            return Result.error("结束时间必须大于等于开始时间");
        }

        CoreStatisticType statisticType = CoreStatisticType.valueOf(jobType);
        if (statisticType == null) {
            return Result.error("找不到对应的任务类型");
        }

        for (int i = 0; i <= daysBetween; i++) {
            Date registerDate = DateUtils.addDays(startDay, i);
            gameDataCountService.doJobDataCount(registerDate, statisticType);
        }

        return Result.ok("successes!");
    }
}

package org.jeecg.modules.quartz.controller;

import cn.hutool.core.date.DateUtil;
import cn.youai.basics.utils.StringUtils;
import cn.youai.server.utils.DateUtils;
import cn.youai.xiuzhen.stat.constant.StatisticType;
import cn.youai.xiuzhen.stat.service.IGameDataCountService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.constant.SymbolConstant;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.ImportExcelUtil;
import org.jeecg.modules.quartz.entity.QuartzJob;
import org.jeecg.modules.quartz.service.IQuartzJobService;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * @Description: 定时任务在线管理
 * @Author: jeecg-boot
 * @Date: 2019-01-02
 * @Version:V1.0
 */
@Slf4j
@RestController
@RequestMapping("/sys/quartzJob")
@Api(tags = "定时任务接口")
public class QuartzJobController extends JeecgController<QuartzJob, IQuartzJobService> {

    @Autowired
    private IQuartzJobService quartzJobService;
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
    public Result<?> queryPageList(QuartzJob quartzJob, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, HttpServletRequest req) {
        QueryWrapper<QuartzJob> queryWrapper = QueryGenerator.initQueryWrapper(quartzJob, req.getParameterMap());
        Page<QuartzJob> page = new Page<QuartzJob>(pageNo, pageSize);
        IPage<QuartzJob> pageList = quartzJobService.page(page, queryWrapper);
        return Result.ok(pageList);

    }

    /**
     * 添加定时任务
     *
     * @param quartzJob
     * @return
     */
    //@RequiresRoles("admin")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public Result<?> add(@RequestBody QuartzJob quartzJob) {
        quartzJobService.saveAndScheduleJob(quartzJob);
        return Result.ok("创建定时任务成功");
    }

    /**
     * 更新定时任务
     *
     * @param quartzJob
     * @return
     */
    //@RequiresRoles("admin")
    @RequestMapping(value = "/edit", method = {RequestMethod.PUT, RequestMethod.POST})
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
    //@RequiresRoles("admin")
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
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
    //@RequiresRoles("admin")
    @RequestMapping(value = "/deleteBatch", method = RequestMethod.DELETE)
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        if (ids == null || "".equals(ids.trim())) {
            return Result.error("参数不识别！");
        }
        for (String id : Arrays.asList(ids.split(SymbolConstant.COMMA))) {
            QuartzJob job = quartzJobService.getById(id);
            quartzJobService.deleteAndStopJob(job);
        }
        return Result.ok("删除定时任务成功!");
    }

    /**
     * 暂停定时任务
     *
     * @param id
     * @return
     */
    //@RequiresRoles("admin")
    @GetMapping(value = "/pause")
    @ApiOperation(value = "停止定时任务")
    public Result<Object> pauseJob(@RequestParam(name = "id") String id) {
        QuartzJob job = quartzJobService.getById(id);
        if (job == null) {
            return Result.error("定时任务不存在！");
        }
        quartzJobService.pause(job);
        return Result.ok("停止定时任务成功");
    }

    /**
     * 启动定时任务
     *
     * @param id
     * @return
     */
    //@RequiresRoles("admin")
    @GetMapping(value = "/resume")
    @ApiOperation(value = "启动定时任务")
    public Result<Object> resumeJob(@RequestParam(name = "id") String id) {
        QuartzJob job = quartzJobService.getById(id);
        if (job == null) {
            return Result.error("定时任务不存在！");
        }
        quartzJobService.resumeJob(job);
        //scheduler.resumeJob(JobKey.jobKey(job.getJobClassName().trim()));
        return Result.ok("启动定时任务成功");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/queryById", method = RequestMethod.GET)
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        QuartzJob quartzJob = quartzJobService.getById(id);
        return Result.ok(quartzJob);
    }

    /**
     * 导出excel
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, QuartzJob quartzJob) {
        return super.exportXls(request, quartzJob, QuartzJob.class, "定时任务");
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
                List<QuartzJob> listQuartzJobs = ExcelImportUtil.importExcel(file.getInputStream(), QuartzJob.class, params);
                //add-begin-author:taoyan date:20210909 for:导入定时任务，并不会被启动和调度，需要手动点击启动，才会加入调度任务中 #2986
                for (QuartzJob job : listQuartzJobs) {
                    job.setStatus(CommonConstant.STATUS_DISABLE);
                }
                List<String> list = ImportExcelUtil.importDateSave(listQuartzJobs, IQuartzJobService.class, errorMessage, CommonConstant.SQL_INDEX_UNIQ_JOB_CLASS_NAME);
                //add-end-author:taoyan date:20210909 for:导入定时任务，并不会被启动和调度，需要手动点击启动，才会加入调度任务中 #2986
                errorLines += list.size();
                successLines += (listQuartzJobs.size() - errorLines);
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
        return ImportExcelUtil.imporReturnRes(errorLines, successLines, errorMessage);
    }

    /**
     * 立即执行
     *
     * @param id
     * @return
     */
    //@RequiresRoles("admin")
    @GetMapping("/execute")
    public Result<?> execute(@RequestParam(name = "id", required = true) String id) {
        QuartzJob quartzJob = quartzJobService.getById(id);
        if (quartzJob == null) {
            return Result.error("未找到对应实体");
        }
        try {
            quartzJobService.execute(quartzJob);
        } catch (Exception e) {
            //e.printStackTrace();
            log.info("定时任务 立即执行失败>>" + e.getMessage());
            return Result.error("执行失败!");
        }
        return Result.ok("执行成功!");
    }

    /**
     * 手动执行定时任务 [开始日期,结束日期]
     *
     * @param jobType   执行任务类型
     * @param startDate 开始日期
     * @param endDate   结束日期
     */
    @GetMapping(value = "/runCoreStatisticManually")
    public Result<?> runCoreStatisticManually(@RequestParam(value = "jobType", defaultValue = "0") int jobType,
                                              @RequestParam(value = "startDate", defaultValue = "") String startDate,
                                              @RequestParam(value = "endDate", defaultValue = "") String endDate) {
        if (jobType <= 0) {
            return Result.error("未设置任务类型");
        }

        if (StringUtils.isEmpty(startDate) || StringUtils.isEmpty(endDate)) {
            return Result.error("未设置开始和结束时间");
        }

        Date now = DateUtils.now();
        Date startDay = DateUtils.dateOnly(DateUtil.parseDate(startDate));
        Date endDay = DateUtils.dateOnly(DateUtil.parseDate(endDate));
        // 设置最晚不超过当前时间
        if (endDay.after(now)) {
            endDay = DateUtils.dateOnly(now);
        }

        int daysBetween = DateUtils.daysBetween(startDay, endDay);
        if (daysBetween < 0) {
            return Result.error("结束时间必须大于等于开始时间");
        }

        StatisticType statisticType = StatisticType.valueOf(jobType);
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

package cn.youai.xiuzhen.stat.job;

import cn.youai.server.utils.DateUtils;
import cn.youai.xiuzhen.stat.constant.StatisticType;
import cn.youai.xiuzhen.stat.service.IGameDataCountService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * QuartzJobList 配置的定时任务
 * ltv统计Update定时任务
 *
 * @author lijunchi
 * @version 1.0
 * @date 2021-4-16 10:44
 */
@Slf4j
public class GameDateCountUpdateByLtv implements Job {

    @Autowired
    private IGameDataCountService gameDataCountService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        gameDataCountService.doJobDataCountUpdateByType(StatisticType.LTV, DateUtils.todayDate());
    }
}

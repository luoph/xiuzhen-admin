package cn.youai.xiuzhen.stat.job;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * QuartzJobList 配置的定时任务
 * 留存统计update定时任务
 *
 * @author lijunchi
 * @version 1.0
 * @date 2021-4-16 10:53
 */
public class GameDateCountUpdateByRemain implements Job {

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
//        gameDataCountService.doJobDataCountUpdateByType(CoreStatisticType.REMAIN, DateUtils.todayDate());
    }
}

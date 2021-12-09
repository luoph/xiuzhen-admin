package org.jeecg.modules.quartz.job;

import cn.youai.server.utils.DateUtils;
import org.jeecg.modules.game.constant.CoreStatisticType;
import org.jeecg.modules.game.service.IGameDataCountService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * QuartzJobList 配置的定时任务
 * 留存统计update定时任务
 *
 * @author lijunchi
 * @version 1.0
 * @date 2021-4-16 10:53
 */
public class GameDateCountUpdateByRemain implements Job {

    @Autowired
    private IGameDataCountService gameDataCountService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        gameDataCountService.doJobDataCountUpdateByType(CoreStatisticType.REMAIN, DateUtils.todayDate());
    }
}

package org.jeecg.modules.quartz.job;

import cn.youai.server.utils.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.game.constant.CoreStatisticType;
import org.jeecg.modules.game.service.IGameDataCountService;
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
        gameDataCountService.doJobDataCountUpdateByType(CoreStatisticType.LTV, DateUtils.todayDate());
    }
}

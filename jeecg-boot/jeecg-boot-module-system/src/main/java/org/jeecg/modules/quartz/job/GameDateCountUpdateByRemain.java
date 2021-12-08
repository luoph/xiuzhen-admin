package org.jeecg.modules.quartz.job;

import cn.youai.server.utils.DateUtils;
import org.jeecg.modules.game.service.IGameDataCountService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author lijunchi
 * @version 1.0
 * @description: 留存统计update定时任务
 * @date 2021-4-16 10:53
 */
public class GameDateCountUpdateByRemain implements Job {

    @Autowired
    private IGameDataCountService gameDataCountService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        gameDataCountService.doJobDataCountUpdateByType(IGameDataCountService.GAME_DATA_COUNT_TYPE_REMAIN, DateUtils.todayDate());
    }
}

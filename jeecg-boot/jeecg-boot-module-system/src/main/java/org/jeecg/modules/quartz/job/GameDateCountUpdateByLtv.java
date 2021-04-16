package org.jeecg.modules.quartz.job;

import cn.youai.xiuzhen.utils.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.game.service.IGameDataCountService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author lijunchi
 * @version 1.0
 * @description: ltv统计Update定时任务
 * @date 2021-4-16 10:44
 */
@Slf4j
public class GameDateCountUpdateByLtv implements Job {

    @Autowired
    private IGameDataCountService gameDataCountService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        gameDataCountService.doJobDataCountUpdateByType(IGameDataCountService.GAME_DATA_COUNT_TYPE_LTV, DateUtils.todayDate());
    }
}

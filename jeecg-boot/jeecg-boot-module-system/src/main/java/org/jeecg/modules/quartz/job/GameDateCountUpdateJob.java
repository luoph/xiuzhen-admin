package org.jeecg.modules.quartz.job;

import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.game.service.IGameDataCountService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @ClassName GameDateCountUpdateJob
 * @Description 游戏数据统计更新
 * @Author buliangliang
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2020/9/8 6:17 下午
 */
@Slf4j
public class GameDateCountUpdateJob implements Job {

    @Autowired
    private IGameDataCountService gameDataCountService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        gameDataCountService.doJobDataCountUpdate();
    }
}
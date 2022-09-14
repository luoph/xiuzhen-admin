package org.jeecg.modules.quartz.job;

import cn.youai.xiuzhen.game.service.IGameDataCountService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * QuartzJobList 配置的定时任务
 *
 * @ClassName GameDataCountJob
 * @Description 游戏数据统计
 * @Author buliangliang
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2020-08-27 22:59
 */
@Slf4j
public class GameDataCountJob implements Job {

    @Autowired
    private IGameDataCountService gameDataCountService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        gameDataCountService.doJobDataCount();
    }
}

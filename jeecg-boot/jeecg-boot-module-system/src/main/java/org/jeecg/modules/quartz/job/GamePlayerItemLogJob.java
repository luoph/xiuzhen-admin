package org.jeecg.modules.quartz.job;

import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;

/**
 * @ClassName PlayerItemLogJob
 * @Description 玩家道具日志同步定时器
 * @Author buliangliang
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2020-07-23 10:36
 */
@Slf4j
public class GamePlayerItemLogJob implements Job {

    @Override
    public void execute(JobExecutionContext context) {

    }
}

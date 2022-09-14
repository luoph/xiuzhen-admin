package cn.youai.xiuzhen.stat.job;

import cn.youai.xiuzhen.stat.service.IGameOnlineNumService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * QuartzJobList 配置的定时任务
 *
 * @author xubodong
 * @version 1.0.0
 * @date 2020/10/15 20:20
 */
@Slf4j
public class GameOnlineNumJob implements Job {

    @Autowired
    private IGameOnlineNumService gameOnlineNumService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        gameOnlineNumService.doGameOnlineNumSave();
    }
}

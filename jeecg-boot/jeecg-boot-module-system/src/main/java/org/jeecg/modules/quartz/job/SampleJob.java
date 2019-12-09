package org.jeecg.modules.quartz.job;

import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.DateUtils;
import org.quartz.Job;
import org.quartz.JobExecutionContext;

/**
 * 示例不带参定时任务
 *
 * @author Scott
 */
@Slf4j
public class SampleJob implements Job {

    @Override
    public void execute(JobExecutionContext jobExecutionContext) {
        log.info(" Jeecg-Boot 普通定时任务 SampleJob !  时间:" + DateUtils.getTimestamp());
    }
}

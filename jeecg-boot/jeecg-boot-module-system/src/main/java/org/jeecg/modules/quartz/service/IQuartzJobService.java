package org.jeecg.modules.quartz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.quartz.entity.QuartzJob;
import org.quartz.SchedulerException;

import java.util.List;

/**
 * @author jeecg-boot
 * @version V1.1
 * @description 定时任务在线管理
 * @date 2019-04-28
 */
public interface IQuartzJobService extends IService<QuartzJob> {

    List<QuartzJob> findByJobClassName(String jobClassName);

    boolean saveAndScheduleJob(QuartzJob quartzJob);

    boolean editAndScheduleJob(QuartzJob quartzJob) throws SchedulerException;

    boolean deleteAndStopJob(QuartzJob quartzJob);

    boolean resumeJob(QuartzJob quartzJob);
}

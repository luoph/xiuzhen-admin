package org.jeecg.modules.quartz.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.quartz.entity.QuartzJob;

import java.util.List;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 定时任务在线管理
 * @date 2019-01-02
 */
public interface QuartzJobMapper extends BaseMapper<QuartzJob> {

    List<QuartzJob> findByJobClassName(@Param("jobClassName") String jobClassName);

}

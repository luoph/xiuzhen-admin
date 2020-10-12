package org.jeecg.modules.game.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.game.entity.GameDataReportCount;

import java.util.Date;
import java.util.List;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 数据报表
 * @date 2020-10-10
 */
public interface GameDataReportCountMapper extends BaseMapper<GameDataReportCount> {

    List<GameDataReportCount> queryDataReportByDateRange(@Param("rangeDateBeginTime") Date rangeDateBeginTime,
                                                         @Param("rangeDateEndTime") Date rangeDateEndTime,
                                                         @Param("serverId") Integer serverId,
                                                         @Param("channel") String channel);
}

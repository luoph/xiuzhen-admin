package cn.youai.xiuzhen.stat.mapper;

import cn.youai.xiuzhen.stat.entity.CombatPowerLog;
import cn.youai.xiuzhen.stat.entity.LogPlayer;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description log_player
 * @date 2021-01-14
 */
public interface LogPlayerMapper extends BaseMapper<LogPlayer> {

    IPage<CombatPowerLog> selectCombatPowerLogList(Page<?> page,
                                                   @Param("channel") String channel,
                                                   @Param("serverId") Integer serverId,
                                                   @Param("playerId") Long playerId,
                                                   @Param("start") Date start,
                                                   @Param("end") Date end);
}

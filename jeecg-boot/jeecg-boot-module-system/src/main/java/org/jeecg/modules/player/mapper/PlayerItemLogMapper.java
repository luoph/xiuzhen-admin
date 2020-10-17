package org.jeecg.modules.player.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.player.entity.PlayerItemLog;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description player_item_log
 * @date 2020-07-21
 */
public interface PlayerItemLogMapper extends BaseMapper<PlayerItemLog> {

	List<PlayerItemLog> queryCurrencyPayIncomeList(@Param("rangeDateBeginTime") Date rangeDateBeginTime,
	                                               @Param("rangeDateEndTime") Date rangeDateEndTime,
	                                               @Param("serverId") Integer serverId,
	                                               @Param("channel") String channel,
	                                               @Param("type") int type,
	                                               @Param("itemId") int itemId);

	BigDecimal getBySyncTime(@Param("syncTime") Date syncTime,
	                         @Param("itemId") int itemId,
	                         @Param("type") int pay,
	                         @Param("serverId") Integer serverId);
}

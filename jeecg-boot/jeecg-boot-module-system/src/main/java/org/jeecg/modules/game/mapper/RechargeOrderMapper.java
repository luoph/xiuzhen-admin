package org.jeecg.modules.game.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.game.entity.RechargeOrder;

import java.util.Date;
import java.util.List;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 今日礼包
 * @date 2020-10-13
 */
public interface RechargeOrderMapper extends BaseMapper<RechargeOrder> {

	List<RechargeOrder> queryGiftList(@Param("rangeDateBeginTime") Date rangeDateBeginTime,
	                                  @Param("rangeDateEndTime") Date rangeDateEndTime,
	                                  @Param("goodsType") int goodsType);

	Long queryDAU(@Param("rangeDateBeginTime") Date rangeDateBeginTime,
	              @Param("rangeDateEndTime") Date rangeDateEndTime,
	              @Param("serverId") Integer serverId,
	              @Param("channel") String channel,
	              @Param("logTable") String logTable);
}

package org.jeecg.modules.game.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.game.entity.PayOrderBill;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description pay_order
 * @date 2020-09-29
 */
public interface PayOrderBillMapper extends BaseMapper<PayOrderBill> {

	BigDecimal queryBillSumByDateRange(@Param("payTimeBeginDate") Date payTimeBeginDate,
	                                   @Param("payTimeEndDate") Date payTimeEndDate,
	                                   @Param("serverId") Integer serverId,
	                                   @Param("channel") String channel);

	PayOrderBill queryPayGradeByDateRange(@Param("rangeDateBeginTime") Date rangeDateBeginTime,
	                                      @Param("rangeDateEndTime") Date rangeDateEndTime,
	                                      @Param("payRankBegin") int payRankBegin,
	                                      @Param("payRankEnd") int payRankEnd,
	                                      @Param("serverId") Integer serverId,
	                                      @Param("channel") String channel);

	PayOrderBill queryPayOrderList(@Param("rangeDateBeginTime") Date rangeDateBeginTime,
	                               @Param("rangeDateEndTime") Date rangeDateEndTime,
	                               @Param("serverId") Integer serverId,
	                               @Param("channel") String channel,
	                               @Param("dau") Long dau,
	                               @Param("goodsId") Integer goodsId);
}

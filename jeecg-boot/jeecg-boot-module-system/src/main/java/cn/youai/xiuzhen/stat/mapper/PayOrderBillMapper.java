package cn.youai.xiuzhen.stat.mapper;

import cn.youai.xiuzhen.game.entity.GameOrder;
import cn.youai.xiuzhen.stat.entity.PayOrderBill;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description game_order
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
                                          @Param("channel") String channel,
                                          @Param("payNumSum") Integer payNumSum);

    PayOrderBill queryPayOrderList(@Param("rangeDateBeginTime") Date rangeDateBeginTime,
                                   @Param("rangeDateEndTime") Date rangeDateEndTime,
                                   @Param("serverId") Integer serverId,
                                   @Param("channel") String channel,
                                   @Param("dau") Long dau,
                                   @Param("goodsId") Integer goodsId);

    Integer queryPayNumSum(@Param("rangeDateBeginTime") Date rangeDateBeginTime,
                           @Param("rangeDateEndTime") Date rangeDateEndTime,
                           @Param("serverId") Integer serverId,
                           @Param("channel") String channel);

    List<GameOrder> getPayAmountSum(@Param("serverId") int serverId);

    /**
     * 查询日期范围内所有用户的支付订单
     */
    @Select("select player_id, order_amount, pay_time from game_order where server_id = #{serverId} and channel = #{channel} and pay_time >= STR_TO_DATE(#{rangeDateBeginTime},'%Y-%m-%d %H:%i:%s') and pay_time <= STR_TO_DATE(#{rangeDateEndTime},'%Y-%m-%d %H:%i:%s')")
    List<Map> selectAllPayInfoByTimeRange(@Param("rangeDateBeginTime") String rangeDateBeginTime,
                                          @Param("rangeDateEndTime") String rangeDateEndTime,
                                          @Param("serverId") Integer serverId,
                                          @Param("channel") String channel);
}

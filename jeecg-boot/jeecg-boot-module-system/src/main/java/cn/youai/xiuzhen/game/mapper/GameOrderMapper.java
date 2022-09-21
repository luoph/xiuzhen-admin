package cn.youai.xiuzhen.game.mapper;

import cn.youai.xiuzhen.game.entity.GameOrder;
import cn.youai.xiuzhen.game.entity.MergeServerVO;
import cn.youai.xiuzhen.stat.entity.GameStatOrder;
import cn.youai.xiuzhen.stat.entity.GameStatRechargeGoods;
import cn.youai.xiuzhen.stat.entity.GameStatRechargeSum;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 充值订单
 * @date 2020-01-05
 */
public interface GameOrderMapper extends BaseMapper<GameOrder> {
    BigDecimal serverRangeAmount(@Param("serverId") int serverId,
                                 @Param("start") Date start,
                                 @Param("end") Date end);

    BigDecimal channelRangeAmount(@Param("channel") String channel,
                                  @Param("start") Date start,
                                  @Param("end") Date end);

    /**
     * 计算当天支付金额
     */
    BigDecimal serverPayAmount(@Param("serverId") int serverId,
                               @Param("payDate") Date payDate);

    BigDecimal chanelPayAmount(@Param("channel") String channel,
                               @Param("payDate") Date payDate);

    int serverPayPlayerNum(@Param("serverId") int serverId,
                           @Param("payDate") Date payDate);

    int channelPayPlayerNum(@Param("channel") String channel,
                            @Param("payDate") Date payDate);

    List<MergeServerVO> getGameOrderRangeDate(@Param("startTime") Date startTime,
                                              @Param("endTime") Date endTime);

    GameStatOrder queryOrderStatByRange(@Param("serverIds") String serverIds,
                                        @Param("serverNum") int serverNum,
                                        @Param("startDate") Date startDate,
                                        @Param("endDate") Date endDate);

    GameStatRechargeSum queryServerStatRechargeGoodsSum(@Param("serverId") int serverId,
                                                        @Param("goodsGroup") int goodsGroup,
                                                        @Param("startDate") Date startDate,
                                                        @Param("endDate") Date endDate);

    GameStatRechargeSum queryChannelStatRechargeGoodsSum(@Param("channel") String channel,
                                                         @Param("goodsGroup") int goodsGroup,
                                                         @Param("startDate") Date startDate,
                                                         @Param("endDate") Date endDate);

    List<GameStatRechargeGoods> queryServerStatRechargeGoods(@Param("serverId") int serverId,
                                                             @Param("goodsGroup") int goodsGroup,
                                                             @Param("startDate") Date startDate,
                                                             @Param("endDate") Date endDate);

    List<GameStatRechargeGoods> queryChannelStatRechargeGoods(@Param("channel") String channel,
                                                              @Param("goodsGroup") int goodsGroup,
                                                              @Param("startDate") Date startDate,
                                                              @Param("endDate") Date endDate);
}

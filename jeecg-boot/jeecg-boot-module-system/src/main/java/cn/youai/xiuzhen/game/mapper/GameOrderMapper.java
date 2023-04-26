package cn.youai.xiuzhen.game.mapper;

import cn.youai.basics.model.DateRange;
import cn.youai.server.model.RangeValue;
import cn.youai.xiuzhen.game.entity.GameOrder;
import cn.youai.xiuzhen.game.entity.MergeServerVO;
import cn.youai.xiuzhen.stat.entity.*;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 充值订单
 * @date 2020-01-05
 */
public interface GameOrderMapper extends BaseMapper<GameOrder> {

    GameOrder queryById(@Param("id") String id);

    List<GameOrder> queryByIds(@Param("orderIds") Collection<Long> orderIds);

    IPage<GameOrder> queryList(Page<?> page,
                               @Param("entity") GameOrder entity,
                               @Param("dateRange") DateRange dateRange,
                               @Param("amountRange") RangeValue<BigDecimal> amountRange,
                               @Param("configAuth") String configAuth);

    BigDecimal serverRangeAmount(@Param("serverId") int serverId,
                                 @Param("start") Date start,
                                 @Param("end") Date end);

    BigDecimal channelRangeAmount(@Param("channel") String channel,
                                  @Param("start") Date start,
                                  @Param("end") Date end);

    BigDecimal payAmount(@Param("channel") String channel,
                         @Param("sdkChannel") String sdkChannel,
                         @Param("serverId") Integer serverId,
                         @Param("payDate") Date payDate,
                         @Param("configAuth") String configAuth);

    int payPlayerNum(@Param("channel") String channel,
                     @Param("sdkChannel") String sdkChannel,
                     @Param("serverId") Integer serverId,
                     @Param("payDate") Date payDate,
                     @Param("configAuth") String configAuth);

    List<MergeServerVO> getGameOrderRangeDate(@Param("startTime") Date startTime,
                                              @Param("endTime") Date endTime);

    GameStatOrder queryOrderStatByRange(@Param("serverIds") String serverIds,
                                        @Param("serverNum") Integer serverNum,
                                        @Param("startDate") Date startDate,
                                        @Param("endDate") Date endDate);


    GameStatRechargeSum queryStatRechargeGoodsSum(@Param("channel") String channel,
                                                  @Param("serverId") Integer serverId,
                                                  @Param("goodsGroup") Integer goodsGroup,
                                                  @Param("startDate") Date startDate,
                                                  @Param("endDate") Date endDate);

    List<GameStatRechargeGoods> queryStatRechargeGoods(@Param("channel") String channel,
                                                       @Param("serverId") Integer serverId,
                                                       @Param("goodsGroup") Integer goodsGroup,
                                                       @Param("startDate") Date startDate,
                                                       @Param("endDate") Date endDate);

    GameStatRechargeSum queryStatRechargeGradeSum(@Param("channel") String channel,
                                                  @Param("serverId") Integer serverId,
                                                  @Param("startDate") Date startDate,
                                                  @Param("endDate") Date endDate);

    List<GameStatPlayerRechargeAmount> queryPlayerRechargeAmount(@Param("channel") String channel,
                                                                 @Param("serverId") Integer serverId,
                                                                 @Param("startDate") Date startDate,
                                                                 @Param("endDate") Date endDate);

    IPage<GameStatRechargeRank> queryRechargeRankList(Page<?> page,
                                                      @Param("channel") String channel,
                                                      @Param("sdkChannel") String sdkChannel,
                                                      @Param("serverId") Integer serverId,
                                                      @Param("dateRange") DateRange dateRange,
                                                      @Param("configAuth") String configAuth);

    List<GameOrder> sumAmountGroupByPlayerId(@Param("serverIds") Set<Integer> serverIds);

}

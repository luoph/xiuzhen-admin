package cn.youai.xiuzhen.stat.service;

import cn.youai.basics.model.DateRange;
import cn.youai.server.model.RangeValue;
import cn.youai.xiuzhen.game.entity.GameOrder;
import cn.youai.xiuzhen.stat.entity.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 充值订单
 * @date 2020-01-05
 */
public interface IGameOrderService extends IService<GameOrder> {
    /**
     * 通过id查询记录
     */
    GameOrder queryById(String id);

    List<GameOrder> queryByIds(Collection<Long> ids);

    List<GameOrder> queryPlayerTotalPayAmount(Collection<Long> ids);

    /**
     * 订单列表查询
     */
    IPage<GameOrder> queryList(Page<?> page, GameOrder entity, DateRange createDateRange, RangeValue<BigDecimal> payAmountRange);

    /**
     * 付费金额总和
     */
    BigDecimal serverRangeAmount(int serverId, Date start, Date end);

    BigDecimal channelRangeAmount(String channel, Date start, Date end);

    BigDecimal payAmount(String channel, String sdkChannel, Integer serverId, Date date, String configAuth);

    /**
     * 付费角色数
     */
    int payPlayerNum(String channel, String sdkChannel, Integer serverId, Date date, String configAuth);

    GameStatOrder queryOrderStatByRange(List<Integer> serverIds, Date startDate, Date endDate);

    GameStatRechargeSum queryStatRechargeGoodsSum(String channel, Integer serverId, Integer goodsGroup, Date start, Date end);

    List<GameStatRechargeGoods> queryStatRechargeGoods(String channel, Integer serverId, Integer goodsGroup, Date start, Date end);

    GameStatRechargeSum queryStatRechargeGradeSum(String channel, Integer serverId, Date start, Date end);

    List<GameStatPlayerRechargeAmount> queryPlayerRechargeAmount(String channel, Integer serverId, Date start, Date end);

    /**
     * 查询充值榜单
     */
    IPage<GameStatRechargeRank> queryRechargeRankList(Page<?> page, String channel, String sdkChannel, Integer serverId, Long playerId, DateRange dateRange);
}

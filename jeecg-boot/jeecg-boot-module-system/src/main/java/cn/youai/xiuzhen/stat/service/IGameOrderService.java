package cn.youai.xiuzhen.stat.service;

import cn.youai.basics.model.DateRange;
import cn.youai.server.model.RangeValue;
import cn.youai.xiuzhen.game.entity.GameOrder;
import cn.youai.xiuzhen.stat.entity.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.math.BigDecimal;
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
    GameOrder selectById(String id);

    /**
     * 订单列表查询
     */
    IPage<GameOrder> selectList(Page<?> page, GameOrder entity, DateRange createDateRange, RangeValue<BigDecimal> payAmountRange);

    /**
     * 付费金额总和
     */
    BigDecimal serverRangeAmount(int serverId, Date start, Date end);

    BigDecimal channelRangeAmount(String channel, Date start, Date end);

    /**
     * 付费金额总和
     */
    BigDecimal serverPayAmount(int serverId, Date date);

    BigDecimal channelPayAmount(String channel, Integer serverId, Date date);

    /**
     * 付费角色数
     */
    int serverPayPlayerNum(int serverId, Date date);

    /**
     * 付费角色数
     */
    int channelPayPlayerNum(String channel, Integer serverId, Date date);

    GameStatOrder queryOrderStatByRange(List<Integer> serverIds, Date startDate, Date endDate);

    GameStatRechargeSum queryStatRechargeGoodsSum(String channel, int serverId, int goodsGroup, Date start, Date end);

    List<GameStatRechargeGoods> queryStatRechargeGoods(String channel, int serverId, int goodsGroup, Date start, Date end);

    GameStatRechargeSum queryStatRechargeGradeSum(String channel, int serverId, Date start, Date end);

    List<GameStatPlayerRechargeAmount> queryPlayerRechargeAmount(String channel, int serverId, Date start, Date end);

    /**
     * 查询充值榜单
     */
    List<GameStatRechargeRank> queryRechargeRankList(String channel, int serverId, Date start, Date end);
}

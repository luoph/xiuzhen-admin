package cn.youai.xiuzhen.stat.service;

import cn.youai.xiuzhen.game.entity.GameOrder;
import cn.youai.xiuzhen.stat.entity.*;
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
public interface IGameOrderStatService extends IService<GameOrder> {

    /**
     * 付费金额总和
     */
    BigDecimal serverRangeAmount(int serverId, Date start, Date end);

    BigDecimal channelRangeAmount(String channel, Date start, Date end);

    /**
     * 付费金额总和
     */
    BigDecimal serverPayAmount(int serverId, Date date);

    BigDecimal channelPayAmount(String channel, Date date);

    /**
     * 付费角色数
     */
    int serverPayPlayerNum(int serverId, Date date);

    /**
     * 付费角色数
     */
    int channelPayPlayerNum(String channel, Date date);

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

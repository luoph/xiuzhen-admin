package cn.youai.xiuzhen.stat.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.youai.xiuzhen.game.entity.GameOrder;
import cn.youai.xiuzhen.game.mapper.GameOrderMapper;
import cn.youai.xiuzhen.stat.entity.*;
import cn.youai.xiuzhen.stat.service.IGameOrderStatService;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 充值订单
 * @date 2020-01-05
 */
@Service
@DS("shardingSphere")
public class GameOrderStatServiceImpl extends ServiceImpl<GameOrderMapper, GameOrder> implements IGameOrderStatService {

    @Override
    public BigDecimal serverRangeAmount(int serverId, Date start, Date end) {
        return getBaseMapper().serverRangeAmount(serverId, start, end);
    }

    @Override
    public BigDecimal channelRangeAmount(String channel, Date start, Date end) {
        return getBaseMapper().channelRangeAmount(channel, start, end);
    }

    @Override
    public BigDecimal serverPayAmount(int serverId, Date date) {
        return getBaseMapper().serverPayAmount(serverId, date);
    }

    @Override
    public BigDecimal channelPayAmount(String channel, Date date) {
        return getBaseMapper().chanelPayAmount(channel, date);
    }

    @Override
    public int serverPayPlayerNum(int serverId, Date date) {
        return getBaseMapper().serverPayPlayerNum(serverId, date);
    }

    @Override
    public int channelPayPlayerNum(String channel, Date date) {
        return getBaseMapper().channelPayPlayerNum(channel, date);
    }

    @Override
    public GameStatOrder queryOrderStatByRange(List<Integer> serverIds, Date startDate, Date endDate) {
        return getBaseMapper().queryOrderStatByRange(StrUtil.join(",", serverIds), serverIds.size(), startDate, endDate);
    }

    public GameStatRechargeSum queryStatRechargeGoodsSum(String channel, int serverId, int goodsGroup, Date start, Date end) {
        return getBaseMapper().queryStatRechargeGoodsSum(channel, serverId, goodsGroup, start, end);
    }

    @Override
    public List<GameStatRechargeGoods> queryStatRechargeGoods(String channel, int serverId, int goodsGroup, Date start, Date end) {
        return getBaseMapper().queryStatRechargeGoods(channel, serverId, goodsGroup, start, end);
    }

    @Override
    public GameStatRechargeSum queryStatRechargeGradeSum(String channel, int serverId, Date start, Date end) {
        return getBaseMapper().queryStatRechargeGradeSum(channel, serverId, start, end);
    }

    @Override
    public List<GameStatPlayerRechargeAmount> queryPlayerRechargeAmount(String channel, int serverId, Date start, Date end) {
        return getBaseMapper().queryPlayerRechargeAmount(channel, serverId, start, end);
    }

    @Override
    public List<GameStatRechargeRank> queryRechargeRankList(String channel, int serverId, Date start, Date end) {
        return getBaseMapper().queryRechargeRankList(channel, serverId, start, end);
    }
}

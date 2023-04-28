package cn.youai.xiuzhen.stat.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.youai.basics.model.DateRange;
import cn.youai.server.model.RangeValue;
import cn.youai.xiuzhen.game.entity.GameOrder;
import cn.youai.xiuzhen.game.mapper.GameOrderMapper;
import cn.youai.xiuzhen.stat.entity.*;
import cn.youai.xiuzhen.stat.service.IGameOrderService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.common.system.query.QueryGenerator;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 充值订单
 * @date 2020-01-05
 */
@Service
public class GameOrderServiceImpl extends ServiceImpl<GameOrderMapper, GameOrder> implements IGameOrderService {

    @Override
    public GameOrder queryById(String id) {
        return getBaseMapper().queryById(id);
    }

    @Override
    public List<GameOrder> queryByIds(Collection<Long> ids) {
        if (CollUtil.isEmpty(ids)) {
            return new ArrayList<>();
        }
        return getBaseMapper().queryByIds(ids);
    }

    @Override
    public List<GameOrder> queryPlayerTotalPayAmount(Collection<Long> playerIds) {
        if (CollUtil.isEmpty(playerIds)) {
            return new ArrayList<>();
        }
        return getBaseMapper().queryPlayerTotalPayAmount(playerIds);
    }

    @Override
    public IPage<GameOrder> queryList(Page<?> page, GameOrder entity, DateRange dateRange, RangeValue<BigDecimal> amountRange) {
        String configAuth = QueryGenerator.installAuthJdbc(GameOrder.class);
        return getBaseMapper().queryList(page, entity, dateRange, amountRange, configAuth);
    }

    @Override
    public BigDecimal serverRangeAmount(int serverId, Date start, Date end) {
        return getBaseMapper().serverRangeAmount(serverId, start, end);
    }

    @Override
    public BigDecimal channelRangeAmount(String channel, Date start, Date end) {
        return getBaseMapper().channelRangeAmount(channel, start, end);
    }

    @Override
    public BigDecimal payAmount(String channel, String sdkChannel, Integer serverId, Date date, String configAuth) {
        return getBaseMapper().payAmount(channel, sdkChannel, serverId, date, configAuth);
    }

    @Override
    public int payPlayerNum(String channel, String sdkChannel, Integer serverId, Date date, String configAuth) {
        return getBaseMapper().payPlayerNum(channel, sdkChannel, serverId, date, configAuth);
    }

    @Override
    public GameStatOrder queryOrderStatByRange(List<Integer> serverIds, Date startDate, Date endDate) {
        return getBaseMapper().queryOrderStatByRange(StrUtil.join(",", serverIds), serverIds.size(), startDate, endDate);
    }

    public GameStatRechargeSum queryStatRechargeGoodsSum(String channel, Integer serverId, Integer goodsGroup, Date start, Date end) {
        return getBaseMapper().queryStatRechargeGoodsSum(channel, serverId, goodsGroup, start, end);
    }

    @Override
    public List<GameStatRechargeGoods> queryStatRechargeGoods(String channel, Integer serverId, Integer goodsGroup, Date start, Date end) {
        return getBaseMapper().queryStatRechargeGoods(channel, serverId, goodsGroup, start, end);
    }

    @Override
    public GameStatRechargeSum queryStatRechargeGradeSum(String channel, Integer serverId, Date start, Date end) {
        return getBaseMapper().queryStatRechargeGradeSum(channel, serverId, start, end);
    }

    @Override
    public List<GameStatPlayerRechargeAmount> queryPlayerRechargeAmount(String channel, Integer serverId, Date start, Date end) {
        return getBaseMapper().queryPlayerRechargeAmount(channel, serverId, start, end);
    }

    @Override
    public IPage<GameStatRechargeRank> queryRechargeRankList(Page<?> page, String channel, String sdkChannel, Integer serverId, DateRange dateRange) {
        String configAuth = QueryGenerator.getAllConfigAuth();
        return getBaseMapper().queryRechargeRankList(page, channel, sdkChannel, serverId, dateRange, configAuth);
    }
}

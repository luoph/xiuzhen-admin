package org.jeecg.modules.game.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.youai.xiuzhen.utils.BigDecimalUtil;
import cn.youai.xiuzhen.utils.DateUtils;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.util.CollectionPageHelp;
import org.jeecg.modules.game.entity.GameStatOrder;
import org.jeecg.modules.game.mapper.GameStatOrderMapper;
import org.jeecg.modules.game.service.IGameStatOrderService;
import org.jeecg.modules.player.service.ILogAccountService;
import org.jeecg.modules.player.service.IPayOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description game_stat_order
 * @date 2021-04-20
 */
@Service
public class GameStatOrderServiceImpl extends ServiceImpl<GameStatOrderMapper, GameStatOrder> implements IGameStatOrderService {

    @Autowired
    IPayOrderService payOrderService;

    @Autowired
    ILogAccountService logAccountService;

    @Override
    public IPage<GameStatOrder> queryGameStatOrderList(GameStatOrder gameStatOrder, Integer pageNo, Integer pageSize) {
        // 计算数据(按日期)
        int num = DateUtils.daysBetween(gameStatOrder.getStartDate(), gameStatOrder.getEndDate());
        List<Date> dateList = new ArrayList<>(num);
        for (int i = 0; i <= num; i++) {
            dateList.add(DateUtils.minusDays(gameStatOrder.getEndDate(), i));
        }
        List<Date> datePageList = CollectionPageHelp.pageBySubList(dateList, pageSize, pageNo);

        List<GameStatOrder> gameStatOrders = queryGameStatOrderList(gameStatOrder, datePageList, CommonConstant.PAY_ORDER_STAT_TYPE_DATE);

        if (CollectionUtil.isEmpty(gameStatOrders)) {
            return null;
        }
        int size = gameStatOrders.size();

        // 计算汇总数据(按月份)
        List<GameStatOrder> gameStatOrdersByMonths = queryGameStatOrderListByMonth(gameStatOrder);
        if (CollectionUtil.isNotEmpty(gameStatOrdersByMonths)) {
            gameStatOrders.addAll(gameStatOrdersByMonths);
        }

        // 计算汇总数据(按年度)
        List<GameStatOrder> gameStatOrdersByYears = queryGameStatOrderListByYear(gameStatOrder);
        if (CollectionUtil.isNotEmpty(gameStatOrdersByYears)) {
            gameStatOrders.addAll(gameStatOrdersByYears);
        }

        IPage<GameStatOrder> result = new Page<>(pageNo, pageSize);
        result.setTotal(size);
        result.setRecords(gameStatOrders);
        return result;
    }

    private List<GameStatOrder> queryGameStatOrderList(GameStatOrder gameStatOrder, List<Date> dateList, int type) {
        // 活跃玩家
        Map<String, Integer> activePlayerByLoginDates = logAccountService.getPlayerNumByLoginDates(gameStatOrder.getServerId() != null ?
                gameStatOrder.getServerId() : 0, dateList, type);
        if (CollectionUtil.isEmpty(activePlayerByLoginDates)) {
            return null;
        }
        // 主体
        List<GameStatOrder> gameStatOrders = payOrderService.statOrderByDates(gameStatOrder.getServerId() != null ?
                gameStatOrder.getServerId() : 0, dateList, type);

        if (CollectionUtil.isEmpty(gameStatOrders)) {
            return null;
        }
        genGameStatOrders(gameStatOrders, activePlayerByLoginDates, type);

        return gameStatOrders;
    }

    /**
     * 统计月份
     */
    private List<GameStatOrder> queryGameStatOrderListByMonth(GameStatOrder gameStatOrder) {
        int num = DateUtils.monthsBetween(gameStatOrder.getStartDate(), gameStatOrder.getEndDate());
        List<Date> monthsDateList = new ArrayList<>(num);
        for (int i = 0; i <= num; i++) {
            monthsDateList.add(DateUtils.minusDays(gameStatOrder.getEndDate(), i));
        }
        return queryGameStatOrderList(gameStatOrder, monthsDateList, CommonConstant.PAY_ORDER_STAT_TYPE_MONTH);
    }

    /**
     * 统计年度
     */
    private List<GameStatOrder> queryGameStatOrderListByYear(GameStatOrder gameStatOrder) {
        int num = DateUtils.yearsBetween(gameStatOrder.getStartDate(), gameStatOrder.getEndDate());
        List<Date> yearsDateList = new ArrayList<>(num);
        for (int i = 0; i <= num; i++) {
            yearsDateList.add(DateUtils.minusDays(gameStatOrder.getEndDate(), i));
        }
        return queryGameStatOrderList(gameStatOrder, yearsDateList, CommonConstant.PAY_ORDER_STAT_TYPE_YEAR);
    }

    /**
     * 计算逻辑
     *
     * @param gameStatOrders           主体
     * @param activePlayerByLoginDates 活跃玩家Map
     */
    private void genGameStatOrders(List<GameStatOrder> gameStatOrders, Map<String, Integer> activePlayerByLoginDates, int type) {
        gameStatOrders.forEach(item -> {
            int activeNum = activePlayerByLoginDates.get(item.getDate()) == null ? 0 : activePlayerByLoginDates.get(item.getDate());
            // 活跃玩家
            item.setActiveNum(activeNum);
            if (activeNum != 0) {
                // 付款率
                item.setPayRate(BigDecimalUtil.dividePercent(BigDecimalUtil.divide((double) item.getPayNum(), activeNum, 4).doubleValue()).toString());
                // arpu
                item.setArpu(BigDecimalUtil.divide(item.getAmount().doubleValue(), activeNum));
            } else {
                item.setPayRate("0.00");
                item.setArpu(new BigDecimal(0));
            }

            // arppu
            if (item.getPayNum() != null && item.getPayNum() != 0) {
                item.setArppu(BigDecimalUtil.divide(item.getAmount().doubleValue(), item.getPayNum()));
            } else {
                item.setArppu(new BigDecimal(0));
            }

            // 服均充值
            if (item.getServerNum() != null && item.getServerNum() != 0) {
                item.setServerAverageAmount(item.getAmount() != null ?
                        BigDecimalUtil.divide(item.getAmount().doubleValue(), (double) item.getServerNum()) : new BigDecimal(0));
            } else {
                item.setServerAverageAmount(new BigDecimal(0));
            }
        });

        if (type == CommonConstant.PAY_ORDER_STAT_TYPE_YEAR) {
            gameStatOrders.forEach(e -> e.setDate(e.getDate() + "汇总"));
        }
    }
}

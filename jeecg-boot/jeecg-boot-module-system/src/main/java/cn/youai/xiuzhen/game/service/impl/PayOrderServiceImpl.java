package cn.youai.xiuzhen.game.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.youai.server.utils.DateUtils;
import cn.youai.xiuzhen.game.entity.GameOrder;
import cn.youai.xiuzhen.game.entity.GameStatOrder;
import cn.youai.xiuzhen.game.mapper.GameOrderMapper;
import cn.youai.xiuzhen.game.service.IPayOrderService;
import cn.youai.xiuzhen.utils.BigDecimalUtils;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.constant.TimeConstant;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 充值订单
 * @date 2020-01-05
 */
@Service
@DS("shardingSphere")
public class PayOrderServiceImpl extends ServiceImpl<GameOrderMapper, GameOrder> implements IPayOrderService {

    @Resource
    private GameOrderMapper gameOrderMapper;

    @Override
    public double sumPayAmount(int serverId, String date) {
        return gameOrderMapper.getSumPayAmount(serverId, date);
    }

    @Override
    public int countPayPlayer(int serverId, String date) {
        return gameOrderMapper.getCountPayPlayer(serverId, date);
    }

    @Override
    public List<GameStatOrder> statOrderByDates(int serverId, List<Date> dateList, int type) {
        if (CollUtil.isEmpty(dateList)) {
            return null;
        }

        List<JSONObject> dateJsonList = new ArrayList<>(dateList.size());
        Date now = DateUtils.now();
        dateList.forEach(e -> {
            Date[] startAndEnd = null;
            if (type == CommonConstant.PAY_ORDER_STAT_TYPE_MONTH) {
                startAndEnd = new Date[]{DateUtil.beginOfMonth(now), DateUtil.endOfMonth(now)};
            } else if (type == CommonConstant.PAY_ORDER_STAT_TYPE_YEAR) {
                startAndEnd = new Date[]{DateUtil.beginOfYear(now), DateUtil.endOfYear(now)};
            } else {
                startAndEnd = new Date[]{DateUtil.beginOfDay(now), DateUtil.endOfDay(now)};
            }
            JSONObject jsonParam = new JSONObject();
            jsonParam.put("startTime", startAndEnd[0]);
            jsonParam.put("endTime", startAndEnd[1]);
            dateJsonList.add(jsonParam);
        });

        List<GameOrder> statPlayerNumAmountByDates = gameOrderMapper.getStatPlayerNumAmountByDates(serverId, dateJsonList);
        if (CollUtil.isNotEmpty(statPlayerNumAmountByDates)) {
            Map<String, GameStatOrder> result = new HashMap<>(dateList.size());
            statPlayerNumAmountByDates.forEach(item -> {
                Date date = dateList.get(item.getId().intValue());
                String formatDate;
                if (type == CommonConstant.PAY_ORDER_STAT_TYPE_MONTH) {
                    formatDate = DateUtils.formatDate(date, TimeConstant.CHINESE_YEAR_MONTH_PATTERN);
                } else if (type == CommonConstant.PAY_ORDER_STAT_TYPE_YEAR) {
                    formatDate = DateUtils.formatDate(date, TimeConstant.CHINESE_YEAR_PATTERN);
                } else {
                    formatDate = DateUtils.formatDate(date, DatePattern.NORM_DATE_PATTERN);
                }

                GameStatOrder gameStatOrder = result.get(formatDate);
                if (gameStatOrder == null) {
                    gameStatOrder = new GameStatOrder();
                    // 日期
                    gameStatOrder.setDate(formatDate);
                    // 服务器id
                    gameStatOrder.setServerId(serverId);
                    // 区服数
                    gameStatOrder.setServerNum(1);
                    // 付费角色数
                    gameStatOrder.setPayNum(1);
                    // 付费金额
                    gameStatOrder.setAmount(item.getPayAmount() != null ? item.getPayAmount() : new BigDecimal(0));
                } else {
                    // 付费角色数
                    gameStatOrder.setPayNum(gameStatOrder.getPayNum() + 1);
                    // 付费金额
                    gameStatOrder.setAmount(BigDecimalUtils.add(gameStatOrder.getAmount(), item.getPayAmount()));
                }
                result.put(formatDate, gameStatOrder);
            });

            // 列表需要打印空的日期
            if (type == CommonConstant.PAY_ORDER_STAT_TYPE_DATE) {
                dateList.forEach(date -> {
                    String formatDate = DateUtils.formatDate(date, DatePattern.NORM_DATE_PATTERN);
                    GameStatOrder gameStatOrder = result.get(formatDate);
                    if (gameStatOrder == null) {
                        result.put(formatDate, new GameStatOrder(formatDate, serverId, 1, 0, new BigDecimal(0)));
                    }
                });
            }
            return new ArrayList<>(result.values());
        }
        return null;
    }
}

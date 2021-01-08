package org.jeecg.modules.game.service.impl;

import cn.hutool.core.date.DatePattern;
import cn.youai.xiuzhen.utils.BigDecimalUtil;
import cn.youai.xiuzhen.utils.DateUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.modules.game.entity.PayOrderBill;
import org.jeecg.modules.game.mapper.PayOrderBillMapper;
import org.jeecg.modules.game.service.IPayOrderBillService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description game_order
 * @date 2020-09-29
 */
@Service
public class PayOrderBillServiceImpl extends ServiceImpl<PayOrderBillMapper, PayOrderBill> implements IPayOrderBillService {

    /**
     * 充值档位
     */
    private final static String[] PAYRANKS = {"0-6", "7-29", "30-67", "68-97", "98-197", "198-327", "328-647", "648-9999"};

    @Resource
    private PayOrderBillMapper payOrderBillMapper;


    @Override
    public BigDecimal queryBillSumByDateRange(String payTimeBegin, String payTimeEnd, Integer serverId, String channel) {
        // 如果选择开始时间和结束时间是同一天
        Date payTimeBeginDate = null;
        Date payTimeEndDate = null;
        payTimeBeginDate = DateUtils.parseDate(payTimeBegin);
        payTimeEndDate = DateUtils.parseDate(payTimeEnd);

        if (payTimeBegin.equals(payTimeEnd)) {
            Date[] dates = DateUtils.dateStartAndEnd(payTimeBeginDate);
            payTimeBeginDate = dates[0];
            payTimeEndDate = dates[1];
        }
        return payOrderBillMapper.queryBillSumByDateRange(payTimeBeginDate, payTimeEndDate, serverId, channel);
    }

    @Override
    public PayOrderBill queryPayGradeByDateRange(String rangeDateBegin, String rangeDateEnd, String payRank, int days, Integer serverId, String channel) {
        PayOrderBill payOrderBill = null;
        String[] payRanks = payRank.split("-");
        int payRankBegin = Integer.parseInt(payRanks[0]);
        int payRankEnd = Integer.parseInt(payRanks[1]);
        if (days == 0) {
            // 如果选择开始时间和结束时间是同一天
            Date rangeDateBeginTime = null;
            Date rangeDateEndTime = null;
            rangeDateBeginTime = DateUtils.parseDate(rangeDateBegin);
            rangeDateEndTime = DateUtils.parseDate(rangeDateEnd);
            if (rangeDateBegin.equals(rangeDateEnd)) {
                Date[] dates = DateUtils.dateStartAndEnd(rangeDateBeginTime);
                rangeDateBeginTime = dates[0];
                rangeDateEndTime = dates[1];
            }
            // 查询该档位下付费人数和
            Integer payNumSum = payOrderBillMapper.queryPayNumSum(rangeDateBeginTime, rangeDateEndTime, serverId, channel);
            payOrderBill = payOrderBillMapper.queryPayGradeByDateRange(rangeDateBeginTime, rangeDateEndTime, payRankBegin, payRankEnd, serverId, channel, payNumSum);
            payOrderBill.setPayRank(payRank);
            return getDataTreating(payOrderBill);

        }
        // 如果有选天数,就使用就近天数查询
        // 获取过去第几天的日期
        Date nowDate = new Date();
        Date pastDate = DateUtils.addDays(nowDate, days * (-1));
        Integer payNumSum = payOrderBillMapper.queryPayNumSum(pastDate, nowDate, serverId, channel);
        payOrderBill = payOrderBillMapper.queryPayGradeByDateRange(pastDate, nowDate, payRankBegin, payRankEnd, serverId, channel, payNumSum);
        payOrderBill.setPayRank(payRank);
        return getDataTreating(payOrderBill);
    }

    @Override
    public List<PayOrderBill> queryForList(String payRankString, String rangeDateBegin, String rangeDateEnd, int days, Integer serverId, String channel) {
        List<String> payRankList = new ArrayList<>();
        if(!StringUtils.isEmpty(payRankString)){
            payRankList.add(payRankString);
        }else{
            for (String payrank : PAYRANKS) {
                payRankList.add(payrank);
            }
        }

        List<PayOrderBill> list = new ArrayList<>();
        for (String payRank : payRankList) {
            PayOrderBill payOrderBill = new PayOrderBill();
            String[] payRanks = payRank.split("-");
            int payRankBegin = Integer.parseInt(payRanks[0]);
            int payRankEnd = Integer.parseInt(payRanks[1]);

            Date rangeDateBeginTime = DateUtils.parseDate(rangeDateBegin + " 00:00:00");
            Date rangeDateEndTime = DateUtils.parseDate(rangeDateEnd + " 23:59:59");
            //查询日期范围内所有支付金额范围内支付订单
            List<Map> allPayInfoList = payOrderBillMapper.selectAllPayInfoByTimeRange(payRankBegin, payRankEnd,DateUtils.formatDate(rangeDateBeginTime, DatePattern.NORM_DATETIME_PATTERN), DateUtils.formatDate(rangeDateEndTime, DatePattern.NORM_DATETIME_PATTERN), serverId, channel);
            //支付订单按player_id收集并去重
            List<Map> allPayInforList_equals_playerId = allPayInfoList.stream().collect(
                    Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(o -> o.get("player_id").toString()))), ArrayList::new));
            //设置 付费人数
            payOrderBill.setPayNumSum(allPayInforList_equals_playerId.size());
            //设置 充值档位
            payOrderBill.setPayRank(payRank);
            //设置 人数占比
//            payOrderBill.setPayNumSumRate();
            //支付订单按order_amount求和
            Double sumOrderAmount = allPayInfoList.stream().mapToDouble(map -> Double.parseDouble(map.get("order_amount").toString())).sum();
            //设置 付费金额
            payOrderBill.setPayAmountSum(new BigDecimal(sumOrderAmount.toString()).setScale(4, BigDecimal.ROUND_HALF_UP));
            //设置 金额占比
//            payOrderBill.setPayAmountSumRate();
            //设置 ARPPU（付费金额 除以 付费人数）
            if(0 == payOrderBill.getPayNumSum()){
                payOrderBill.setArppu(new BigDecimal("0").setScale(4, BigDecimal.ROUND_HALF_UP));
            }else{
                payOrderBill.setArppu(BigDecimalUtil.divide(payOrderBill.getPayAmountSum().doubleValue(),payOrderBill.getPayNumSum(),4));
            }

            list.add(payOrderBill);

        }
        return list;
    }

    /**
     * 数据处理
     *
     * @param payOrderBill
     * @return
     */
    private PayOrderBill getDataTreating(PayOrderBill payOrderBill) {

        BigDecimal payNumSumRate = payOrderBill.getPayNumSumRate();
        BigDecimal payAmountSumRate = payOrderBill.getPayAmountSumRate();
        BigDecimal arppu = payOrderBill.getArppu();
        BigDecimal payAmountSum = payOrderBill.getPayAmountSum();

        if (payAmountSum == null) {
            payAmountSum = BigDecimal.ZERO;
        }
        payOrderBill.setPayAmountSum(payAmountSum);

        if (payNumSumRate == null) {
            payNumSumRate = BigDecimal.ZERO;
        }
        payOrderBill.setPayNumSumRate(BigDecimalUtil.dividePercent(payNumSumRate.doubleValue()));

        if (payAmountSumRate == null) {
            payAmountSumRate = BigDecimal.ZERO;
        }
        payOrderBill.setPayAmountSumRate(BigDecimalUtil.dividePercent(payAmountSumRate.doubleValue()));

        if (arppu == null) {
            arppu = BigDecimal.ZERO;
        }
        payOrderBill.setArppu(BigDecimalUtil.divide(arppu.doubleValue(), 1, 3));

        return payOrderBill;
    }
}

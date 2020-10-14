package org.jeecg.modules.game.service.impl;

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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description pay_order
 * @date 2020-09-29
 */
@Service
public class PayOrderBillServiceImpl extends ServiceImpl<PayOrderBillMapper, PayOrderBill> implements IPayOrderBillService {

    private static String [] PAYRANKS = {"0-6", "7-29", "30-67", "68-97", "98-197", "198-327", "328-647", "648-9999"};

    @Resource
    private PayOrderBillMapper payOrderBillMapper;


    @Override
    public BigDecimal queryBillSumByDateRange(String payTimeBegin, String payTimeEnd, Integer serverId, String channel) {
        Date payTimeBeginDate = DateUtils.parseDate(payTimeBegin);
        Date payTimeEndDate = DateUtils.parseDate(payTimeEnd);

        BigDecimal billSum = payOrderBillMapper.queryBillSumByDateRange(payTimeBeginDate, payTimeEndDate, serverId, channel);
        return billSum;
    }

    @Override
    public PayOrderBill queryPaygGradeByDateRange(String rangeDateBegin, String rangeDateEnd, String payRank, int days, Integer serverId, String channel) {
        PayOrderBill payOrderBill= null;
        String[] payRanks = payRank.split("-");
        int payRankBegin = Integer.parseInt(payRanks[0]);
        int payRankEnd = Integer.parseInt(payRanks[1]);
        if (days == 0){
            Date rangeDateBeginTime = DateUtils.parseDate(rangeDateBegin);
            Date rangeDateEndTime = DateUtils.parseDate(rangeDateEnd);
            payOrderBill = payOrderBillMapper.queryPaygGradeByDateRange(rangeDateBeginTime, rangeDateEndTime, payRankBegin, payRankEnd, serverId, channel);
            payOrderBill.setPayRank(payRank);
            return  getDataTreating(payOrderBill);

        }
        // 如果有选天数,就使用就近天数查询
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // 获取过去第几天的日期
        Date pastDate = DateUtils.getPastDate(days, sdf);
        Date nowDate = new Date();
        payOrderBill = payOrderBillMapper.queryPaygGradeByDateRange(pastDate, nowDate, payRankBegin, payRankEnd, serverId, channel);
        payOrderBill.setPayRank(payRank);
        return getDataTreating(payOrderBill);
    }

    @Override
    public List<PayOrderBill> queryForList(String rangeDateBegin, String rangeDateEnd, int days, Integer serverId, String channel) {
        List<PayOrderBill> list = new ArrayList<>();
        PayOrderBill payOrderBill= null;
        for (String payRank : PAYRANKS) {
            String[] payRanks = payRank.split("-");
            int payRankBegin = Integer.parseInt(payRanks[0]);
            int payRankEnd = Integer.parseInt(payRanks[1]);

            if (days == 0){
                Date rangeDateBeginTime = DateUtils.parseDate(rangeDateBegin);
                Date rangeDateEndTime = DateUtils.parseDate(rangeDateEnd);
                payOrderBill = payOrderBillMapper.queryPaygGradeByDateRange(rangeDateBeginTime, rangeDateEndTime, payRankBegin, payRankEnd, serverId, channel);
                payOrderBill.setPayRank(payRank);
                list.add(getDataTreating(payOrderBill));
            }else {
                // 如果有选天数,就使用就近天数查询
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                // 获取过去第几天的日期
                Date pastDate = DateUtils.getPastDate(days, sdf);
                Date nowDate = new Date();
                payOrderBill = payOrderBillMapper.queryPaygGradeByDateRange(pastDate, nowDate, payRankBegin, payRankEnd, serverId, channel);
                payOrderBill.setPayRank(payRank);
                list.add(getDataTreating(payOrderBill));
            }
        }
        return list;
    }

    /**
     *  数据处理
     * @param payOrderBill
     * @return
     */
    public PayOrderBill getDataTreating(PayOrderBill payOrderBill){

        BigDecimal payNumSumRate = payOrderBill.getPayNumSumRate();
        BigDecimal payAmountSumRate = payOrderBill.getPayAmountSumRate();
        BigDecimal arppu = payOrderBill.getArppu();
        BigDecimal payAmountSum = payOrderBill.getPayAmountSum();

        if (payAmountSum == null){
            payAmountSum = new BigDecimal(0);
        }
        payOrderBill.setPayAmountSum(payAmountSum);

        if (payNumSumRate == null){
            payNumSumRate = new BigDecimal(0);
        }
        payOrderBill.setPayNumSumRate(BigDecimalUtil.divideFour(payNumSumRate.doubleValue(),1,true));

        if (payAmountSumRate == null){
            payAmountSumRate = new BigDecimal(0);
        }
        payOrderBill.setPayAmountSumRate(BigDecimalUtil.divideFour(payAmountSumRate.doubleValue(),1,true));

        if (arppu == null){
            arppu = new BigDecimal(0);
        }
        payOrderBill.setArppu(BigDecimalUtil.divide(arppu.doubleValue(),1,3));

        return payOrderBill;
    }
}

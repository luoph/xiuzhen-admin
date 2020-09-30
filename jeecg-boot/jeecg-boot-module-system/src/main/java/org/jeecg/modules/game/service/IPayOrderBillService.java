package org.jeecg.modules.game.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.game.entity.PayOrderBill;

import java.math.BigDecimal;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description pay_order
 * @date 2020-09-29
 */
public interface IPayOrderBillService extends IService<PayOrderBill> {

    /**
     * 通过订单支付时间和服务器id和游戏渠道查询流水总额
     * @param payTimeBegin  开始时间
     * @param payTimeEnd    结束时间
     * @param serverId      服务器id
     * @param channel       游戏渠道
     * @return
     */
    BigDecimal queryBillSumByDateRange(String payTimeBegin, String payTimeEnd, Integer serverId, String channel);
}

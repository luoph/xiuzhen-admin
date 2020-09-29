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

    BigDecimal queryBillSumByDateRange(String payTimeBegin, String payTimeEnd, Integer serverId, Integer channel);
}

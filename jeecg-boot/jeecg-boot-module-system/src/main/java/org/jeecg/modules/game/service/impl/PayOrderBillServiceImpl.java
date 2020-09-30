package org.jeecg.modules.game.service.impl;

import cn.youai.xiuzhen.utils.DateUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.game.entity.PayOrderBill;
import org.jeecg.modules.game.mapper.PayOrderBillMapper;
import org.jeecg.modules.game.service.IPayOrderBillService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description pay_order
 * @date 2020-09-29
 */
@Service
public class PayOrderBillServiceImpl extends ServiceImpl<PayOrderBillMapper, PayOrderBill> implements IPayOrderBillService {

    @Resource
    private PayOrderBillMapper payOrderBillMapper;


    @Override
    public BigDecimal queryBillSumByDateRange(String payTimeBegin, String payTimeEnd, Integer serverId, String channel) {
        Date payTimeBeginDate = DateUtils.parseDate(payTimeBegin);
        Date payTimeEndDate = DateUtils.parseDate(payTimeEnd);

        BigDecimal billSum = payOrderBillMapper.queryBillSumByDateRange(payTimeBeginDate, payTimeEndDate, serverId, channel);
        return billSum;
    }
}

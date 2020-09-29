package org.jeecg.modules.game.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.game.entity.PayOrderBill;
import org.jeecg.modules.game.mapper.PayOrderBillMapper;
import org.jeecg.modules.game.service.IPayOrderBillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
    public BigDecimal queryBillSumByDateRange(String payTimeBegin, String payTimeEnd, Integer serverId, Integer channel) {
        //String time = "2019-07-23";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//yyyy-MM-dd HH-mm-ss
        Date payTimeBeginDate = null;
        Date payTimeEndDate = null;
        try {
            payTimeBeginDate = simpleDateFormat.parse(payTimeBegin);
            payTimeEndDate = simpleDateFormat.parse(payTimeEnd);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        BigDecimal billSum = payOrderBillMapper.queryBillSumByDateRange(payTimeBeginDate, payTimeEndDate, serverId, channel);
        System.out.println(billSum);
        return billSum;
    }
}

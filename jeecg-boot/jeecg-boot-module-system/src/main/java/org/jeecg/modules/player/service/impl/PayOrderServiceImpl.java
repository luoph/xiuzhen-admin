package org.jeecg.modules.player.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.player.entity.PayOrder;
import org.jeecg.modules.player.mapper.PayOrderMapper;
import org.jeecg.modules.player.service.IPayOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.math.BigDecimal;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 充值订单
 * @date 2020-01-05
 */
@Service
public class PayOrderServiceImpl extends ServiceImpl<PayOrderMapper, PayOrder> implements IPayOrderService {

    @Resource
    private PayOrderMapper payOrderMapper;

    @Override
    public double sumPayAmount(String channel, int serverId, String date) {
        return payOrderMapper.getSumPayAmount(channel, serverId, date);
    }

    @Override
    public int countPayPlayer(String channel, int serverId, String date) {
        return payOrderMapper.getCountPayPlayer(channel, serverId, date);
    }
}

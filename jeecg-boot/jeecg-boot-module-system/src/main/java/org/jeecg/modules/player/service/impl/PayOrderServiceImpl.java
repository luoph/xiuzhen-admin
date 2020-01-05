package org.jeecg.modules.player.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.player.entity.PayOrder;
import org.jeecg.modules.player.mapper.PayOrderMapper;
import org.jeecg.modules.player.service.IPayOrderService;
import org.springframework.stereotype.Service;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 充值订单
 * @date 2020-01-05
 */
@Service
public class PayOrderServiceImpl extends ServiceImpl<PayOrderMapper, PayOrder> implements IPayOrderService {

}

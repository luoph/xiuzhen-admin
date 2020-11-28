package org.jeecg.modules.player.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.player.entity.GameOrder;
import org.jeecg.modules.player.mapper.GameOrderMapper;
import org.jeecg.modules.player.service.IPayOrderService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 充值订单
 * @date 2020-01-05
 */
@Service
public class PayOrderServiceImpl extends ServiceImpl<GameOrderMapper, GameOrder> implements IPayOrderService {

    @Resource
    private GameOrderMapper gameOrderMapper;

    @Override
    public double sumPayAmount(String channel, int serverId, String date) {
        return gameOrderMapper.getSumPayAmount(channel, serverId, date);
    }

    @Override
    public int countPayPlayer(String channel, int serverId, String date) {
        return gameOrderMapper.getCountPayPlayer(channel, serverId, date);
    }
}

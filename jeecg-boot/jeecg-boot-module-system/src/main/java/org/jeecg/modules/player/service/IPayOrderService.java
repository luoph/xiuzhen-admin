package org.jeecg.modules.player.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.player.entity.PayOrder;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 充值订单
 * @date 2020-01-05
 */
public interface IPayOrderService extends IService<PayOrder> {
    /**
     * 付费金额总和
     *
     * @param channel  渠道
     * @param serverId 服务器id
     * @param date     日期
     * @return 付费总金额
     */
    double sumPayAmount(String channel, int serverId, String date);

    /**
     * 付费角色数
     *
     * @param channel
     * @param serverId
     * @param date
     * @return
     */
    int countPayPlayer(String channel, int serverId, String date);
}

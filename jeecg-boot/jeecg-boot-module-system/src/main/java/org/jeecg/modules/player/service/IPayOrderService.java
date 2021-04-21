package org.jeecg.modules.player.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.game.entity.GameStatOrder;
import org.jeecg.modules.player.entity.GameOrder;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 充值订单
 * @date 2020-01-05
 */
public interface IPayOrderService extends IService<GameOrder> {
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

    /**
     * 统计日期内，付费玩家数，付费金额
     * @param serverId 服务器id
     * @param dateList 统计日期列表（有序）
     * @param type 统计类型 1-按天 2-按月 3-按年
     * @return Map<String, GameStatOrder> 日期-
     */
    List<GameStatOrder> statOrderByDates(int serverId, List<Date> dateList, int type);
}

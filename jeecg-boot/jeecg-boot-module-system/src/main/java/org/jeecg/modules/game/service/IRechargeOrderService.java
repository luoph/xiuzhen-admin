package org.jeecg.modules.game.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.game.entity.GameChalcedonyOrder;
import org.jeecg.modules.game.entity.RechargeOrder;

import java.util.List;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 今日礼包
 * @date 2020-10-13
 */
public interface IRechargeOrderService extends IService<RechargeOrder> {

    /**
     * 查询充值礼包
     *
     * @param rangeDateBegin
     * @param rangeDateEnd
     * @param days
     * @param serverId
     * @param channel
     * @param goodsType
     * @return
     */
    List<RechargeOrder> queryGiftList(String rangeDateBegin, String rangeDateEnd, int days, Integer serverId, String channel, int goodsType);

    /**
     * 查询消耗礼包
     *
     * @param rangeDateBegin
     * @param rangeDateEnd
     * @param days
     * @param serverId
     * @param channel
     * @param goodsType
     * @return
     */
    List<GameChalcedonyOrder> queryExpendGiftList(String rangeDateBegin, String rangeDateEnd, int days, Integer serverId, String channel, String goodsType);

}

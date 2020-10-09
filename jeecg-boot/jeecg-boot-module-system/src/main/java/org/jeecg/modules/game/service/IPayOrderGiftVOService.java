package org.jeecg.modules.game.service;

import org.jeecg.modules.game.entity.PayOrderGiftVO;

import java.util.List;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 直充道具数据统计
 * @date 2020-09-29
 */
public interface IPayOrderGiftVOService {

    /**
     * 通过支付时间和服务器id和游戏渠道查询直充道具的数据
     *
     * @param payTimeBegin  开始时间
     * @param payTimeEnd    结束时间
     * @param serverId      服务器id
     * @param channel       游戏渠道
     * @return
     */
    List<PayOrderGiftVO> queryGiftByDateRange(String payTimeBegin, String payTimeEnd, Integer serverId, String channel);

}

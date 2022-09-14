package cn.youai.xiuzhen.game.service;

import cn.youai.xiuzhen.game.entity.GameChalcedonyOrder;
import cn.youai.xiuzhen.game.entity.RechargeOrder;
import com.baomidou.mybatisplus.extension.service.IService;

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

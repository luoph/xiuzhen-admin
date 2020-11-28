package org.jeecg.modules.player.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.player.entity.GameOrder;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 充值订单
 * @date 2020-01-05
 */
public interface GameOrderMapper extends BaseMapper<GameOrder> {
    /**
     * 计算当天支付金额
     *
     * @param channel
     * @param serverId
     * @param payDate
     * @return
     */
    double getSumPayAmount(@Param("channel") String channel, @Param("serverId") int serverId, @Param("payDate") String payDate);

    int getCountPayPlayer(@Param("channel") String channel, @Param("serverId") int serverId, @Param("payDate") String payDate);
}

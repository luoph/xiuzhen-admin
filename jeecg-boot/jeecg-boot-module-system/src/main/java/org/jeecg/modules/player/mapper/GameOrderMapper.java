package org.jeecg.modules.player.mapper;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.player.entity.GameOrder;

import java.util.List;

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

    /**
     * 统计日期内，付费玩家数，付费金额
     *
     * @param serverId 服务器id
     * @param dates    日期范围<String[开始日期，结束日期]>
     * @return GameOrder id对应参数date集合下标
     */
    List<GameOrder> getStatPlayerNumAmountByDates(@Param("serverId") int serverId, @Param("dateList") List<JSONObject> dates);
}

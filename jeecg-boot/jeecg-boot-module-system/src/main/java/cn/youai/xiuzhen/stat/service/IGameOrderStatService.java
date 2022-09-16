package cn.youai.xiuzhen.stat.service;

import cn.youai.xiuzhen.game.entity.GameOrder;
import cn.youai.xiuzhen.stat.entity.GameStatOrder;
import com.baomidou.mybatisplus.extension.service.IService;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 充值订单
 * @date 2020-01-05
 */
public interface IGameOrderStatService extends IService<GameOrder> {
    /**
     * 付费金额总和
     */
    BigDecimal serverPayAmount(int serverId, Date date);

    BigDecimal channelPayAmount(String channel, Date date);

    /**
     * 付费角色数
     */
    int serverPayPlayerNum(int serverId, Date date);

    /**
     * 付费角色数
     */
    int channelPayPlayerNum(String channel, Date date);

    /**
     * 统计日期内，付费玩家数，付费金额
     *
     * @param serverId 服务器id
     * @param dateList 统计日期列表（有序）
     * @param type     统计类型 1-按天 2-按月 3-按年
     * @return Map<String, GameStatOrder> 日期-
     */
    List<GameStatOrder> statOrderByDates(int serverId, List<Date> dateList, int type);
}

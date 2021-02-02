package org.jeecg.modules.game.constant;

import lombok.Getter;
import org.jeecg.modules.game.entity.*;

/**
 * 节日活动类型
 * 1.登录礼包, 2.累计充值, 3.兑换, 4.节日任务, 5.buff-修为加成, 6.buff-灵气加成
 *
 * @author luopeihuan
 */
@Getter
@SuppressWarnings("rawtypes")
public enum CampaignFestivalType {
    /**
     * 活动状态
     */
    LOGIN("登录礼包", 1, GameCampaignTypeLogin.class, GameCampaignTypeLoginVO.class),
    RECHARGE("累计充值", 2, GameCampaignTypeRecharge.class, GameCampaignTypeRechargeVO.class),
    EXCHANGE("兑换", 3, GameCampaignTypeExchange.class, GameCampaignTypeExchangeVO.class),
    TASK("节日任务", 4, GameCampaignTypeTask.class, GameCampaignTypeTaskVO.class),
    BUFF_PRACTICE("修为加成", 5, GameCampaignTypeBuff.class, GameCampaignTypeBuffVO.class),
    BUFF_ANIMA("灵气加成", 6, GameCampaignTypeBuff.class, GameCampaignTypeBuffVO.class),
    FALL("节日掉落", 7, GameCampaignTypeFall.class, GameCampaignTypeBuffVO.class),
    //    FALL_REWARD("节日掉落奖励", 7, GameCampaignTypeFallReward.class, GameCampaignTypeBuffVO.class),
    FIREWORK("烟火", 8, GameCampaignTypeFirework.class, GameCampaignTypeFireworkVO.class),
    REDUCE_RANK("消耗排行", 9, GameCampaignTypeReduce.class, GameCampaignTypeReduceVO.class),
    ;

    private final String name;
    private final int value;
    private final Class tableClass;
    private final Class excelClass;

    CampaignFestivalType(String name, int value, Class tableClass, Class excelClass) {
        this.name = name;
        this.value = value;
        this.tableClass = tableClass;
        this.excelClass = excelClass;
    }

    public static CampaignFestivalType valueOf(int type) {
        for (CampaignFestivalType e : CampaignFestivalType.values()) {
            if (e.getValue() == type) {
                return e;
            }
        }
        return null;
    }
}

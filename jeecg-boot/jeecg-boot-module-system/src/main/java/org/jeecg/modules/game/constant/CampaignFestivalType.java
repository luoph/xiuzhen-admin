package org.jeecg.modules.game.constant;

import com.alibaba.fastjson.JSON;
import lombok.Getter;
import org.jeecg.modules.game.entity.*;

import java.util.List;

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
    LOGIN("登录礼包", 1, GameCampaignTypeLogin.class),
    RECHARGE("累计充值", 2, GameCampaignTypeRecharge.class),
    EXCHANGE("兑换", 3, GameCampaignTypeExchange.class),
    TASK("节日任务", 4, GameCampaignTypeTask.class),
    BUFF_PRACTICE("修为加成", 5, GameCampaignTypeBuff.class),
    BUFF_ANIMA("灵气加成", 6, GameCampaignTypeBuff.class),
    ;

    private final String name;
    private final int value;
    private final Class tableClass;

    CampaignFestivalType(String name, int value, Class tableClass) {
        this.name = name;
        this.value = value;
        this.tableClass = tableClass;
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

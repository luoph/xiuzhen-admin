package org.jeecg.modules.game.constant;

import lombok.Getter;
import org.jeecg.modules.game.entity.*;

/**
 * 开服活动项类型(1.开服排行，2.开服礼包，3.单笔充值，4.寻宝，5.道具消耗)
 *
 * @author luopeihuan
 */
@Getter
@SuppressWarnings("rawtypes")
public enum OpenServiceType {

    /**
     * 活动状态
     */
    RANK("开服排行", 1, OpenServiceCampaignRankDetail.class),
    GIFT("开服礼包", 2, OpenServiceCampaignGiftDetail.class),
    RECHARGE("单笔充值", 3, OpenServiceCampaignSingleGiftDetail.class),
    LOTTERY("寻宝", 4, OpenServiceCampaignLotteryDetail.class),
    CONSUME("道具消耗", 5, OpenServiceCampaignConsumeDetail.class),
    ;

    private final String name;
    private final int value;
    private final Class tableClass;

    OpenServiceType(String name, int value, Class tableClass) {
        this.name = name;
        this.value = value;
        this.tableClass = tableClass;
    }

    public static OpenServiceType valueOf(int type) {
        for (OpenServiceType e : OpenServiceType.values()) {
            if (e.getValue() == type) {
                return e;
            }
        }
        return null;
    }
}

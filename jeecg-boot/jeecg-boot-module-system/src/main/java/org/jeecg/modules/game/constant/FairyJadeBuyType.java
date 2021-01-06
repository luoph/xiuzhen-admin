package org.jeecg.modules.game.constant;

import lombok.Getter;

/**
 * @ClassName FairyJadeBuyType
 * @Description 仙玉充值类型购买
 * @Author buliangliang
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2020/12/24 3:19 下午
 */
@Getter
public enum FairyJadeBuyType {
    /**
     * 购买类型
     */
    PRACTICE_FUND(1, "成长基金"),
    IMMORTAL(2, "仙职位"),
    DAILY_GIFT(3, "特惠礼包"),
    SEVEN_DAY_GIFT(4, "七天礼包"),
    ZERO_BUY(5, "0元购买"),
    OPEN_SERVICE_GIFT(6, "开服礼包");

    private final Integer type;
    private final String desc;

    FairyJadeBuyType(Integer type, String desc) {
        this.type = type;
        this.desc = desc;
    }
}
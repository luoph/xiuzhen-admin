package org.jeecg.modules.game.constant;

import lombok.Getter;

/**
 * itemId枚举
 */
@Getter
public enum ItemId {
    /**
     * 购买类型
     */
    PRACTICE_FUND(1010, "仙石"),
    IMMORTAL(1002, "玉髓"),
    DAILY_GIFT(1001, "灵石");

    private final Integer itemId;
    private final String name;

    ItemId(Integer type, String name) {
        this.itemId = type;
        this.name = name;
    }
}
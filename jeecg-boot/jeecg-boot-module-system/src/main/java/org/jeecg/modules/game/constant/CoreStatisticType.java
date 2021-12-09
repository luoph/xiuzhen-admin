package org.jeecg.modules.game.constant;

import lombok.Getter;

/**
 * 统计类型
 */
@Getter
public enum CoreStatisticType {
    /**
     * 统计类型
     */
    DAILY("日常统计", 1),
    REMAIN("留存统计", 2),
    LTV("ltv统计", 3),
    ;

    private final String name;
    private final int value;

    CoreStatisticType(String name, int value) {
        this.name = name;
        this.value = value;
    }

    public static CoreStatisticType valueOf(int type) {
        for (CoreStatisticType e : CoreStatisticType.values()) {
            if (e.getValue() == type) {
                return e;
            }
        }
        return null;
    }
}

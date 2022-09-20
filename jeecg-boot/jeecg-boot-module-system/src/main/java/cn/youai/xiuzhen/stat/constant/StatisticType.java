package cn.youai.xiuzhen.stat.constant;

import lombok.Getter;

/**
 * 统计类型
 */
@Getter
public enum StatisticType {
    /**
     * 统计类型
     */
    DAILY("日常统计", 1),
    REMAIN("留存统计", 2),
    LTV("LTV统计", 3),
    REMAIN_DETAIL("留存详细统计", 4),
    ;

    private final String name;
    private final int value;

    public static final String DEFAULT_CHANNEL = "default";
    public static final int DEFAULT_SERVER_ID = 0;

    StatisticType(String name, int value) {
        this.name = name;
        this.value = value;
    }

    public static StatisticType valueOf(int type) {
        for (StatisticType e : StatisticType.values()) {
            if (e.getValue() == type) {
                return e;
            }
        }
        return null;
    }
}

package cn.youai.xiuzhen.game.constant;

import lombok.Getter;

/**
 * 活动时间类型
 */
@Getter
public enum TimeType {
    /**
     * 统计类型
     */
    TIME_RANGE("具体时间范围", 1),
    OPEN_DAY("开服第N天", 2),
    ;

    private final String name;
    private final int type;

    TimeType(String name, int type) {
        this.name = name;
        this.type = type;
    }

}

package cn.youai.xiuzhen.game.constant;

import lombok.Getter;

/**
 * 日期范围枚举
 */
@Getter
public enum StatDayType {
    /**
     * 统计类型
     */
    DEFAULT("自定义", 0),
    D7("7天", 7),
    D15("15天", 15),
    M1("30天", 30),
    M2("60天", 60),
    ;

    private final String name;
    private final int value;

    StatDayType(String name, int value) {
        this.name = name;
        this.value = value;
    }

    public static StatDayType valueOf(int type) {
        for (StatDayType e : StatDayType.values()) {
            if (e.getValue() == type) {
                return e;
            }
        }
        return DEFAULT;
    }
}

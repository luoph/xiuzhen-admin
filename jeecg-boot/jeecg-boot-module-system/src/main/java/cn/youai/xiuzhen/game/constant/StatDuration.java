package cn.youai.xiuzhen.game.constant;

import lombok.Getter;

/**
 * 统计周期
 */
@Getter
public enum StatDuration {
    /**
     * 统计周期
     */
    DAY("天", 1, "yyyy-MM-dd", "yyyy-MM-dd"),
    WEEK("周", 2, "yyyy-ww", "yyyy年ww周"),
    MONTH("月", 3, "yyyy-MM", "yyyy年MM月"),
    YEAR("年", 4, "yyyy", "yyyy年"),
    ;

    private final String name;

    private final String timeFormat;

    private final String viewFormat;
    private final int value;

    StatDuration(String name, int value, String timeFormat, String viewFormat) {
        this.name = name;
        this.value = value;
        this.timeFormat = timeFormat;
        this.viewFormat = viewFormat;
    }

    public static StatDuration valueOf(int type) {
        for (StatDuration e : StatDuration.values()) {
            if (e.getValue() == type) {
                return e;
            }
        }
        return DAY;
    }
}

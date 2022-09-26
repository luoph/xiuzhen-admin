package cn.youai.xiuzhen.game.constant;

import lombok.Getter;

import java.math.BigDecimal;

/**
 * 充值档位
 */
@Getter
public enum RechargeGrade {
    /**
     * 统计周期
     */
    L1("<=6", 1, 0, 6),
    L2("7~29", 2, 7, 29),
    L3("30~67", 3, 30, 67),
    L4("68~97", 4, 68, 97),
    L5("98~197", 5, 98, 197),
    L6("198~327", 6, 198, 327),
    L7("328~647", 7, 328, 647),
    L8(">=648", 8, 648, Integer.MAX_VALUE),
    ;

    private final String name;

    private final int type;

    private final BigDecimal min;

    private final BigDecimal max;

    RechargeGrade(String name, int type, int min, int max) {
        this.name = name;
        this.type = type;
        this.min = BigDecimal.valueOf(min);
        this.max = BigDecimal.valueOf(max);
    }

    public static RechargeGrade valueOf(BigDecimal value) {
        for (RechargeGrade e : RechargeGrade.values()) {
            if (e.getMin().compareTo(value) <= 0 && e.getMax().compareTo(value) >= 0) {
                return e;
            }
        }
        return null;
    }
}

package cn.youai.xiuzhen.constant;

import cn.youai.xiuzhen.game.entity.GameStatLtvDetail;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.function.BiConsumer;

/**
 * LTV详情映射
 *
 * @author luopeihuan
 * @date 2021/12/22
 */
@Getter
public enum LtvDetailField {

    D1(1, GameStatLtvDetail::setD1),
    D2(2, GameStatLtvDetail::setD2),
    D3(3, GameStatLtvDetail::setD3),
    D4(4, GameStatLtvDetail::setD4),
    D5(5, GameStatLtvDetail::setD5),
    D6(6, GameStatLtvDetail::setD6),
    D7(7, GameStatLtvDetail::setD7),
    D8(8, GameStatLtvDetail::setD8),
    D9(9, GameStatLtvDetail::setD9),
    D10(10, GameStatLtvDetail::setD10),
    D11(11, GameStatLtvDetail::setD11),
    D12(12, GameStatLtvDetail::setD12),
    D13(13, GameStatLtvDetail::setD13),
    D14(14, GameStatLtvDetail::setD14),
    D15(15, GameStatLtvDetail::setD15),
    D16(16, GameStatLtvDetail::setD16),
    D17(17, GameStatLtvDetail::setD17),
    D18(18, GameStatLtvDetail::setD18),
    D19(19, GameStatLtvDetail::setD19),
    D20(20, GameStatLtvDetail::setD20),
    D21(21, GameStatLtvDetail::setD21),
    D22(22, GameStatLtvDetail::setD22),
    D23(23, GameStatLtvDetail::setD23),
    D24(24, GameStatLtvDetail::setD24),
    D25(25, GameStatLtvDetail::setD25),
    D26(26, GameStatLtvDetail::setD26),
    D27(27, GameStatLtvDetail::setD27),
    D28(28, GameStatLtvDetail::setD28),
    D29(29, GameStatLtvDetail::setD29),
    D30(30, GameStatLtvDetail::setD30),
    D45(45, GameStatLtvDetail::setD45),
    D60(60, GameStatLtvDetail::setD60),
    D90(90, GameStatLtvDetail::setD90),
    D120(120, GameStatLtvDetail::setD120),
    D150(150, GameStatLtvDetail::setD150),
    D180(180, GameStatLtvDetail::setD180),
    D270(270, GameStatLtvDetail::setD270),
    D360(360, GameStatLtvDetail::setD360),
    ;

    private final int days;
    private final BiConsumer<GameStatLtvDetail, BigDecimal> function;

    LtvDetailField(int days, BiConsumer<GameStatLtvDetail, BigDecimal> function) {
        this.days = days;
        this.function = function;
    }

    public static LtvDetailField valueOf(int days) {
        for (LtvDetailField value : LtvDetailField.values()) {
            if (value.getDays() == days) {
                return value;
            }
        }
        return null;
    }
}

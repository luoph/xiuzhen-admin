package cn.youai.xiuzhen.stat.constant;

import cn.youai.xiuzhen.stat.entity.GameStatRemainDetail;
import lombok.Getter;

import java.util.function.ObjIntConsumer;

/**
 * 留存详情映射
 *
 * @author luopeihuan
 * @date 2021/12/22
 */
@Getter
public enum RemainDetailField {

    D2(2 - 1, GameStatRemainDetail::setD2),
    D3(3 - 1, GameStatRemainDetail::setD3),
    D4(4 - 1, GameStatRemainDetail::setD4),
    D5(5 - 1, GameStatRemainDetail::setD5),
    D6(6 - 1, GameStatRemainDetail::setD6),
    D7(7 - 1, GameStatRemainDetail::setD7),
    D8(8 - 1, GameStatRemainDetail::setD8),
    D9(9 - 1, GameStatRemainDetail::setD9),
    D10(10 - 1, GameStatRemainDetail::setD10),
    D11(11 - 1, GameStatRemainDetail::setD11),
    D12(12 - 1, GameStatRemainDetail::setD12),
    D13(13 - 1, GameStatRemainDetail::setD13),
    D14(14 - 1, GameStatRemainDetail::setD14),
    D15(15 - 1, GameStatRemainDetail::setD15),
    D16(16 - 1, GameStatRemainDetail::setD16),
    D17(17 - 1, GameStatRemainDetail::setD17),
    D18(18 - 1, GameStatRemainDetail::setD18),
    D19(19 - 1, GameStatRemainDetail::setD19),
    D20(20 - 1, GameStatRemainDetail::setD20),
    D21(21 - 1, GameStatRemainDetail::setD21),
    D22(22 - 1, GameStatRemainDetail::setD22),
    D23(23 - 1, GameStatRemainDetail::setD23),
    D24(24 - 1, GameStatRemainDetail::setD24),
    D25(25 - 1, GameStatRemainDetail::setD25),
    D26(26 - 1, GameStatRemainDetail::setD26),
    D27(27 - 1, GameStatRemainDetail::setD27),
    D28(28 - 1, GameStatRemainDetail::setD28),
    D29(29 - 1, GameStatRemainDetail::setD29),
    D30(30 - 1, GameStatRemainDetail::setD30),
    D45(45 - 1, GameStatRemainDetail::setD45),
    D60(60 - 1, GameStatRemainDetail::setD60),
    D90(90 - 1, GameStatRemainDetail::setD90),
    D120(120 - 1, GameStatRemainDetail::setD120),
    D150(150 - 1, GameStatRemainDetail::setD150),
    D180(180 - 1, GameStatRemainDetail::setD180),
    D270(270 - 1, GameStatRemainDetail::setD270),
    D360(360 - 1, GameStatRemainDetail::setD360),
    ;

    private final int days;
    private final ObjIntConsumer<GameStatRemainDetail> function;

    RemainDetailField(int days, ObjIntConsumer<GameStatRemainDetail> function) {
        this.days = days;
        this.function = function;
    }

    public static RemainDetailField valueOf(int days) {
        for (RemainDetailField value : RemainDetailField.values()) {
            if (value.getDays() == days) {
                return value;
            }
        }
        return null;
    }
}

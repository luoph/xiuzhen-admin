package cn.youai.xiuzhen.constant;

import lombok.Getter;
import org.jeecg.modules.game.entity.GameStatRemainDetail;

import java.util.function.ObjIntConsumer;

/**
 * 留存详情映射
 *
 * @author luopeihuan
 * @date 2021/12/22
 */
@Getter
public enum RemainDetailField {

    D2(2, GameStatRemainDetail::setD2),
    D3(3, GameStatRemainDetail::setD3),
    D4(4, GameStatRemainDetail::setD4),
    D5(5, GameStatRemainDetail::setD5),
    D6(6, GameStatRemainDetail::setD6),
    D7(7, GameStatRemainDetail::setD7),
    D8(8, GameStatRemainDetail::setD8),
    D9(9, GameStatRemainDetail::setD9),
    D10(10, GameStatRemainDetail::setD10),
    D11(11, GameStatRemainDetail::setD11),
    D12(12, GameStatRemainDetail::setD12),
    D13(13, GameStatRemainDetail::setD13),
    D14(14, GameStatRemainDetail::setD14),
    D15(15, GameStatRemainDetail::setD15),
    D16(16, GameStatRemainDetail::setD16),
    D17(17, GameStatRemainDetail::setD17),
    D18(18, GameStatRemainDetail::setD18),
    D19(19, GameStatRemainDetail::setD19),
    D20(20, GameStatRemainDetail::setD20),
    D21(21, GameStatRemainDetail::setD21),
    D22(22, GameStatRemainDetail::setD22),
    D23(23, GameStatRemainDetail::setD23),
    D24(24, GameStatRemainDetail::setD24),
    D25(25, GameStatRemainDetail::setD25),
    D26(26, GameStatRemainDetail::setD26),
    D27(27, GameStatRemainDetail::setD27),
    D28(28, GameStatRemainDetail::setD28),
    D29(29, GameStatRemainDetail::setD29),
    D30(30, GameStatRemainDetail::setD30),
    D45(45, GameStatRemainDetail::setD45),
    D60(60, GameStatRemainDetail::setD60),
    D90(90, GameStatRemainDetail::setD90),
    D120(120, GameStatRemainDetail::setD120),
    D150(150, GameStatRemainDetail::setD150),
    D180(180, GameStatRemainDetail::setD180),
    D270(270, GameStatRemainDetail::setD270),
    D360(360, GameStatRemainDetail::setD360),
    ;

    private final int days;
    private final ObjIntConsumer<GameStatRemainDetail> function;

    RemainDetailField(int days, ObjIntConsumer<GameStatRemainDetail> function) {
        this.days = days;
        this.function = function;
    }

    public static RemainDetailField valueOf(int days) {
        for (RemainDetailField value : RemainDetailField.values()) {
            if (value.getDays() <= days) {
                return value;
            }
        }
        return null;
    }
}

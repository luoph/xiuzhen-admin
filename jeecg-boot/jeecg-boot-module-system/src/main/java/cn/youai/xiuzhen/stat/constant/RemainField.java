package cn.youai.xiuzhen.stat.constant;

import cn.youai.xiuzhen.stat.entity.GameStatRemain;
import lombok.Getter;

import java.util.function.ObjIntConsumer;

/**
 * 留存详情映射
 *
 * @author luopeihuan
 * @date 2021/12/22
 */
@Getter
public enum RemainField {

    D2(2, GameStatRemain::setD2), D3(3, GameStatRemain::setD3), D4(4, GameStatRemain::setD4), D5(5, GameStatRemain::setD5), D6(6, GameStatRemain::setD6), D7(7, GameStatRemain::setD7), D15(15, GameStatRemain::setD15), D30(30, GameStatRemain::setD30), D60(60, GameStatRemain::setD60), D90(90, GameStatRemain::setD90), D120(120, GameStatRemain::setD120), D180(180, GameStatRemain::setD180), D360(360, GameStatRemain::setD360),
    ;

    private final int days;
    private final ObjIntConsumer<GameStatRemain> function;

    RemainField(int days, ObjIntConsumer<GameStatRemain> function) {
        this.days = days;
        this.function = function;
    }

    public static RemainField valueOf(int days) {
        for (RemainField value : RemainField.values()) {
            if (value.getDays() == days) {
                return value;
            }
        }
        return null;
    }

    public static RemainField match(int days) {
        if (days == 2) {
            return D2;
        } else if (days == 3) {
            return D3;
        } else if (days == 4) {
            return D4;
        } else if (days == 5) {
            return D5;
        } else if (days == 6) {
            return D6;
        } else if (days == 7) {
            return D7;
        } else if (days <= 15) {
            return D15;
        } else if (days <= 30) {
            return D30;
        } else if (days <= 60) {
            return D60;
        } else if (days <= 90) {
            return D90;
        } else if (days <= 120) {
            return D120;
        } else if (days <= 180) {
            return D180;
        } else if (days <= 360) {
            return D360;
        }
        return null;
    }
}

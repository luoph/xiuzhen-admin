package cn.youai.xiuzhen.entity.common;

import lombok.Getter;

/**
 * 充值类型
 * 对应充值表的 recharge_goods.goods_type
 *
 * @author luopeihuan
 * @since 2020-07-30
 */
@Getter
public enum RechargeGoodsType {

    /**
     * 充值分类
     */
    COMMON(0, "普通类型"),
    OFFICIAL_JOB(1, "仙职"),
    MONTH_CARD(2, "月卡"),
    GIFT_BAG(3, "特惠礼包"),
    FIRST_CHARGE(4, "首充");

    private final String name;
    private final int type;

    RechargeGoodsType(int type, String name) {
        this.name = name;
        this.type = type;
    }

    public static RechargeGoodsType valueOf(int type) {
        for (RechargeGoodsType e : RechargeGoodsType.values()) {
            if (e.getType() == type) {
                return e;
            }
        }
        return COMMON;
    }
}

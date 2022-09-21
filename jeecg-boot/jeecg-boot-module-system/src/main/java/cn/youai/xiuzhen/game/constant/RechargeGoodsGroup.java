package cn.youai.xiuzhen.game.constant;

import lombok.Getter;

/**
 * 充值道具分组
 *
 * @author luopeihuan
 * @since 2022/09/22
 */
@Getter
public enum RechargeGoodsGroup {
    /**
     * 充值分类
     */
    ALL(0, "全部"),
    DIRECT(1, "直充"),
    PACKAGE(2, "礼包"),
    ;

    private final String name;
    private final int type;

    RechargeGoodsGroup(int type, String name) {
        this.name = name;
        this.type = type;
    }

    public static RechargeGoodsGroup valueOf(int type) {
        for (RechargeGoodsGroup e : RechargeGoodsGroup.values()) {
            if (e.getType() == type) {
                return e;
            }
        }
        return ALL;
    }
}

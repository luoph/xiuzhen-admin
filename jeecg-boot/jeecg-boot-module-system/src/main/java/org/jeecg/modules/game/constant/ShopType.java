package org.jeecg.modules.game.constant;

import cn.youai.xiuzhen.entity.pojo.ItemReduce;
import lombok.Getter;

/**
 * 商店类型
 */
@Getter
public enum ShopType {
    /**
     * 商店类型
     */
    DISCOUNT_SHOP(1001, "优惠坊市"),
    FASHION_SHOP_HEAD(100201, "时装坊市--头像"),
    FASHION_SHOP_CHAT(100202, "时装坊市--聊天"),
    FASHION_SHOP_NAMECARD(100203, "时装坊市--名帖"),
    HONOR_SHOP(1003, "荣誉坊市"),
    IMMORTAL_WAY(3001, "成仙路"),
    DEFAULT(0,"不存在的类型，请检查！");

    private final Integer shopId;
    private final String name;

    ShopType(Integer type, String name) {
        this.shopId = type;
        this.name = name;
    }

    public static ShopType valueOf(int type) {
        for (ShopType itemReduce : ShopType.values()) {
            if (type == itemReduce.getShopId()) {
                return itemReduce;
            }
        }
        return DEFAULT;
    }
}
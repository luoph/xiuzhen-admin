/*
 * create by mybatis-plus-generator  https://github.com/xiweile
 */
package cn.youai.xiuzhen.game.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 节日活动-自选特惠-物品部分
 * </p>
 *
 * @author chuantian
 * @since 2021-09-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("game_campaign_type_select_discount_item")
public class GameCampaignTypeSelectDiscountItem extends GameCampaignTypeBase {

    private static final long serialVersionUID = 1L;

    public GameCampaignTypeSelectDiscountItem() {
    }

    public GameCampaignTypeSelectDiscountItem(GameCampaignTypeSelectDiscountItem other) {
        this.showOrder = other.showOrder;
        this.itemDesc = other.itemDesc;
        this.limitNum = other.limitNum;
        this.goodsId = other.goodsId;
        this.chooseItems = other.chooseItems;
        this.freeItems = other.freeItems;
        this.free = other.free;
    }

    /**
     * 位置序号
     */
    private Integer showOrder;

    /**
     * 商品描述
     */
    private String itemDesc;

    /**
     * 限购的话次数多少
     */
    private Integer limitNum;

    /**
     * 商品id
     */
    private Integer goodsId;

    /**
     * 可选物品
     */
    private String chooseItems;

    /**
     * 免费物品
     */
    private String freeItems;

    /**
     * 本组商品是否免费领取
     */
    private Boolean free;


}

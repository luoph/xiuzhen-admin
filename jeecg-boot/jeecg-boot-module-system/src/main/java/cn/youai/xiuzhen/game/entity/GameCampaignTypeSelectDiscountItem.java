/*
 * create by mybatis-plus-generator  https://github.com/xiweile
 */
package cn.youai.xiuzhen.game.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * <p>
 * 节日活动-自选特惠-物品部分
 * </p>
 *
 * @author chuantian
 * @since 2021-09-08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("game_campaign_type_select_discount_item")
public class GameCampaignTypeSelectDiscountItem extends GameCampaignTypeBaseDetail {

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
        this.setMinLevel(other.getMinLevel());
        this.setMaxLevel(other.getMaxLevel());
    }

    /**
     * 位置序号
     */
    @Excel(name = "位置序号", width = 15)
    private Integer showOrder;

    /**
     * 商品描述
     */
    @Excel(name = "商品描述", width = 15)
    private String itemDesc;

    /**
     * 限购的话次数多少
     */
    @Excel(name = "限购次数", width = 15)
    private Integer limitNum;

    /**
     * 商品id
     */
    @Excel(name = "商品id", width = 15)
    private Integer goodsId;

    /**
     * 可选物品
     */
    @Excel(name = "可选物品", width = 15)
    private String chooseItems;

    /**
     * 免费物品
     */
    @Excel(name = "免费物品", width = 15)
    private String freeItems;

    /**
     * 本组商品是否免费领取
     */
    @Excel(name = "是否免费", width = 15)
    private Boolean free;
}

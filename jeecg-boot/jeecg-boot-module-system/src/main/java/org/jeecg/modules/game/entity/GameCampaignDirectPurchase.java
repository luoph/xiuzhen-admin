package org.jeecg.modules.game.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 直购礼包
 * @date 2021-04-15
 */
@Data
@TableName("game_campaign_direct_purchase")
@EqualsAndHashCode(callSuper = true)
public class GameCampaignDirectPurchase extends GameCampaignTypeBase {

    private static final long serialVersionUID = 1L;

    public GameCampaignDirectPurchase() {
    }

    public GameCampaignDirectPurchase(GameCampaignDirectPurchase other) {
        this.limitNum = other.getLimitNum();
        this.name = other.getName();
        this.type = other.getType();
        this.sort = other.getSort();
        this.reward = other.getReward();
        this.goodsId = other.getGoodsId();
    }

    /**
     * 已购数量
     */
    @Excel(name = "商品Id", width = 15)
    @ExcelProperty("商品Id")
    private java.lang.Long goodsId;

    /**
     * 已购数量
     */
    @Excel(name = "限购数量", width = 15)
    @ExcelProperty("限购数量")
    private java.lang.Integer limitNum;

    /**
     * 礼包名
     */
    @Excel(name = "礼包名", width = 15)
    @ExcelProperty("礼包名")
    private java.lang.String name;

    /**
     * 礼包组类型
     */
    @Excel(name = "礼包组类型", width = 15)
    @ExcelProperty("礼包组类型")
    private java.lang.Integer type;

    /**
     * 组排序
     */
    @Excel(name = "组排序", width = 15)
    @ExcelProperty("组排序")
    private java.lang.Integer sort;

    /**
     * 奖励列表
     */
    @Excel(name = "奖励列表", width = 15)
    @ExcelProperty("奖励列表")
    private java.lang.String reward;
}

package org.jeecg.modules.game.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 砸蛋礼包
 * @date 2021-03-10
 */
@Data
@TableName("game_campaign_type_throwing_eggs_gift")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class GameCampaignTypeThrowingEggsGift extends GameCampaignTypeBase {

    private static final long serialVersionUID = 1L;


    public GameCampaignTypeThrowingEggsGift() {
    }

    public GameCampaignTypeThrowingEggsGift(GameCampaignTypeThrowingEggsGift other) {
        this.costItemId = other.getCostItemId();
        this.costNum = other.getCostNum();
        this.stack = other.getStack();
        this.amount = other.getAmount();
        this.discount = other.getDiscount();
        this.limitCondition = other.getLimitCondition();
        this.reward = other.getReward();
        this.giftName = other.getGiftName();
    }

    /**
     * 消耗道具
     */
    @Excel(name = "消耗道具", width = 15)
    private java.lang.Integer costItemId;

    /**
     * 消耗道具数量
     */
    @Excel(name = "消耗道具数量", width = 15)
    private java.lang.Integer costNum;

    /**
     * 库存
     */
    @Excel(name = "库存", width = 15)
    private java.lang.Integer stack;

    /**
     * 原价
     */
    @Excel(name = "原价", width = 15)
    private java.lang.Integer amount;

    /**
     * 折扣
     */
    @Excel(name = "折扣", width = 15)
    private java.lang.Integer discount;

    /**
     * 限购条件
     */
    @Excel(name = "限购条件", width = 15)
    private java.lang.String limitCondition;

    /**
     * 显示奖励内容
     */
    @Excel(name = "奖励内容", width = 15)
    private java.lang.String reward;


    @Excel(name = "礼包名", width = 15)
    private java.lang.String giftName;
}

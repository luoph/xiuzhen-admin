package org.jeecg.modules.game.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 兑换活动
 * @date 2020-10-15
 */
@Data
@TableName("game_campaign_type_exchange")
@EqualsAndHashCode(callSuper = true)
public class GameCampaignTypeExchange extends GameCampaignTypeBase {

    private static final long serialVersionUID = 1L;


    public GameCampaignTypeExchange() {

    }

    public GameCampaignTypeExchange(GameCampaignTypeExchange other) {
        this.exchangeId = other.getExchangeId();
        this.itemName = other.getItemName();
        this.maxExchangeNum = other.getMaxExchangeNum();
        this.reward = other.getReward();
        this.consume = other.getConsume();
        this.minLevel = other.getMinLevel();
        this.maxLevel = other.getMaxLevel();
    }

    /**
     * 兑换id
     */
    @ExcelProperty("兑换id")
    @Excel(name = "兑换id", width = 15)
    private Integer exchangeId;

    /**
     * 道具名称
     */
    @ExcelProperty("道具名称")
    @Excel(name = "道具名称", width = 15)
    private java.lang.String itemName;

    /**
     * 最大兑换数量
     */
    @ExcelProperty("最大兑换数量")
    @Excel(name = "最大兑换数量", width = 15)
    private Integer maxExchangeNum;

    /**
     * 奖励列表
     */
    @ExcelProperty("奖励列表")
    @Excel(name = "奖励列表", width = 15)
    private java.lang.String reward;

    /**
     * 消耗列表
     */
    @ExcelProperty("消耗列表")
    @Excel(name = "消耗列表", width = 15)
    private java.lang.String consume;

    /**
     * 最小世界等级
     */
    @ExcelProperty("最小世界等级")
    @Excel(name = "最小世界等级", width = 15)
    private Integer minLevel;

    /**
     * 最大世界等级
     */
    @ExcelProperty("最大世界等级")
    @Excel(name = "最大世界等级", width = 15)
    private Integer maxLevel;
}

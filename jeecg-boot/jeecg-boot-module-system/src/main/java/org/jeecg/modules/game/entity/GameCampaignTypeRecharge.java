package org.jeecg.modules.game.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jeecgframework.poi.excel.annotation.Excel;

import java.math.BigDecimal;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 充值活动
 * @date 2020-10-15
 */
@Data
@TableName("game_campaign_type_recharge")
@EqualsAndHashCode(callSuper = true)
public class GameCampaignTypeRecharge extends GameCampaignTypeBase {

    private static final long serialVersionUID = 1L;


    public GameCampaignTypeRecharge() {
    }

    public GameCampaignTypeRecharge(GameCampaignTypeRecharge other) {
        this.rechargeId = other.getRechargeId();
        this.rechargeAmount = other.getRechargeAmount();
        this.reward = other.getReward();
    }

    /**
     * 礼包id
     */
    @ExcelProperty("礼包id")
    @Excel(name = "礼包id", width = 15)
    private Integer rechargeId;

    /**
     * 累计充值额度
     */
    @ExcelProperty("累计充值额度")
    @Excel(name = "累计充值额度", width = 15)
    private BigDecimal rechargeAmount;

    /**
     * 奖励列表
     */
    @ExcelProperty("奖励列表")
    @Excel(name = "奖励列表", width = 15)
    private String reward;
}

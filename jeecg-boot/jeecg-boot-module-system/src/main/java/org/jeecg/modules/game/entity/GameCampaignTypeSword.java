package org.jeecg.modules.game.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jeecgframework.poi.excel.annotation.Excel;


/**
 * @author jeecg-boot
 * @version V1.0
 * @description game_campaign_type_sword
 * @date 2021-02-23
 */
@Data
@TableName("game_campaign_type_sword")
@EqualsAndHashCode(callSuper = true)
public class GameCampaignTypeSword extends GameCampaignTypeBase {

    private static final long serialVersionUID = 1L;

    public GameCampaignTypeSword() {
    }

    public GameCampaignTypeSword(GameCampaignTypeSword other) {
        this.checkpointId = other.getCheckpointId();
        this.monsterId = other.getMonsterId();
        this.unlockCheckpointId = other.getUnlockCheckpointId();
        this.reward = other.getReward();
        this.combatPower = other.getCombatPower();
        this.checkpointName = other.getCheckpointName();
    }


    /**
     * 关卡id
     */
    @Excel(name = "关卡id", width = 15)
    @ExcelProperty("关卡id")
    private java.lang.Integer checkpointId;

    /**
     * 怪物id
     */
    @Excel(name = "怪物id", width = 15)
    @ExcelProperty("怪物id")
    private java.lang.Integer monsterId;

    /**
     * 解锁关卡
     */
    @Excel(name = "解锁关卡", width = 15)
    @ExcelProperty("解锁关卡")
    private java.lang.Integer unlockCheckpointId;

    /**
     * 奖励
     */
    @Excel(name = "奖励", width = 15)
    @ExcelProperty("奖励")
    private java.lang.String reward;


    /**
     * 奖励
     */
    @Excel(name = "推荐战力", width = 15)
    @ExcelProperty("推荐战力")
    private java.lang.Long combatPower;

    /**
     * 关卡名
     */
    @Excel(name = "关卡名", width = 15)
    @ExcelProperty("关卡名")
    private java.lang.String checkpointName;
}

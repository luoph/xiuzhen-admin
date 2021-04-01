package org.jeecg.modules.game.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecg.common.constant.TimeConstant;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description game_campaign_type_sword
 * @date 2021-02-23
 */
@Data
@TableName("game_campaign_type_sword")
@Accessors(chain = true)
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
    private java.lang.Integer checkpointId;

    /**
     * 怪物id
     */
    @Excel(name = "怪物id", width = 15)
    private java.lang.Integer monsterId;

    /**
     * 解锁关卡
     */
    @Excel(name = "解锁关卡", width = 15)
    private java.lang.Integer unlockCheckpointId;

    /**
     * 奖励
     */
    @Excel(name = "奖励", width = 15)
    private java.lang.String reward;


    /**
     * 奖励
     */
    @Excel(name = "推荐战力", width = 15)
    private java.lang.Long combatPower;

    /**
     * 关卡名
     */
    @Excel(name = "关卡名", width = 15)
    private java.lang.String checkpointName;
}

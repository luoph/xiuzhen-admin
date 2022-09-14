package cn.youai.xiuzhen.game.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description game_campaign_type_sword
 * @date 2021-02-23
 */
@Data
public class GameCampaignTypeSwordVO {

    /**
     * 关卡id
     */
    @ExcelProperty("关卡id")
    private Integer checkpointId;

    /**
     * 怪物id
     */
    @ExcelProperty("怪物id")
    private Integer monsterId;

    /**
     * 推荐战力
     */
    @ExcelProperty("推荐战力")
    private java.lang.Long combatPower;
    /**
     * 解锁关卡
     */
    @ExcelProperty("解锁关卡")
    private Integer unlockCheckpointId;

    /**
     * 奖励
     */
    @ExcelProperty("奖励")
    private String reward;

    /**
     * 关卡名
     */
    @ExcelProperty("关卡名")
    private String checkpointName;
}

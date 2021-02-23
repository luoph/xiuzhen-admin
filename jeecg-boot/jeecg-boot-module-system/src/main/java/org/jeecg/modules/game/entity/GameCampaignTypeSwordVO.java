package org.jeecg.modules.game.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
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

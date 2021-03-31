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
 * @description 节日派对进度任务
 * @date 2021-03-30
 */
@Data
@TableName("game_campaign_type_party_progress")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class GameCampaignTypePartyProgress extends GameCampaignTypeBase {

    private static final long serialVersionUID = 1L;

    /**
     * 任务规定数量
     */
    @Excel(name = "任务规定数量", width = 15)
    private java.lang.Integer target;

    /**
     * 进度百分比
     */
    @Excel(name = "进度百分比", width = 15)
    private java.lang.Integer percent;

    /**
     * 任务奖励
     */
    @Excel(name = "任务奖励", width = 15)
    private java.lang.String reward;

}

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
 * @description 节日派对任务
 * @date 2021-03-30
 */
@Data
@TableName("game_campaign_type_party_task")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class GameCampaignTypePartyTask extends GameCampaignTypeBase {

    private static final long serialVersionUID = 1L;

    /**
     * 任务类型
     */
    @Excel(name = "任务类型", width = 15)
    private java.lang.Integer type;

    /**
     * 任务模块id
     */
    @Excel(name = "任务模块id", width = 15)
    private java.lang.Integer moduleId;

    /**
     * 参数
     */
    @Excel(name = "参数", width = 15)
    private java.lang.Integer args;

    /**
     * 任务描述
     */
    @Excel(name = "任务描述", width = 15)
    private java.lang.String remark;

    /**
     * 任务规定数量
     */
    @Excel(name = "任务规定数量", width = 15)
    private java.lang.Integer target;

    /**
     * 直接消耗数量
     */
    @Excel(name = "直接消耗数量", width = 15)
    private java.lang.Integer costNum;

    /**
     * 跳转id
     */
    @Excel(name = "跳转id", width = 15)
    private java.lang.Integer jumpId;

    /**
     * 任务奖励
     */
    @Excel(name = "任务奖励", width = 15)
    private java.lang.String reward;
}

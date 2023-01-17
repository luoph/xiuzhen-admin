package cn.youai.xiuzhen.game.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * @Description: 节日活动-夺宝战令
 * @Author: jeecg-boot
 * @Date: 2023-01-17
 * @Version: V1.0
 */
@Data
@TableName("game_campaign_type_lottery_token")
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "game_campaign_type_lottery_token对象", description = "节日活动-夺宝战令")
public class GameCampaignTypeLotteryToken extends GameCampaignTypeBaseDetail {
    private static final long serialVersionUID = 1L;

    /**
     * 任务id
     */
    @Excel(name = "任务id", width = 15)
    @ApiModelProperty(value = "任务id")
    private Integer taskId;

    /**
     * 描述
     */
    @Excel(name = "描述", width = 15)
    @ApiModelProperty(value = "描述")
    private String description;

    /**
     * 模块id
     */
    @Excel(name = "模块id", width = 15)
    @ApiModelProperty(value = "模块id")
    private Integer moduleId;

    /**
     * 任务完成条件
     */
    @Excel(name = "任务完成条件", width = 15)
    @ApiModelProperty(value = "任务完成条件")
    private Integer target;

    /**
     * 任务参数
     */
    @Excel(name = "任务参数", width = 15)
    @ApiModelProperty(value = "任务参数")
    private Integer args;

    /**
     * 普通奖励
     */
    @Excel(name = "普通奖励", width = 15)
    @ApiModelProperty(value = "普通奖励")
    private String reward;

    /**
     * 特殊奖励
     */
    @Excel(name = "特殊奖励", width = 15)
    @ApiModelProperty(value = "特殊奖励")
    private String specialReward;

    /**
     * 跳转id
     */
    @Excel(name = "跳转id", width = 15)
    @ApiModelProperty(value = "跳转id")
    private Integer jumpId;
}

package cn.youai.xiuzhen.game.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * @Description: 节日活动-阶段任务-任务
 * @Author: jeecg-boot
 * @Date: 2023-01-13
 * @Version: V1.0
 */
@Data
@TableName("game_campaign_type_stage_task_item")
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "game_campaign_type_stage_task_item对象", description = "节日活动-阶段任务-任务")
public class GameCampaignTypeStageTaskItem extends GameCampaignTypeBase {
    private static final long serialVersionUID = 1L;

    /**
     * 阶段
     */
    @Excel(name = "阶段", width = 15)
    @ApiModelProperty(value = "阶段")
    private Integer stage;

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
     * 模块id, task_module_type.json.module_id
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
     * 奖励 [{"itemid":1001, "num":1}]
     */
    @Excel(name = "奖励", width = 15)
    @ApiModelProperty(value = "奖励")
    private String reward;

    /**
     * 跳转id
     */
    @Excel(name = "跳转id", width = 15)
    @ApiModelProperty(value = "跳转id")
    private Integer jumpId;
}

package org.jeecg.modules.game.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 任务活动
 * @date 2020-10-15
 */
@Data
@TableName("game_campaign_type_task")
@EqualsAndHashCode(callSuper = true)
public class GameCampaignTypeTask extends GameCampaignTypeBase {

    private static final long serialVersionUID = 1L;

    public GameCampaignTypeTask() {
    }

    public GameCampaignTypeTask(GameCampaignTypeTask other) {
        this.taskId = other.getTaskId();
        this.description = other.getDescription();
        this.moduleId = other.getModuleId();
        this.target = other.getTarget();
        this.args = other.getArgs();
        this.reward = other.getReward();
        this.jumpId = other.getJumpId();
    }

    /**
     * 任务id
     */
    @ExcelProperty("任务id")
    @Excel(name = "任务id", width = 15)
    private Integer taskId;

    /**
     * 描述
     */
    @ExcelProperty("任务描述")
    @Excel(name = "任务描述", width = 15)
    private String description;

    /**
     * task_module_type.json.module_id
     */
    @ExcelProperty("模块id")
    @Excel(name = "模块id", width = 15)
    private Integer moduleId;

    /**
     * 任务完成条件
     */
    @ExcelProperty("条件")
    @Excel(name = "条件", width = 15)
    private Integer target;

    /**
     * 任务参数
     */
    @ExcelProperty("参数")
    @Excel(name = "参数", width = 15)
    private Integer args;

    /**
     * 奖励列表
     */
    @ExcelProperty("奖励列表")
    @Excel(name = "奖励列表", width = 15)
    private String reward;
    /**
     * 跳转 id
     */
    @ExcelProperty("跳转id")
    @Excel(name = "跳转id", width = 15)
    private Integer jumpId;
}

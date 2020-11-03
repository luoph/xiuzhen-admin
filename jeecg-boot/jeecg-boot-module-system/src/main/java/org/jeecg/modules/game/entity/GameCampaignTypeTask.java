package org.jeecg.modules.game.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 任务活动
 * @date 2020-10-15
 */
@Data
@TableName("game_campaign_type_task")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class GameCampaignTypeTask extends GameCampaignTypeBase {

    private static final long serialVersionUID = 1L;

    /**
     * 任务id
     */
    @Excel(name = "任务id", width = 15)
    private Integer taskId;

    /**
     * 描述
     */
    @Excel(name = "描述", width = 15)
    private String description;

    /**
     * task_module_type.json.module_id
     */
    @Excel(name = "task_module_type.json.module_id", width = 15)
    private Integer moduleId;

    /**
     * 任务完成条件
     */
    @Excel(name = "任务完成条件", width = 15)
    private Integer target;

    /**
     * 奖励列表
     */
    @Excel(name = "奖励列表", width = 15)
    private String reward;
    /**
     * 跳转 id
     */
    @Excel(name = "跳转 id", width = 15)
    private Integer jumpId;
}

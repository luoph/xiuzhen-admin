package cn.youai.xiuzhen.game.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 任务活动
 * @date 2020-10-15
 */
@Data
public class GameCampaignTypeTaskVO {

    /**
     * 任务id
     */
    @ExcelProperty("任务id")
    private Integer taskId;

    /**
     * 描述
     */
    @ExcelProperty("任务描述")
    private String description;

    /**
     * task_module_type.json.module_id
     */
    @ExcelProperty("模块id")
    private Integer moduleId;

    /**
     * 任务完成条件
     */
    @ExcelProperty("条件")
    private Integer target;

    /**
     * 任务参数
     */
    @ExcelProperty("参数")
    private Integer args;

    /**
     * 奖励列表
     */
    @ExcelProperty("奖励列表")
    private String reward;
    /**
     * 跳转 id
     */
    @ExcelProperty("跳转id")
    private Integer jumpId;

    /**
     * 最小世界等级
     */
    @ExcelProperty("最小世界等级")
    private Integer minLevel;

    /**
     * 最大世界等级
     */
    @ExcelProperty("最大世界等级")
    private Integer maxLevel;
}

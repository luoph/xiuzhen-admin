package org.jeecg.modules.game.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 节日派对任务
 * @date 2021-03-30
 */
@Data
public class GameCampaignTypePartyTaskVO {

    /**
     * 任务类型
     */
    @ExcelProperty("任务类型")
    private Integer type;

    /**
     * 任务模块id
     */
    @ExcelProperty("任务模块id")
    private Integer moduleId;

    /**
     * 参数
     */
    @ExcelProperty("参数")
    private Integer args;

    /**
     * 任务描述
     */
    @ExcelProperty("任务描述")
    private String remark;

    /**
     * 任务规定数量
     */
    @ExcelProperty("任务规定数量")
    private Integer target;

    /**
     * 直接消耗数量
     */
    @ExcelProperty("直接消耗数量")
    private Integer costNum;

    /**
     * 跳转id
     */
    @ExcelProperty("跳转id")
    private Integer jumpId;

    /**
     * 任务奖励
     */
    @ExcelProperty("任务奖励")
    private String reward;
}

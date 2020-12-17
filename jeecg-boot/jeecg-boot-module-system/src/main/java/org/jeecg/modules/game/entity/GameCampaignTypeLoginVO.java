package org.jeecg.modules.game.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 登录活动
 * @date 2020-10-15
 */
@Data
public class GameCampaignTypeLoginVO {

    /**
     * 登录天数
     */
    @ExcelProperty("登录天数")
    private Integer loginDay;

    /**
     * 描述
     */
    @ExcelProperty("描述")
    private String description;

    /**
     * 奖励列表
     */
    @ExcelProperty("奖励列表")
    private String reward;
}

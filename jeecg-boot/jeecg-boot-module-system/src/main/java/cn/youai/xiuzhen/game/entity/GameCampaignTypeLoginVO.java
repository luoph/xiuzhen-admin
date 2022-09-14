package cn.youai.xiuzhen.game.entity;

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

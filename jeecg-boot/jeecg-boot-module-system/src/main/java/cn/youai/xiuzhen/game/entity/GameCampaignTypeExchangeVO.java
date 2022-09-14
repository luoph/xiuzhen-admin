package cn.youai.xiuzhen.game.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 兑换活动
 * @date 2020-10-15
 */
@Data
public class GameCampaignTypeExchangeVO {

    private static final long serialVersionUID = 1L;

    /**
     * 兑换id
     */
    @ExcelProperty("兑换id")
    private Integer exchangeId;

    /**
     * 道具名称
     */
    @ExcelProperty("道具名称")
    private String itemName;

    /**
     * 最大兑换数量
     */
    @ExcelProperty("最大兑换数量")
    private Integer maxExchangeNum;

    /**
     * 奖励列表
     */
    @ExcelProperty("奖励列表")
    private String reward;

    /**
     * 消耗列表
     */
    @ExcelProperty("消耗列表")
    private String consume;

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

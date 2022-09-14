package cn.youai.xiuzhen.game.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 砸蛋礼包
 * @date 2021-03-10
 */
@Data
public class GameCampaignTypeThrowingEggsGiftVO {


    /**
     * 消耗道具
     */
    @ExcelProperty("消耗道具")
    private Integer costItemId;

    /**
     * 消耗道具数量
     */
    @ExcelProperty("消耗道具数量")
    private Integer costNum;

    /**
     * 库存
     */
    @ExcelProperty("库存")
    private Integer stack;

    /**
     * 原价
     */
    @ExcelProperty("原价")
    private Integer amount;

    /**
     * 折扣
     */
    @ExcelProperty("折扣")
    private Integer discount;

    /**
     * 限购条件
     */
    @ExcelProperty("限购条件")
    private String limitCondition;

    /**
     * 显示奖励内容
     */
    @ExcelProperty("奖励内容")
    private String reward;


    @ExcelProperty("礼包名")
    private String giftName;


}

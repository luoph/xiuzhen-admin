package org.jeecg.modules.game.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;
import org.jeecg.common.constant.TimeConstant;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 砸蛋礼包
 * @date 2021-03-10
 */
@Data
public class GameCampaignTypeThrowingEggsGiftVO {

    /**
     * 道具id
     */
    @ExcelProperty("道具id")
    private Integer itemId;

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
    @ExcelProperty("显示奖励内容")
    private String showReward;

}

package org.jeecg.modules.game.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecg.common.system.base.entity.BaseEntity;
import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 直购礼包
 * @date 2021-04-15
 */
@Data
@Accessors(chain = true)
public class GameCampaignDirectPurchaseVO {

    /**
     * 已购数量
     */
    @ExcelProperty("已购数量")
    private Integer limitNum;

    /**
     * 单价
     */
    @ExcelProperty("单价s")
    private java.math.BigDecimal price;

    /**
     * 折扣
     */
    @ExcelProperty("折扣")
    private Integer discount;

    /**
     * 原价
     */
    @ExcelProperty("原价")
    private java.math.BigDecimal amount;

    /**
     * 礼包名
     */
    @ExcelProperty("礼包名")
    private String name;

    /**
     * 礼包组类型
     */
    @ExcelProperty("礼包组类型")
    private Integer type;

    /**
     * 组排序
     */
    @ExcelProperty("组排序")
    private Integer sort;

    /**
     * 奖励列表
     */
    @ExcelProperty("奖励列表")
    private String reward;
}

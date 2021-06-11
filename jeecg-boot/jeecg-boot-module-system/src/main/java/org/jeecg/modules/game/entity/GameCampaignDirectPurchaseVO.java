package org.jeecg.modules.game.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

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
    @ExcelProperty("商品Id")
    private java.lang.Long goodsId;

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

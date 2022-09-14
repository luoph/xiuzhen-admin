/*
 * create by mybatis-plus-generator  https://github.com/xiweile
 */
package cn.youai.xiuzhen.game.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * <p>
 * 节日活动-自选特惠-物品部分
 * </p>
 *
 * @author chuantian
 * @since 2021-09-08
 */
@Data
public class GameCampaignTypeSelectDiscountItemVO {

    /**
     * 位置序号
     */
    @ExcelProperty("关卡id")
    private Integer showOrder;

    /**
     * 商品描述
     */
    @ExcelProperty("商品描述")
    private String itemDesc;

    /**
     * 限购的话次数多少
     */
    @ExcelProperty("限购的话次数多少")
    private Integer limitNum;

    /**
     * 商品id
     */
    @ExcelProperty("商品id")
    private Integer goodsId;

    /**
     * 可选物品
     */
    @ExcelProperty("可选物品")
    private String chooseItems;

    /**
     * 免费物品
     */
    @ExcelProperty("免费物品")
    private String freeItems;

    /**
     * 本组商品是否免费领取
     */
    @ExcelProperty("本组商品是否免费领取")
    private Boolean free;
}

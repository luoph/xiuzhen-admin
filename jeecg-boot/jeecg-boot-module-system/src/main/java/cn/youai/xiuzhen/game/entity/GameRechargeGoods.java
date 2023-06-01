package cn.youai.xiuzhen.game.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jeecg.common.system.base.entity.BaseEntity;
import org.jeecgframework.poi.excel.annotation.Excel;

import java.math.BigDecimal;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description game_recharge_goods
 * @date 2021-04-16
 */
@Data
@TableName("game_recharge_goods")
@EqualsAndHashCode(callSuper = true)
public class GameRechargeGoods extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 自增id
     */
    @ExcelProperty("id")
    @Excel(name = "id", width = 15)
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 单价(创建订单实际价格)
     */
    @ExcelProperty("商品Id")
    @Excel(name = "商品Id", width = 15)
    private Integer goodsId;

    /**
     * SKU
     */
    @ExcelProperty("sku")
    @Excel(name = "sku", width = 15)
    private String sku;

    /**
     * webSku
     */
    @ExcelProperty("webSku")
    @Excel(name = "webSku", width = 15)
    private String webSku;

    /**
     * 商品类型
     */
    @ExcelProperty("商品类型")
    @Excel(name = "商品类型", width = 15)
    private Integer goodsType;

    /**
     * 商品组别
     */
    @ExcelProperty("商品组别")
    @Excel(name = "商品组别", width = 15)
    private Integer goodsGroup;

    /**
     * 购买类型: 1.真实充值, 2.gm额度, 3.代金券
     */
    @ExcelProperty("购买类型")
    @Excel(name = "购买类型", width = 15)
    private String buyType;

    /**
     * gm额度
     */
    @ExcelProperty("gm额度")
    @Excel(name = "gm额度", width = 15)
    private Long gmCoin;

    /**
     * 代金券
     */
    @ExcelProperty("代金券")
    @Excel(name = "代金券", width = 15)
    private Long cashCoupon;

    /**
     * 商品名
     */
    @ExcelProperty("商品名")
    @Excel(name = "商品名", width = 15)
    private String name;

    /**
     * 单价(创建订单实际价格)
     */
    @ExcelProperty("单价(创建订单实际价格)")
    @Excel(name = "单价(创建订单实际价格)", width = 15)
    private BigDecimal price;

    /**
     * 当地货币价格
     */
    @ExcelProperty("当地货币价格")
    @Excel(name = "当地货币价格", width = 15)
    private BigDecimal localPrice;

    /**
     * web支付当地货币价格
     */
    @ExcelProperty("web支付当地货币价格")
    @Excel(name = "web支付当地货币价格", width = 15)
    private BigDecimal webLocalPrice;

    /**
     * 显示价格
     */
    @ExcelProperty("显示价格")
    @Excel(name = "显示价格", width = 15)
    private BigDecimal displayPrice;

    /**
     * web支付当地货币显示价格
     */
    @ExcelProperty("web显示价格")
    @Excel(name = "web显示价格", width = 15)
    private BigDecimal webDisplayPrice;

    /**
     * 折扣(打折后的价格)
     */
    @ExcelProperty("折扣价")
    @Excel(name = "折扣价", width = 15)
    private BigDecimal discount;

    /**
     * 奖励列表
     */
    @ExcelProperty("奖励列表")
    @Excel(name = "奖励列表", width = 15)
    private String items;

    /**
     * 是否记入累充（0 - 不计入 1 - 记入）
     */
    @ExcelProperty("是否记入累充")
    @Excel(name = "是否记入累充", width = 15)
    private Integer amountStat;

    /**
     * 累充统计类型
     */
    @ExcelProperty("累充统计类型")
    @Excel(name = "累充统计类型", width = 15)
    private String amountStatTypes;

    /**
     * 货币
     */
    @ExcelProperty("货币")
    @Excel(name = "货币", width = 15)
    private String currency;

    /**
     * 首次额外赠送
     */
    @ExcelProperty("首次额外赠送")
    @Excel(name = "首次额外赠送", width = 15)
    private String addition;

    /**
     * 游戏币与货币的兑换比例
     */
    @ExcelProperty("兑换比例")
    @Excel(name = "兑换比例", width = 15)
    private Integer exchange;

    /**
     * 商品特殊标记
     */
    @ExcelProperty("特殊标记")
    @Excel(name = "特殊标记", width = 15)
    private Integer recommend;

    /**
     * 传闻
     */
    @ExcelProperty("传闻")
    @Excel(name = "传闻", width = 15)
    private String rumor;

    /**
     * 备注
     */
    @ExcelProperty("备注")
    @Excel(name = "备注", width = 15)
    private String remark;
}

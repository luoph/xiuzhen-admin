package cn.youai.xiuzhen.game.entity;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jeecg.common.system.base.entity.BaseEntity;
import org.jeecg.modules.converter.CustomStringToBigDecimalConverter;
import org.jeecgframework.poi.excel.annotation.Excel;

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
    @TableId(type = IdType.AUTO)
    @ExcelIgnore
    private java.lang.Long id;

    /**
     * 单价(创建订单实际价格)
     */
    @Excel(name = "商品Id", width = 15)
    @ExcelProperty(value = "商品Id")
    private java.lang.Integer goodsId;

    /**
     * 商品名
     */
    @Excel(name = "商品名", width = 15)
    @ExcelProperty(value = "商品名")
    private java.lang.String name;

    /**
     * 奖励列表
     */
    @Excel(name = "奖励列表", width = 15)
    @ExcelProperty(value = "奖励列表")
    private java.lang.String items;

    /**
     * 商品类型
     */
    @Excel(name = "商品类型", width = 15)
    @ExcelProperty(value = "商品类型")
    private java.lang.Integer goodsType;

    /**
     * 商品组别
     */
    @Excel(name = "商品组别", width = 15)
    @ExcelProperty(value = "商品组别")
    private java.lang.Integer goodsGroup;

    /**
     * SKU
     */
    @Excel(name = "sku", width = 15)
    @ExcelProperty(value = "sku")
    private java.lang.String sku;

    /**
     * webSku
     */
    @Excel(name = "webSku", width = 15)
    @ExcelProperty(value = "webSku")
    private java.lang.String webSku;

    /**
     * 单价(创建订单实际价格)
     */
    @Excel(name = "单价(创建订单实际价格)", width = 15)
    @ExcelProperty(value = "单价(创建订单实际价格)", converter = CustomStringToBigDecimalConverter.class)
    private java.math.BigDecimal price;

    /**
     * 当地货币价格
     */
    @Excel(name = "当地货币价格", width = 15)
    @ExcelProperty(value = "当地货币价格", converter = CustomStringToBigDecimalConverter.class)
    private java.math.BigDecimal localPrice;

    /**
     * web支付当地货币价格
     */
    @Excel(name = "web支付当地货币价格", width = 15)
    @ExcelProperty(value = "web支付当地货币价格", converter = CustomStringToBigDecimalConverter.class)
    private java.math.BigDecimal webLocalPrice;

    /**
     * 显示价格
     */
    @Excel(name = "显示价格", width = 15)
    @ExcelProperty(value = "显示价格", converter = CustomStringToBigDecimalConverter.class)
    private java.math.BigDecimal displayPrice;

    /**
     * web支付当地货币显示价格
     */
    @Excel(name = "web显示价格", width = 15)
    @ExcelProperty(value = "web显示价格", converter = CustomStringToBigDecimalConverter.class)
    private java.math.BigDecimal webDisplayPrice;

    /**
     * 折扣(打折后的价格)
     */
    @Excel(name = "折扣价", width = 15)
    @ExcelProperty(value = "折扣价", converter = CustomStringToBigDecimalConverter.class)
    private java.math.BigDecimal discount;

    /**
     * 是否记入累充（0 - 不计入 1 - 记入）
     */
    @Excel(name = "是否记入累充", width = 15)
    @ExcelProperty(value = "是否记入累充")
    private java.lang.Integer amountStat;

    /**
     * 首次额外赠送
     */
    @Excel(name = "首次额外赠送", width = 15)
    @ExcelProperty(value = "首次额外赠送")
    private java.lang.String addition;

    /**
     * 游戏币与货币的兑换比例
     */
    @Excel(name = "兑换比例", width = 15)
    @ExcelProperty(value = "兑换比例")
    private java.lang.Integer exchange;

    /**
     * 传闻
     */
    @Excel(name = "传闻", width = 15)
    @ExcelProperty(value = "传闻")
    private java.lang.String rumor;

    /**
     * 商品特殊标记
     */
    @Excel(name = "特殊标记", width = 15)
    @ExcelProperty(value = "特殊标记")
    private Integer recommend;

    /**
     * 货币
     */
    @Excel(name = "货币", width = 15)
    @ExcelProperty(value = "货币")
    private String currency;
}

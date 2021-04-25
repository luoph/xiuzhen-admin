package org.jeecg.modules.game.entity;

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
 * @description game_recharge_goods
 * @date 2021-04-16
 */
@Data
@TableName("game_recharge_goods")
@Accessors(chain = true)
public class GameRechargeGoods implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 自增id
     */
    @TableId(type = IdType.AUTO)
    private java.lang.Integer id;

    /**
     * 单价(创建订单实际价格)
     */
    @Excel(name = "商品Id", width = 15)
    private java.lang.Long goodsId;

    /**
     * 单价(创建订单实际价格)
     */
    @Excel(name = "单价(创建订单实际价格)", width = 15)
    private java.math.BigDecimal price;

    /**
     * 折扣
     */
    @Excel(name = "折扣", width = 15)
    private java.lang.Integer discount;

    /**
     * 商品名
     */
    @Excel(name = "商品名", width = 15)
    private java.lang.String name;

    /**
     * 奖励列表
     */
    @Excel(name = "奖励列表", width = 15)
    private java.lang.String items;

    /**
     * 充值分类
     */
    @Excel(name = "商品分类", width = 15)
    private java.lang.Integer goodsType;

    /**
     * 是否记入累充（0 - 不计入 1 - 记入）
     */
    @Excel(name = "是否记入累充（0 - 不计入 1 - 记入）", width = 15)
    private java.lang.Integer amountStat;

    /**
     * 首次额外赠送
     */
    @Excel(name = "首次额外赠送", width = 15)
    private java.lang.String addition;

    /**
     * 游戏币与人民币(元)的兑换比例
     */
    @Excel(name = "游戏币与人民币(元)的兑换比例", width = 15)
    private java.lang.Integer exchange;

    /**
     * 传闻
     */
    @Excel(name = "传闻", width = 15)
    private java.lang.String rumor;

    /**
     * 商品特殊标记
     */
    @Excel(name = "特殊标记", width = 15)
    private Integer recommend;

    /**
     * 创建时间
     */
    @Excel(name = "创建时间", width = 15, format = TimeConstant.DEFAULT_DATE_FORMAT)
    @JsonFormat(timezone = TimeConstant.DEFAULT_TIMEZONE, pattern = TimeConstant.DEFAULT_DATE_FORMAT)
    @DateTimeFormat(pattern = TimeConstant.DEFAULT_DATE_FORMAT)
    private java.util.Date createTime;

    /**
     * 创建者
     */
    @Excel(name = "创建者", width = 15)
    private java.lang.String createBy;

    /**
     * 更新时间
     */
    @Excel(name = "更新时间", width = 15, format = TimeConstant.DEFAULT_DATE_FORMAT)
    @JsonFormat(timezone = TimeConstant.DEFAULT_TIMEZONE, pattern = TimeConstant.DEFAULT_DATE_FORMAT)
    @DateTimeFormat(pattern = TimeConstant.DEFAULT_DATE_FORMAT)
    private java.util.Date updateTime;

    /**
     * 更新者
     */
    @Excel(name = "更新者", width = 15)
    private java.lang.String updateBy;
}

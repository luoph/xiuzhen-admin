/*
 * create by mybatis-plus-generator  https://github.com/xiweile
 */
package cn.youai.xiuzhen.game.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 每日数据统计
 * </p>
 *
 * @author buliangliang
 * @since 2020-08-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class GameStatDaily implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 自增id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 每天登录玩家数
     */
    private Integer loginNum;

    /**
     * 渠道标记
     */
    private String channel;

    /**
     * 服务器id
     */
    private Integer serverId;

    /**
     * 每天支付总额
     */
    private BigDecimal payAmount;

    /**
     * 每天支付玩家数
     */
    private Integer payNum;

    /**
     * 每天支付率
     */
    private BigDecimal payRate;

    /**
     * arpu
     */
    private BigDecimal arpu;

    /**
     * arppu
     */
    private BigDecimal arppu;

    /**
     * 新增注册玩家数
     */
    private Integer addNum;

    /**
     * 新增注册玩家支付总额
     */
    private BigDecimal addPayAmount;

    /**
     * 新增注册玩家支付数
     */
    private Integer addPayNum;

    /**
     * 新增注册玩家支付率
     */
    private BigDecimal addPayRate;

    /**
     * 新增注册arpu
     */
    private BigDecimal addArpu;

    /**
     * 新增注册arppu
     */
    private BigDecimal addArppu;

    /**
     * 新增注册二次付费玩家数
     */
    private Integer doublePay;

    /**
     * 二次付费率
     */
    private BigDecimal doublePayRate;

    /**
     * 统计日期
     */
    private Date countDate;

    /**
     * 创建时间
     */
    private Date createTime;


}

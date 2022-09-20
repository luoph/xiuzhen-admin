/*
 * create by mybatis-plus-generator  https://github.com/xiweile
 */
package cn.youai.xiuzhen.stat.entity;

import cn.youai.server.utils.DateUtils;
import cn.youai.xiuzhen.stat.constant.StatisticType;
import cn.youai.xiuzhen.utils.BigDecimalUtils;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
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
    private Integer payPlayerNum;

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
    private Integer newPlayerNum;

    /**
     * 新增注册玩家支付总额
     */
    private BigDecimal newPlayerPayAmount;

    /**
     * 新增注册玩家支付数
     */
    private Integer newPlayerPayNum;

    /**
     * 新增注册玩家支付率
     */
    private BigDecimal newPlayerPayRate;

    /**
     * 新增注册arpu
     */
    private BigDecimal newPlayerArpu;

    /**
     * 新增注册arppu
     */
    private BigDecimal newPlayerArppu;

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


    public static GameStatDaily of(String channel, Date date,
                                   BigDecimal payAmount,
                                   int payPlayerNum,
                                   int loginNum,
                                   int registerPlayer,
                                   BigDecimal registerPayAmount,
                                   int registerPayPlayer,
                                   int doublePayPlayer) {
        return new GameStatDaily()
                .setChannel(channel)
                .setServerId(StatisticType.DEFAULT_SERVER_ID)
                .setCountDate(date)
                .setPayAmount(payAmount)
                .setLoginNum(loginNum)
                .setPayPlayerNum(payPlayerNum)
                .setNewPlayerNum(registerPlayer)
                .setNewPlayerPayNum(registerPayPlayer)
                .setNewPlayerPayAmount(registerPayAmount)
                .setDoublePay(doublePayPlayer)
                .setCreateTime(DateUtils.now())
                .calc();
    }

    public static GameStatDaily of(int serverId, Date date,
                                   BigDecimal payAmount,
                                   int payPlayerNum,
                                   int loginNum,
                                   int registerPlayer,
                                   BigDecimal registerPayAmount,
                                   int registerPayPlayer,
                                   int doublePayPlayer) {
        return new GameStatDaily()
                .setChannel(StatisticType.DEFAULT_CHANNEL)
                .setServerId(serverId)
                .setServerId(StatisticType.DEFAULT_SERVER_ID)
                .setCountDate(date)
                .setPayAmount(payAmount)
                .setLoginNum(loginNum)
                .setPayPlayerNum(payPlayerNum)
                .setNewPlayerNum(registerPlayer)
                .setNewPlayerPayNum(registerPayPlayer)
                .setNewPlayerPayAmount(registerPayAmount)
                .setDoublePay(doublePayPlayer)
                .setCreateTime(DateUtils.now())
                .calc();
    }

    public GameStatDaily calc() {
        setArpu(BigDecimalUtils.divideZero(payAmount, BigDecimal.valueOf(loginNum), false))
                .setArppu(BigDecimalUtils.divideZero(payAmount, BigDecimal.valueOf(payPlayerNum), false))
                .setPayRate(BigDecimalUtils.divideZero(payPlayerNum, loginNum, true))
                .setNewPlayerPayRate(BigDecimalUtils.divideZero(newPlayerPayNum, newPlayerNum, true))
                .setDoublePayRate(BigDecimalUtils.divideZero(doublePay, newPlayerPayNum, true))
                .setNewPlayerArpu(BigDecimalUtils.divideZero(newPlayerPayAmount, BigDecimal.valueOf(newPlayerPayNum), false))
                .setNewPlayerArppu(BigDecimalUtils.divideZero(newPlayerPayAmount, BigDecimal.valueOf(newPlayerPayNum), false));
        return this;
    }

}

package cn.youai.xiuzhen.stat.entity;

import cn.youai.xiuzhen.game.constant.StatDuration;
import cn.youai.xiuzhen.utils.BigDecimalUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;
import org.jeecg.common.constant.TimeConstant;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 充值统计
 *
 * @author jeecg-boot
 * @version V1.0
 * @date 2021-04-20
 */
@Data
@Accessors(chain = true)
public class GameStatOrder implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 列表统计日期
     */
    @Excel(name = "统计日期", width = 15)
    @JsonFormat(timezone = TimeConstant.DEFAULT_TIMEZONE, pattern = TimeConstant.DEFAULT_DATE_FORMAT)
    @DateTimeFormat(pattern = TimeConstant.DEFAULT_DATE_FORMAT)
    private Date countDate;

    /**
     * 统计范围
     */
    private StatDuration duration;

    /**
     * 统计标识
     */
    private String statTime;

    /**
     * 统计开始日期
     */
    private Date starDate;

    /**
     * 统计结束日期
     */
    private Date endDate;

    /**
     * 快速统计日期范围
     */
    private Integer dayType;

    /**
     * 渠道id
     */
    private String channel;

    /**
     * 服务器id
     */
    private Integer serverId;

    /**
     * 区服数
     */
    @Excel(name = "区服数", width = 15)
    private Integer serverNum;

    /**
     * 活跃角色数
     */
    @Excel(name = "活跃角色数", width = 15)
    private Integer activeNum;

    /**
     * 付费角色数
     */
    @Excel(name = "付费角色数", width = 15)
    private Integer payNum;

    /**
     * 付费金额
     */
    @Excel(name = "付费金额", width = 15)
    private BigDecimal payAmount;

    /**
     * 每用户平均收入
     */
    @Excel(name = "arpu", width = 15)
    private BigDecimal arpu;

    /**
     * 每付费用户平均收益
     */
    @Excel(name = "arppu", width = 15)
    private BigDecimal arppu;

    /**
     * 服均充值
     */
    @Excel(name = "服均充值", width = 15)
    private BigDecimal averageAmount;

    public GameStatOrder calc() {
        this.arpu = BigDecimalUtils.divideZero(payAmount, BigDecimal.valueOf(activeNum), false);
        this.arppu = BigDecimalUtils.divideZero(payAmount, BigDecimal.valueOf(payNum), false);
        this.averageAmount = BigDecimalUtils.divideZero(payAmount, BigDecimal.valueOf(serverNum), false);
        return this;
    }

    public static GameStatOrder zero(StatDuration duration, Date startDate, Date endDate) {
        return new GameStatOrder()
                .setDuration(duration)
                .setStarDate(startDate)
                .setEndDate(endDate)
                .setArpu(BigDecimal.ZERO)
                .setArppu(BigDecimal.ZERO)
                .setPayAmount(BigDecimal.ZERO)
                .setAverageAmount(BigDecimal.ZERO)
                .setServerNum(0)
                .setActiveNum(0)
                .setPayNum(0);
    }

}

package cn.youai.xiuzhen.stat.entity;

import cn.youai.xiuzhen.utils.BigDecimalUtils;
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
import java.math.BigDecimal;
import java.util.Date;

/**
 * ARPU统计
 *
 * @author jeecg-boot
 * @version V1.0
 * @date 2020-10-10
 */
@Data
@TableName("game_stat_arpu")
@Accessors(chain = true)
public class GameStatArpu implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 自增id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 渠道
     */
    @Excel(name = "渠道", width = 15)
    private String channel;

    /**
     * 服务器id
     */
    @Excel(name = "服务器id", width = 15)
    private Integer serverId;

    /**
     * 统计日期
     */
    @Excel(name = "统计日期", width = 15, format = TimeConstant.DEFAULT_DATE_FORMAT)
    @JsonFormat(timezone = TimeConstant.DEFAULT_TIMEZONE, pattern = TimeConstant.DEFAULT_DATE_FORMAT)
    @DateTimeFormat(pattern = TimeConstant.DEFAULT_DATE_FORMAT)
    private Date countDate;

    /**
     * 每天登陆玩家数
     */
    @Excel(name = "活跃玩家", width = 15)
    private Integer loginNum;

    /**
     * 累计玩家数
     */
    @Excel(name = "累计玩家数", width = 15)
    private Integer playerNum;

    /**
     * 新玩家数
     */
    @Excel(name = "新玩家数", width = 15)
    private Integer newPlayerNum;

    /**
     * 新玩家付费数
     */
    @Excel(name = "新玩家付费数", width = 15)
    private Integer newPayNum;

    /**
     * 当日充值金额
     */
    @Excel(name = "当日充值金额", width = 15)
    private BigDecimal payAmount;

    /**
     * 累计充值金额
     */
    @Excel(name = "累计充值金额", width = 15)
    private BigDecimal totalAmount;

    /**
     * 新增玩家支付金额
     */
    @Excel(name = "新增充值金额", width = 15)
    private BigDecimal newPayAmount;

    /**
     * 当日支付玩家数
     */
    @Excel(name = "当日支付玩家数", width = 15)
    private Integer payNum;

    /**
     * 累计支付玩家数
     */
    @Excel(name = "累计支付玩家数", width = 15)
    private Integer totalPayNum;

    /**
     * 活跃玩家付费数
     */
    @Excel(name = "活跃玩家付费数", width = 15)
    private Integer loginPayNum;

    /**
     * 二次付费数
     */
    @Excel(name = "二次付费数", width = 15)
    private BigDecimal doublePayNum;

    /**
     * 老玩家数
     * oldPlayerNum = playerNum - newPlayerNum
     */
    @Excel(name = "老玩家数", width = 15)
    private Integer oldPlayerNum;

    /**
     * 老玩家付费数
     * oldPayNum = payNum - newPayNum
     */
    @Excel(name = "老玩家付费数", width = 15)
    private Integer oldPayNum;

    /**
     * 老玩家充值金额
     * oldPayAmount = totalAmount - newPayAmount
     */
    @Excel(name = "老玩家充值金额", width = 15)
    private BigDecimal oldPayAmount;

    /**
     * arpu
     * arpu = totalAmount / playerNum
     */
    @Excel(name = "ARPU", width = 15)
    private BigDecimal arpu;

    /**
     * arppu
     * arppu = totalAmount / totalPayNum
     */
    @Excel(name = "ARPPU", width = 15)
    private BigDecimal arppu;

    /**
     * 新增注册arpu
     * newArpu = newPayAmount / newPlayerNum
     */
    @Excel(name = "新增ARPU", width = 15)
    private BigDecimal newArpu;

    /**
     * 新增注册arppu
     * newArppu = newPayAmount / newPayNum
     */
    @Excel(name = "新增ARPPU", width = 15)
    private BigDecimal newArppu;


    /**
     * 老玩家ARPU
     * oldArpu = oldPayAmount / oldPlayerNum
     */
    @Excel(name = "老玩家ARPU", width = 15)
    private BigDecimal oldArpu;

    /**
     * 老玩家ARPPU
     * oldArppu = oldPayAmount / oldPayNum
     */
    @Excel(name = "老玩家ARPPU", width = 15)
    private BigDecimal oldArppu;

    /**
     * 创建时间
     */
    @JsonFormat(timezone = TimeConstant.DEFAULT_TIMEZONE, pattern = TimeConstant.DEFAULT_TIME_FORMAT)
    @DateTimeFormat(pattern = TimeConstant.DEFAULT_TIME_FORMAT)
    private Date createTime;

    public GameStatArpu calc() {
        oldPlayerNum = playerNum - newPlayerNum;
        oldPayNum = payNum - newPayNum;
        oldPayAmount = totalAmount.subtract(newPayAmount);
        arpu = BigDecimalUtils.divideZero(totalAmount, BigDecimal.valueOf(playerNum), false);
        arppu = BigDecimalUtils.divideZero(totalAmount, BigDecimal.valueOf(totalPayNum), false);
        newArpu = BigDecimalUtils.divideZero(newPayAmount, BigDecimal.valueOf(newPlayerNum), false);
        newArppu = BigDecimalUtils.divideZero(newPayAmount, BigDecimal.valueOf(newPayNum), false);
        oldArpu = BigDecimalUtils.divideZero(oldPayAmount, BigDecimal.valueOf(oldPlayerNum), false);
        oldArppu = BigDecimalUtils.divideZero(oldPayAmount, BigDecimal.valueOf(oldPayNum), false);
        return this;
    }

    public GameStatArpu apply(GameStatArpu other) {
        setLoginNum(other.getLoginNum());
        setPlayerNum(other.getPlayerNum());
        setNewPlayerNum(other.getNewPlayerNum());
        setNewPayNum(other.getNewPayNum());
        setPayAmount(other.getPayAmount());
        setTotalAmount(other.getTotalAmount());
        setNewPayAmount(other.getNewPayAmount());
        setPayNum(other.getPayNum());
        setTotalPayNum(other.getTotalPayNum());
        setLoginPayNum(other.getLoginPayNum());
        setDoublePayNum(other.getDoublePayNum());
        setOldPlayerNum(other.getOldPlayerNum());
        setOldPayNum(other.getOldPayNum());
        setOldPayAmount(other.getOldPayAmount());
        setArpu(other.getArpu());
        setArppu(other.getArppu());
        setNewArpu(other.getNewArpu());
        setNewArppu(other.getNewArppu());
        setOldArpu(other.getOldArpu());
        setOldArppu(other.getOldArppu());
        return this;
    }
}

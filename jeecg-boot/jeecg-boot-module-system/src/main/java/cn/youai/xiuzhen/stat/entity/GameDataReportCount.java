package cn.youai.xiuzhen.stat.entity;

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

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 数据报表
 * @date 2020-10-10
 */
@Data
@TableName("game_day_data_count")
@Accessors(chain = true)
public class GameDataReportCount implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 自增id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 渠道
     */
    //@Excel(name = "渠道", width = 15)
    private String channel;

    /**
     * 服务器id
     */
    //@Excel(name = "服务器id", width = 15)
    private Integer serverId;

    /**
     * 每天登陆玩家数
     */
    @Excel(name = "活跃玩家", width = 15)
    private Integer loginNum;

    /**
     * 每天支付总额
     */
    @Excel(name = "充值金额", width = 15)
    private java.math.BigDecimal payAmount;

    /**
     * 每天支付玩家数
     */
    @Excel(name = "活跃付费数", width = 15)
    private Integer payNum;

    /**
     * 每天支付率
     */
    @Excel(name = "活跃付费率", width = 15)
    private java.math.BigDecimal payRate;

    /**
     * arpu
     */
    @Excel(name = "ARPU", width = 15)
    private java.math.BigDecimal arpu;

    /**
     * arppu
     */
    @Excel(name = "ARPPU", width = 15)
    private java.math.BigDecimal arppu;

    /**
     * 新增注册玩家数
     */
    @Excel(name = "新增玩家", width = 15)
    private Integer addNum;

    /**
     * 新增注册玩家支付总额
     */
    @Excel(name = "新增充值金额", width = 15)
    private java.math.BigDecimal addPayAmount;

    /**
     * 新增注册玩家支付数
     */
    @Excel(name = "新增付费数", width = 15)
    private Integer addPayNum;

    /**
     * 新增注册玩家支付率
     */
    @Excel(name = "新增付费率", width = 15)
    private java.math.BigDecimal addPayRate;

    /**
     * 新增注册arpu
     */
    @Excel(name = "新增ARPU", width = 15)
    private java.math.BigDecimal addArpu;

    /**
     * 新增注册arppu
     */
    @Excel(name = "新增ARPPU", width = 15)
    private java.math.BigDecimal addArppu;

    /**
     * 新增注册二次付费玩家数
     */
    //@Excel(name = "新增注册二次付费玩家数", width = 15)
    private Integer doublePay;

    /**
     * 二次付费率
     */
    //@Excel(name = "二次付费率", width = 15)
    private java.math.BigDecimal doublePayRate;

    /**
     * 统计日期
     */
    @Excel(name = "统计日期", width = 15, format = TimeConstant.DEFAULT_DATE_FORMAT)
    @JsonFormat(timezone = TimeConstant.DEFAULT_TIMEZONE, pattern = TimeConstant.DEFAULT_DATE_FORMAT)
    @DateTimeFormat(pattern = TimeConstant.DEFAULT_DATE_FORMAT)
    private java.util.Date countDate;

    /**
     * 创建时间
     */
    //@Excel(name = "创建时间", width = 20, format = TimeConstant.DEFAULT_TIME_FORMAT)
    @JsonFormat(timezone = TimeConstant.DEFAULT_TIMEZONE, pattern = TimeConstant.DEFAULT_TIME_FORMAT)
    @DateTimeFormat(pattern = TimeConstant.DEFAULT_TIME_FORMAT)
    private java.util.Date createTime;

    /**
     * 老玩家数
     */
    @Excel(name = "老玩家数", width = 15)
    private Integer oldNum;

    /**
     * 老玩家付费数
     */
    @Excel(name = "老玩家付费数", width = 15)
    private Integer oldPayNum;

    /**
     * 老玩家付费率
     */
    @Excel(name = "老玩家付费率", width = 15)
    private BigDecimal oldPayRate;

    /**
     * 老玩家充值金额
     */
    @Excel(name = "老玩家充值金额", width = 15)
    private BigDecimal oldPayAmount;

    /**
     * 老玩家ARPU
     */
    @Excel(name = "老玩家ARPU", width = 15)
    private BigDecimal oldArpu;

    /**
     * 老玩家ARPPU
     */
    @Excel(name = "老玩家ARPPU", width = 15)
    private BigDecimal oldArppu;

    /**
     * 将总和统计到该字段中
     */
    private GameDataReportCount child;

    /**
     * 时间字符串
     */
    private String dateStr;

}

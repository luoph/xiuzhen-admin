package org.jeecg.modules.game.entity;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.constant.TimeConstant;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description game_stat_order
 * @date 2021-04-20
 */
@Data
@TableName("game_stat_order")
@Accessors(chain = true)
public class GameStatOrder implements Serializable {

    private static final long serialVersionUID = 1L;

    public GameStatOrder() {}

    public GameStatOrder(String date, int serverId, int ServerNum, int payNum, BigDecimal amount){
        this.date = date;
        this.serverId = serverId;
        this.serverNum = ServerNum;
        this.payNum = payNum;
        this.amount = amount;
    }

    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private java.lang.Long id;

    /**
     * 列表统计日期
     */
    @Excel(name = "统计日期", width = 15)
    private java.lang.String date;

    /**
     * 渠道id
     */
    @ExcelIgnore
    private java.lang.Integer channelId;

    /**
     * 服务器id
     */
    @ExcelIgnore
    private java.lang.Integer serverId;

    /**
     * 区服数
     */
    @Excel(name = "区服数", width = 15)
    private java.lang.Integer serverNum;

    /**
     * 活跃角色数
     */
    @Excel(name = "活跃角色数", width = 15)
    private java.lang.Integer activeNum;

    /**
     * 付费角色数
     */
    @Excel(name = "付费角色数", width = 15)
    private java.lang.Integer payNum;

    /**
     * 付费金额
     */
    @Excel(name = "付费金额", width = 15, numFormat = CommonConstant.BIG_DECIMAL_NUMBER_FORMAT)
    private java.math.BigDecimal amount;

    /**
     * 付费率
     */
    @Excel(name = "付费率", width = 15, suffix = "%")
    private java.lang.String payRate;

    /**
     * 每用户平均收入
     */
    @Excel(name = "arpu", width = 15, numFormat = CommonConstant.BIG_DECIMAL_NUMBER_FORMAT)
    private java.math.BigDecimal arpu;

    /**
     * 每付费用户平均收益
     */
    @Excel(name = "arppu", width = 15, numFormat = CommonConstant.BIG_DECIMAL_NUMBER_FORMAT)
    private java.math.BigDecimal arppu;

    /**
     * 服均充值
     */
    @Excel(name = "服均充值", width = 15, numFormat = CommonConstant.BIG_DECIMAL_NUMBER_FORMAT)
    private java.math.BigDecimal serverAverageAmount;

    /**
     * 统计开始日期
     */
    @ExcelIgnore
    @TableField(exist = false)
    @JsonFormat(timezone = TimeConstant.DEFAULT_TIMEZONE, pattern = TimeConstant.DEFAULT_DATE_FORMAT)
    @DateTimeFormat(pattern = TimeConstant.DEFAULT_DATE_FORMAT)
    private java.util.Date startDate;

    /**
     * 统计结束日期
     */
    @ExcelIgnore
    @TableField(exist = false)
    @JsonFormat(timezone = TimeConstant.DEFAULT_TIMEZONE, pattern = TimeConstant.DEFAULT_DATE_FORMAT)
    @DateTimeFormat(pattern = TimeConstant.DEFAULT_DATE_FORMAT)
    private java.util.Date endDate;

    /**
     * 快速统计日期范围
     */
    @ExcelIgnore
    private java.lang.Integer daysType;
}

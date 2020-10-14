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
 * @description daily_gift_package_buy
 * @date 2020-09-30
 */
@Data
@TableName("daily_gift_package_buy")
@Accessors(chain = true)
public class DailyGiftPackageBuy implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 自增id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 玩家id
     */
    @Excel(name = "玩家id", width = 15)
    private Long playerId;

    /**
     * 礼包id
     */
    @Excel(name = "礼包id", width = 15)
    private Integer giftPackageId;

    /**
     * 购买日期
     */
    @Excel(name = "购买日期", width = 20, format = TimeConstant.DEFAULT_TIME_FORMAT)
    @JsonFormat(timezone = TimeConstant.DEFAULT_TIMEZONE, pattern = TimeConstant.DEFAULT_TIME_FORMAT)
    @DateTimeFormat(pattern = TimeConstant.DEFAULT_TIME_FORMAT)
    private java.util.Date buyDate;

    /**
     * buyTimes
     */
    @Excel(name = "buyTimes", width = 15)
    private Integer buyTimes;

    /**
     * 奖励物品
     */
    @Excel(name = "奖励物品", width = 15)
    private String reward;

    /**
     * 充值金额
     */
    @Excel(name = "充值金额", width = 15)
    private java.math.BigDecimal rechargeAmount;

    /**
     * 创建时间
     */
    @Excel(name = "创建时间", width = 20, format = TimeConstant.DEFAULT_TIME_FORMAT)
    @JsonFormat(timezone = TimeConstant.DEFAULT_TIMEZONE, pattern = TimeConstant.DEFAULT_TIME_FORMAT)
    @DateTimeFormat(pattern = TimeConstant.DEFAULT_TIME_FORMAT)
    private java.util.Date createTime;

    /**
     * 更新时间
     */
    @Excel(name = "更新时间", width = 15, format = TimeConstant.DEFAULT_DATE_FORMAT)
    @JsonFormat(timezone = TimeConstant.DEFAULT_TIMEZONE, pattern = TimeConstant.DEFAULT_DATE_FORMAT)
    @DateTimeFormat(pattern = TimeConstant.DEFAULT_DATE_FORMAT)
    private java.util.Date updateTime;
}

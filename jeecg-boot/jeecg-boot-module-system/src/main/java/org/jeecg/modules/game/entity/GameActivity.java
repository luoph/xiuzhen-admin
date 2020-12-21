package org.jeecg.modules.game.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecg.common.constant.TimeConstant;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 活动
 * @date 2020-01-12
 */
@Data
@TableName("game_activity")
@Accessors(chain = true)
public class GameActivity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 活动id
     */
    @TableId(type = IdType.AUTO)
    private java.lang.Long id;

    /**
     * 活动唯一标识, e.g. "buff", "worldboss"
     */
    @Excel(name = "活动唯一标识", width = 15)
    private java.lang.String activity;

    /**
     * 活动名称
     */
    @Excel(name = "活动名称", width = 15)
    private java.lang.String name;

    /**
     * 活动标语
     */
    @Excel(name = "活动标语", width = 15)
    private java.lang.String slogan;

    /**
     * 活动入口的icon
     */
    @Excel(name = "活动icon", width = 15)
    private java.lang.String icon;

    /**
     * 活动状态: 0.关闭, 1.开启
     */
    @Excel(name = "活动状态", width = 15)
    @Dict(dicCode = "valid_status")
    private java.lang.Integer status;

    /**
     * 开始时的传闻id
     */
    @Excel(name = "开始时的传闻id", width = 15)
    private java.lang.Integer startRumor;

    /**
     * 结束时的传闻id
     */
    @Excel(name = "结束时的传闻id", width = 15)
    private java.lang.Integer endRumor;

    /**
     * 0：图标常驻, 1：预告时才显示，平时隐藏
     */
    @Excel(name = "图标显示状态", width = 15)
    private java.lang.Integer iconDisplay;

    /**
     * 提前预告时间（秒）
     */
    @Excel(name = "提前预告时间(秒)", width = 15)
    private java.lang.Integer noticeTime;

    /**
     * 跑马灯显示周期(秒)，0表示不显示跑马灯
     */
    @Excel(name = "跑马灯显示周期(秒)", width = 15)
    private java.lang.Integer noticePeriod;

    /**
     * 开始时间
     */
    @Excel(name = "开始时间", width = 15, format = TimeConstant.DEFAULT_TIME_FORMAT)
    @JsonFormat(timezone = TimeConstant.DEFAULT_TIMEZONE, pattern = TimeConstant.DEFAULT_TIME_FORMAT)
    @DateTimeFormat(pattern = TimeConstant.DEFAULT_TIME_FORMAT)
    private java.util.Date startTime;

    /**
     * 结束时间
     */
    @Excel(name = "结束时间", width = 15, format = TimeConstant.DEFAULT_TIME_FORMAT)
    @JsonFormat(timezone = TimeConstant.DEFAULT_TIMEZONE, pattern = TimeConstant.DEFAULT_TIME_FORMAT)
    @DateTimeFormat(pattern = TimeConstant.DEFAULT_TIME_FORMAT)
    private java.util.Date endTime;

    /**
     * 自定义json参数. e.g. {"buff":"1,2"}
     */
    @Excel(name = "自定义json参数", width = 15)
    private java.lang.String custom;

    /**
     * 备注
     */
    @Excel(name = "备注", width = 15)
    private java.lang.String remark;

    /**
     * 创建时间
     */
    @Excel(name = "创建时间", width = 15, format = TimeConstant.DEFAULT_TIME_FORMAT)
    @JsonFormat(timezone = TimeConstant.DEFAULT_TIMEZONE, pattern = TimeConstant.DEFAULT_TIME_FORMAT)
    @DateTimeFormat(pattern = TimeConstant.DEFAULT_TIME_FORMAT)
    private java.util.Date createTime;

    /**
     * 更新时间
     */
    @Excel(name = "更新时间", width = 15, format = TimeConstant.DEFAULT_TIME_FORMAT)
    @JsonFormat(timezone = TimeConstant.DEFAULT_TIMEZONE, pattern = TimeConstant.DEFAULT_TIME_FORMAT)
    @DateTimeFormat(pattern = TimeConstant.DEFAULT_TIME_FORMAT)
    private java.util.Date updateTime;
}

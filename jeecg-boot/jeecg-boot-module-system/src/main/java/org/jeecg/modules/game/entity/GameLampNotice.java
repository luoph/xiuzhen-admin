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
 * @description game_lamp_notice
 * @date 2020-08-10
 */
@Data
@TableName("game_lamp_notice")
@Accessors(chain = true)
public class GameLampNotice implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 公告id
     */
    @TableId(type = IdType.AUTO)
    private java.lang.Long id;

    /**
     * 标题
     */
    @Excel(name = "标题", width = 15)
    private java.lang.String noticeTitle;

    /**
     * 正文
     */
    @Excel(name = "正文", width = 15)
    private java.lang.String noticeText;

    /**
     * 投放服务器
     */
    @Excel(name = "投放服务器", width = 15)
    private java.lang.String gameServerList;

    /**
     * 播放频率
     */
    @Excel(name = "播放频率", width = 15)
    private java.lang.Integer frequency;

    /**
     * 循环播放周期
     */
    @Excel(name = "循环播放周期", width = 15)
    private java.lang.Integer cyclePeriod;

    /**
     * 状态 0-关闭 1-开启
     */
    @Excel(name = "状态", width = 15)
    private java.lang.Integer status;

    /**
     * 开始时间
     */
    @Excel(name = "开始时间", width = 20, format = TimeConstant.DEFAULT_TIME_FORMAT)
    @JsonFormat(timezone = TimeConstant.DEFAULT_TIMEZONE, pattern = TimeConstant.DEFAULT_TIME_FORMAT)
    @DateTimeFormat(pattern = TimeConstant.DEFAULT_TIME_FORMAT)
    private java.util.Date beginTime;

    /**
     * 结束时间
     */
    @Excel(name = "结束时间", width = 20, format = TimeConstant.DEFAULT_TIME_FORMAT)
    @JsonFormat(timezone = TimeConstant.DEFAULT_TIMEZONE, pattern = TimeConstant.DEFAULT_TIME_FORMAT)
    @DateTimeFormat(pattern = TimeConstant.DEFAULT_TIME_FORMAT)
    private java.util.Date endTime;

    /**
     * lastSendTime
     */
    @Excel(name = "lastSendTime", width = 15, format = TimeConstant.DEFAULT_DATE_FORMAT)
    @JsonFormat(timezone = TimeConstant.DEFAULT_TIMEZONE, pattern = TimeConstant.DEFAULT_DATE_FORMAT)
    @DateTimeFormat(pattern = TimeConstant.DEFAULT_DATE_FORMAT)
    private java.util.Date lastSendTime;

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

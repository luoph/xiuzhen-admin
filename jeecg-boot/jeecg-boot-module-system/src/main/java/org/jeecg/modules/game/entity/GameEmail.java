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
 * @description 游戏下发邮件
 * @date 2020-01-04
 */
@Data
@TableName("game_email")
@Accessors(chain = true)
public class GameEmail implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主id
     */
    @TableId(type = IdType.AUTO)
    private java.lang.Integer id;

    /**
     * 邮件标题
     */
    @Excel(name = "邮件标题", width = 15)
    private java.lang.String title;

    /**
     * 邮件描述
     */
    @Excel(name = "邮件描述", width = 15)
    private java.lang.String descri;

    /**
     * 1有奖励附件content不为空 2无奖励附件contents是null
     */
    @Excel(name = "1有奖励附件content不为空 2无奖励附件contents是null", width = 15)
    private java.lang.Integer emailType;

    /**
     * 附件内容
     */
    @Excel(name = "附件内容", width = 15)
    private java.lang.String content;

    /**
     * 1有效 0无效
     */
    @Excel(name = "1有效 0无效", width = 15)
    private java.lang.Integer validState;

    /**
     * 目标主体类型1玩家 2服务器
     */
    @Excel(name = "目标主体类型1玩家 2服务器", width = 15)
    private java.lang.Integer targetBodyType;

    /**
     * 目标主体id playerId serverId
     */
    @Excel(name = "目标主体id playerId serverId", width = 15)
    private java.lang.Integer targetBodyId;

    /**
     * 向目标主体发送的时间
     */
    @Excel(name = "向目标主体发送的时间", width = 15, format = TimeConstant.DEFAULT_DATE_FORMAT)
    @JsonFormat(timezone = TimeConstant.DEFAULT_TIMEZONE, pattern = TimeConstant.DEFAULT_DATE_FORMAT)
    @DateTimeFormat(pattern = TimeConstant.DEFAULT_DATE_FORMAT)
    private java.util.Date sendTime;

    /**
     * 有效日期开始
     */
    @Excel(name = "有效日期开始", width = 15, format = TimeConstant.DEFAULT_DATE_FORMAT)
    @JsonFormat(timezone = TimeConstant.DEFAULT_TIMEZONE, pattern = TimeConstant.DEFAULT_DATE_FORMAT)
    @DateTimeFormat(pattern = TimeConstant.DEFAULT_DATE_FORMAT)
    private java.util.Date validStarTime;

    /**
     * 有效日期结束
     */
    @Excel(name = "有效日期结束", width = 15, format = TimeConstant.DEFAULT_DATE_FORMAT)
    @JsonFormat(timezone = TimeConstant.DEFAULT_TIMEZONE, pattern = TimeConstant.DEFAULT_DATE_FORMAT)
    @DateTimeFormat(pattern = TimeConstant.DEFAULT_DATE_FORMAT)
    private java.util.Date validEndTime;

    /**
     * 创建者
     */
    @Excel(name = "创建者", width = 15)
    private java.lang.String createBy;

    /**
     * 创建时间
     */
    @Excel(name = "创建时间", width = 15, format = TimeConstant.DEFAULT_DATE_FORMAT)
    @JsonFormat(timezone = TimeConstant.DEFAULT_TIMEZONE, pattern = TimeConstant.DEFAULT_DATE_FORMAT)
    @DateTimeFormat(pattern = TimeConstant.DEFAULT_DATE_FORMAT)
    private java.util.Date createTime;

    /**
     * 更新着
     */
    @Excel(name = "更新着", width = 15)
    private java.lang.String updateBy;

    /**
     * 更新时间
     */
    @Excel(name = "更新时间", width = 15, format = TimeConstant.DEFAULT_DATE_FORMAT)
    @JsonFormat(timezone = TimeConstant.DEFAULT_TIMEZONE, pattern = TimeConstant.DEFAULT_DATE_FORMAT)
    @DateTimeFormat(pattern = TimeConstant.DEFAULT_DATE_FORMAT)
    private java.util.Date updateTime;
}

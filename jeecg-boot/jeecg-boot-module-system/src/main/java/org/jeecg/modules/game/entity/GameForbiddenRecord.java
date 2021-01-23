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
 * @description 封禁记录
 * @date 2021-01-23
 */
@Data
@TableName("game_forbidden_record")
@Accessors(chain = true)
public class GameForbiddenRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private java.lang.Long id;

    /**
     * 执行的增删改操作类型
     */
    @Excel(name = "执行的增删改操作类型", width = 15)
    private java.lang.String operation;

    /**
     * 封号禁言表id
     */
    @Excel(name = "封号禁言表id", width = 15)
    private java.lang.Long forbiddenId;

    /**
     * 服务器id
     */
    @Excel(name = "服务器id", width = 15)
    private java.lang.Integer serverId;

    /**
     * 1-登录 2-聊天
     */
    @Excel(name = "1-登录 2-聊天", width = 15)
    private java.lang.Integer type;

    /**
     * playerId/ip/deviceId
     */
    @Excel(name = "playerId/ip/deviceId", width = 15)
    private java.lang.String banKey;

    /**
     * 对应 ban_type 的值
     */
    @Excel(name = "对应 ban_type 的值", width = 15)
    private java.lang.String banValue;

    /**
     * 封禁原因
     */
    @Excel(name = "封禁原因", width = 15)
    private java.lang.String reason;

    /**
     * 0-临时 1-永久
     */
    @Excel(name = "0-临时 1-永久", width = 15)
    private java.lang.Integer isForever;

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
     * 创建时间
     */
    @Excel(name = "创建时间", width = 15, format = TimeConstant.DEFAULT_TIME_FORMAT)
    @JsonFormat(timezone = TimeConstant.DEFAULT_TIMEZONE, pattern = TimeConstant.DEFAULT_TIME_FORMAT)
    @DateTimeFormat(pattern = TimeConstant.DEFAULT_TIME_FORMAT)
    private java.util.Date createTime;

    /**
     * 操作人
     */
    @Excel(name = "操作人", width = 15)
    private java.lang.String createBy;
}

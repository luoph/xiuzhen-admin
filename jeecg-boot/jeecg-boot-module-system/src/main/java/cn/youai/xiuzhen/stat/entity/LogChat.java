package cn.youai.xiuzhen.stat.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
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
 * @Description: 聊天日志
 * @Author: jeecg-boot
 * @Date: 2022-12-15
 * @Version: V1.0
 */
@Data
@TableName("log_chat")
@Accessors(chain = true)
public class LogChat implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 自增id
     */
    @TableId(type = IdType.AUTO)
    private java.lang.Long id;
    /**
     * 来源区服
     */
    @Excel(name = "来源区服", width = 15)
    private java.lang.Integer serverId;
    /**
     * 发送者id，为0表示系统消息
     */
    @Excel(name = "发送者id，为0表示系统消息", width = 15)
    private java.lang.Long senderId;
    /**
     * 发送者名称
     */
    @Excel(name = "发送者名称", width = 15)
    private java.lang.String senderName;
    /**
     * 接收者id
     */
    @Excel(name = "接收者id", width = 15)
    private java.lang.Long receiverId;
    /**
     * 接收者名称
     */
    @Excel(name = "接收者名称", width = 15)
    private java.lang.String receiverName;
    /**
     * 聊天类型 1-世界聊天 2-私聊 3-仙盟聊天 4-跨服聊天
     */
    @Excel(name = "聊天类型", width = 15)
    private java.lang.Integer chatType;
    /**
     * 消息类型 1-普通文本 2-修真日志 3-分享
     */
    @Excel(name = "消息类型", width = 15)
    private java.lang.Integer msgType;
    /**
     * 消息内容
     */
    @Excel(name = "消息内容", width = 15)
    private java.lang.String msgContent;

    /**
     * 创建时间
     */
    @Excel(name = "创建时间", width = 15, format = TimeConstant.DEFAULT_TIME_FORMAT)
    @JsonFormat(timezone = TimeConstant.DEFAULT_TIMEZONE, pattern = TimeConstant.DEFAULT_TIME_FORMAT)
    @DateTimeFormat(pattern = TimeConstant.DEFAULT_TIME_FORMAT)
    private java.util.Date createTime;

    /**
     * 创建日期
     */
    @Excel(name = "创建日期", width = 15, format = TimeConstant.DEFAULT_DATE_FORMAT)
    @JsonFormat(timezone = TimeConstant.DEFAULT_TIMEZONE, pattern = TimeConstant.DEFAULT_DATE_FORMAT)
    @DateTimeFormat(pattern = TimeConstant.DEFAULT_DATE_FORMAT)
    private java.util.Date createDate;

    @TableField(exist = false)
    private String account;

    @TableField(exist = false)
    private Integer level;

    @TableField(exist = false)
    private String channel;

    @TableField(exist = false)
    private String sdkChannel;
}

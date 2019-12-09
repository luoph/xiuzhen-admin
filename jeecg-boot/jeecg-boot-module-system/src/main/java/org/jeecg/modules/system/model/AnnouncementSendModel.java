package org.jeecg.modules.system.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.jeecg.common.constant.TimeConstant;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 用户通告阅读标记表
 * @date 2019-02-21
 */
@Data
@TableName("sys_announcement_send")
public class AnnouncementSendModel implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(type = IdType.ID_WORKER_STR)
    private java.lang.String id;
    /**
     * 通告id
     */
    private java.lang.String anntId;
    /**
     * 用户id
     */
    private java.lang.String userId;
    /**
     * 标题
     */
    private java.lang.String title;
    /**
     * 内容
     */
    private java.lang.String msgContent;
    /**
     * 发布人
     */
    private java.lang.String sender;
    /**
     * 优先级（L低，M中，H高）
     */
    private java.lang.String priority;
    /**
     * 阅读状态
     */
    private java.lang.String readFlag;
    /**
     * 发布时间
     */
    @JsonFormat(timezone = TimeConstant.DEFAULT_TIMEZONE, pattern = TimeConstant.DEFAULT_TIME_FORMAT)
    @DateTimeFormat(pattern = TimeConstant.DEFAULT_TIME_FORMAT)
    private java.util.Date sendTime;
    /**
     * 页数
     */
    private java.lang.Integer pageNo;
    /**
     * 大小
     */
    private java.lang.Integer pageSize;
    /**
     * 消息类型1:通知公告2:系统消息
     */
    private java.lang.String msgCategory;
}

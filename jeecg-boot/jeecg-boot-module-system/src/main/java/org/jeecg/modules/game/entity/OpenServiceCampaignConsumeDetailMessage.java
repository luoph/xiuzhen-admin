package org.jeecg.modules.game.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecg.common.constant.TimeConstant;
import org.jeecg.common.system.base.entity.BaseEntity;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 开服活动消耗传闻
 * @date 2020-12-28
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@TableName("game_open_service_campaign_consume_detail_message")
public class OpenServiceCampaignConsumeDetailMessage extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private java.lang.Integer id;

    /**
     * 开服活动id, open_service_campaign.id
     */
    @Excel(name = "开服活动id", width = 15)
    private java.lang.Integer campaignId;

    /**
     * open_service_campaign_type.id
     */
    @Excel(name = "页签id", width = 15)
    private java.lang.Integer campaignTypeId;

    /**
     * open_service_campaign_consume_detail.id
     */
    @Excel(name = "详情id", width = 15)
    private java.lang.Integer consumeDetailId;

    /**
     * 传闻推送时间
     */
    @Excel(name = "传闻推送时间", width = 15, format = TimeConstant.DEFAULT_TIME_FORMAT)
    @JsonFormat(timezone = TimeConstant.DEFAULT_TIMEZONE, pattern = TimeConstant.DEFAULT_TIME_FORMAT)
    @DateTimeFormat(pattern = TimeConstant.DEFAULT_TIME_FORMAT)
    private java.util.Date sendTime;

    /**
     * 传闻内容
     */
    @Excel(name = "传闻内容", width = 15)
    private java.lang.String message;

    /**
     * 传闻次数
     */
    @Excel(name = "传闻次数", width = 15)
    private java.lang.Integer num;

    /**
     * 是否发送邮件
     */
    @Excel(name = "是否发送邮件", width = 15)
    private java.lang.Integer email;
}
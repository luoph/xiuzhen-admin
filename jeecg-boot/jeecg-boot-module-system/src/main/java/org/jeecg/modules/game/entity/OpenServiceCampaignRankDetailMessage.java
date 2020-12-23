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
 * @description 开服活动-开服排行-活动明细-传闻
 * @date 2020-12-21
 */
@Data
@TableName("game_open_service_campaign_rank_detail_message")
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class OpenServiceCampaignRankDetailMessage extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private java.lang.Long id;

    /**
     * 开服活动id
     */
    @Excel(name = "开服活动id", width = 15)
    private java.lang.Long campaignId;

    /**
     * typeId
     */
    @Excel(name = "typeId", width = 15)
    private java.lang.Long campaignTypeId;

    /**
     * open_service_campaign_rank_detail.id
     */
    @Excel(name = "rankDetailId", width = 15)
    private java.lang.Long rankDetailId;

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
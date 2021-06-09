package org.jeecg.modules.game.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jeecg.common.constant.TimeConstant;
import org.jeecg.common.system.base.entity.BaseEntity;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 开服互动消耗配置
 * @date 2020-12-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("game_open_service_campaign_consume_detail")
public class OpenServiceCampaignConsumeDetail extends BaseEntity {

    private static final long serialVersionUID = 1L;

    public OpenServiceCampaignConsumeDetail() {
    }

    public OpenServiceCampaignConsumeDetail(OpenServiceCampaignConsumeDetail other) {
//        this.id = other.id;
//        this.campaignId = other.campaignId;
//        this.campaignTypeId = other.campaignTypeId;
        this.timeType = other.timeType;
        this.startTime = other.startTime;
        this.endTime = other.endTime;
        this.startDay = other.startDay;
        this.duration = other.duration;
        this.tabName = other.tabName;
        this.name = other.name;
        this.banner = other.banner;
        this.consumeRewardEmailTitle = other.consumeRewardEmailTitle;
        this.consumeRewardEmailContent = other.consumeRewardEmailContent;
        this.helpMsg = other.helpMsg;
    }

    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private java.lang.Long id;

    /**
     * 开服活动id, open_service_campaign.id
     */
    @ExcelProperty("开服活动id")
    @Excel(name = "开服活动id", width = 15)
    private java.lang.Long campaignId;

    /**
     * open_service_campaign_type.id
     */
    @ExcelProperty("页签id")
    @Excel(name = "页签id", width = 15)
    private java.lang.Long campaignTypeId;

    /**
     * 时间类型: 1.具体时间范围, 2.开服第N天
     */
    @Excel(name = "时间类型", width = 15)
    private Integer timeType;

    /**
     * 开始时间
     */
    @Excel(name = "开始时间", width = 15, format = TimeConstant.DEFAULT_TIME_FORMAT)
    @JsonFormat(timezone = TimeConstant.DEFAULT_TIMEZONE, pattern = TimeConstant.DEFAULT_TIME_FORMAT)
    @DateTimeFormat(pattern = TimeConstant.DEFAULT_TIME_FORMAT)
    private Date startTime;

    /**
     * 结束时间
     */
    @Excel(name = "结束时间", width = 15, format = TimeConstant.DEFAULT_TIME_FORMAT)
    @JsonFormat(timezone = TimeConstant.DEFAULT_TIMEZONE, pattern = TimeConstant.DEFAULT_TIME_FORMAT)
    @DateTimeFormat(pattern = TimeConstant.DEFAULT_TIME_FORMAT)
    private Date endTime;

    /**
     * 开始时间(开服第n天, e.g. 0表示开服第1天)
     */
    @ExcelProperty("开始天数(开服第n天)")
    @Excel(name = "开始天数", width = 15)
    private java.lang.Integer startDay;

    /**
     * 持续时间(天)
     */
    @ExcelProperty("持续时间(天)")
    @Excel(name = "持续时间(天)", width = 15)
    private java.lang.Integer duration;

    /**
     * 活动页签名称
     */
    @ExcelProperty("页签名称")
    @Excel(name = "页签名称", width = 15)
    private java.lang.String tabName;

    /**
     * 活动名称
     */
    @ExcelProperty("活动名称")
    @Excel(name = "活动名称", width = 15)
    private java.lang.String name;

    /**
     * 活动宣传背景图
     */
    @ExcelProperty("活动宣传图")
    @Excel(name = "活动宣传图", width = 15)
    private java.lang.String banner;

    /**
     * 消耗奖励邮件标题
     */
    @ExcelProperty("消耗奖励邮件标题")
    @Excel(name = "消耗奖励邮件标题", width = 15)
    private java.lang.String consumeRewardEmailTitle;

    /**
     * 消耗奖励邮件内容
     */
    @ExcelProperty("消耗奖励邮件内容")
    @Excel(name = "消耗奖励邮件内容", width = 15)
    private java.lang.String consumeRewardEmailContent;

    /**
     * 帮助信息
     */
    @ExcelProperty("帮助信息")
    @Excel(name = "帮助信息", width = 15)
    private java.lang.String helpMsg;

    @TableField(exist = false)
    private List<OpenServiceCampaignConsumeDetailItem> consumeList;

    @TableField(exist = false)
    private List<OpenServiceCampaignConsumeDetailMessage> messageList;

}

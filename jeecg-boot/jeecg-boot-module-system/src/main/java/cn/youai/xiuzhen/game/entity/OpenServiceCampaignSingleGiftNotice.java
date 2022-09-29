package cn.youai.xiuzhen.game.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jeecg.common.constant.TimeConstant;
import org.jeecg.common.system.base.entity.BaseEntity;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 开服活动-单笔好礼-传闻消息
 * @date 2020-12-23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("game_open_service_campaign_single_gift_notice")
public class OpenServiceCampaignSingleGiftNotice extends BaseEntity {

    private static final long serialVersionUID = 1L;

    public OpenServiceCampaignSingleGiftNotice() {
    }

    public OpenServiceCampaignSingleGiftNotice(OpenServiceCampaignSingleGiftNotice other) {
//        this.id = other.id;
//        this.campaignId = other.campaignId;
//        this.campaignTypeId = other.campaignTypeId;
        this.giftDetailId = other.giftDetailId;
        this.num = other.num;
        this.message = other.message;
        this.email = other.email;
        this.sendTime = other.sendTime;
    }

    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private java.lang.Long id;

    /**
     * 开服活动id
     */
    @ExcelProperty("开服活动id")
    @Excel(name = "开服活动id", width = 15)
    private java.lang.Long campaignId;

    /**
     * typeId
     */
    @ExcelProperty("typeId")
    @Excel(name = "typeId", width = 15)
    private java.lang.Long campaignTypeId;

    /**
     * open_service_single_recharge_gift_detail.id
     */
    @ExcelProperty("详情id")
    @Excel(name = "详情id", width = 15)
    private java.lang.Long giftDetailId;

    /**
     * 播放次数
     */
    @ExcelProperty("播放次数")
    @Excel(name = "播放次数", width = 15)
    private java.lang.Integer num;

    /**
     * 消息内容
     */
    @ExcelProperty("消息内容")
    @Excel(name = "消息内容", width = 15)
    private java.lang.String message;

    /**
     * 是否发送邮件
     */
    @ExcelProperty("是否发送邮件")
    @Excel(name = "是否发送邮件", width = 15)
    private java.lang.Integer email;

    /**
     * 发送时间
     */
    @ExcelProperty("发送时间")
    @com.alibaba.excel.annotation.format.DateTimeFormat("HH:mm:ss")
    @Excel(name = "发送时间", width = 15, format = TimeConstant.DEFAULT_TIME_FORMAT)
    @JsonFormat(timezone = TimeConstant.DEFAULT_TIMEZONE, pattern = TimeConstant.DEFAULT_TIME_FORMAT)
    @DateTimeFormat(pattern = TimeConstant.DEFAULT_TIME_FORMAT)
    private java.util.Date sendTime;

}

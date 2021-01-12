package org.jeecg.modules.game.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jeecg.common.system.base.entity.BaseEntity;
import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 开服活动-单比好礼-任务明细
 * @date 2020-12-23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("game_open_service_campaign_single_gift_item")
public class OpenServiceCampaignSingleGiftItem extends BaseEntity {

    private static final long serialVersionUID = 1L;

    public OpenServiceCampaignSingleGiftItem() {
    }

    public OpenServiceCampaignSingleGiftItem(OpenServiceCampaignSingleGiftItem other) {
//        this.id = other.id;
//        this.campaignId = other.campaignId;
//        this.campaignTypeId = other.campaignTypeId;
        this.giftDetailId = other.giftDetailId;
        this.amount = other.amount;
        this.limitTimes = other.limitTimes;
        this.remark = other.remark;
        this.reward = other.reward;
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
     * 页签id
     */
    @ExcelProperty("页签id")
    @Excel(name = "页签id", width = 15)
    private java.lang.Long campaignTypeId;

    /**
     * open_service_single_recharge_gift_detail.id
     */
    @ExcelProperty("详情id")
    @Excel(name = "详情id", width = 15)
    private java.lang.Long giftDetailId;

    /**
     * 任务金额
     */
    @ExcelProperty("任务金额")
    @Excel(name = "任务金额", width = 15)
    private java.math.BigDecimal amount;

    /**
     * 领取上限次数
     */
    @ExcelProperty("领取上限次数")
    @Excel(name = "领取上限次数", width = 15)
    private java.lang.Integer limitTimes;

    /**
     * 任务描述
     */
    @ExcelProperty("任务描述")
    @Excel(name = "任务描述", width = 15)
    private java.lang.String remark;

    /**
     * 奖励json
     */
    @ExcelProperty("奖励列表")
    @Excel(name = "奖励列表", width = 15)
    private java.lang.String reward;
}

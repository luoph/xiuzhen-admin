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
 * @description 开服活动消耗道具
 * @date 2020-12-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("game_open_service_campaign_consume_detail_item")
public class OpenServiceCampaignConsumeDetailItem extends BaseEntity {

    private static final long serialVersionUID = 1L;

    public OpenServiceCampaignConsumeDetailItem() {
    }

    public OpenServiceCampaignConsumeDetailItem(OpenServiceCampaignConsumeDetailItem other) {
//        this.id = other.id;
//        this.campaignId = other.campaignId;
//        this.campaignTypeId = other.campaignTypeId;
//        this.consumeDetailId = other.consumeDetailId;
        this.sort = other.sort;
        this.consumeType = other.consumeType;
        this.startDay = other.startDay;
        this.statisticsNotStart = other.statisticsNotStart;
        this.description = other.description;
        this.jump = other.jump;
        this.consumeItems = other.consumeItems;
        this.num = other.num;
        this.reward = other.reward;
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
     * open_service_campaign_consume_detail.id
     */
    @ExcelProperty("详情id")
    @Excel(name = "详情id", width = 15)
    private java.lang.Long consumeDetailId;

    /**
     * 排序
     */
    @ExcelProperty("排序")
    @Excel(name = "排序", width = 15)
    private java.lang.Integer sort;

    /**
     * 统计类型 0.个人, 1.全服
     */
    @ExcelProperty("统计类型")
    @Excel(name = "统计类型", width = 15)
    private java.lang.Integer consumeType;

    /**
     * 开始时间(子活动开始第n天, e.g. 0表示子活动开始第1天)
     */
    @ExcelProperty("开始时间(子活动开始第n天)")
    @Excel(name = "开始时间(子活动开始第n天)", width = 15)
    private java.lang.Integer startDay;

    /**
     * 开启前是否统计，全服统计默认是
     */
    @ExcelProperty("开启前是否统计")
    @Excel(name = "开启前是否统计", width = 15)
    private java.lang.Integer statisticsNotStart;

    /**
     * 描述
     */
    @ExcelProperty("描述")
    @Excel(name = "描述", width = 15)
    private java.lang.String description;

    /**
     * 跳转
     */
    @ExcelProperty("跳转")
    @Excel(name = "跳转", width = 15)
    private java.lang.String jump;

    /**
     * 消耗道具 e.g. [1001, 1002, 1003]
     */
    @ExcelProperty("消耗道具")
    @Excel(name = "消耗道具", width = 15)
    private java.lang.String consumeItems;

    /**
     * 总数量
     */
    @ExcelProperty("总数量")
    @Excel(name = "总数量", width = 15)
    private java.lang.Integer num;

    /**
     * 奖励列表 e.g. [{"itemId":1001, "num":1}]
     */
    @ExcelProperty("奖励列表")
    @Excel(name = "奖励列表", width = 15)
    private java.lang.String reward;
}

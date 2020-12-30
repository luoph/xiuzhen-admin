package org.jeecg.modules.game.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecg.common.system.base.entity.BaseEntity;
import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 开服夺宝积分
 * @date 2020-12-25
 */
@Data
@TableName("game_open_service_campaign_lottery_detail_score")
@EqualsAndHashCode(callSuper = true)
public class OpenServiceCampaignLotteryDetailScore extends BaseEntity {

    private static final long serialVersionUID = 1L;

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
     * open_service_campaign_lottery_detail.id
     */
    @ExcelProperty("详情id")
    @Excel(name = "详情id", width = 15)
    private java.lang.Long lotteryDetailId;

    /**
     * 积分
     */
    @ExcelProperty("积分")
    @Excel(name = "积分", width = 15)
    private java.lang.Integer score;

    /**
     * 奖励列表 e.g. [{"itemId":1001, "num":1}]
     */
    @ExcelProperty("奖励列表")
    @Excel(name = "奖励列表", width = 15)
    private java.lang.String reward;
}

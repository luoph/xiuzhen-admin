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
 * @description 开服夺宝榜单
 * @date 2020-12-25
 */
@Data
@TableName("game_open_service_campaign_lottery_detail_ranking")
@EqualsAndHashCode(callSuper = true)
public class OpenServiceCampaignLotteryDetailRanking extends BaseEntity {

    private static final long serialVersionUID = 1L;

    public OpenServiceCampaignLotteryDetailRanking() {
    }

    public OpenServiceCampaignLotteryDetailRanking(OpenServiceCampaignLotteryDetailRanking other) {
//        this.id = other.id;
//        this.campaignId = other.campaignId;
//        this.campaignTypeId = other.campaignTypeId;
//        this.lotteryDetailId = other.lotteryDetailId;
        this.minRank = other.minRank;
        this.maxRank = other.maxRank;
        this.score = other.score;
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
    @ExcelProperty("ypeId")
    @Excel(name = "typeId", width = 15)
    private java.lang.Long campaignTypeId;

    /**
     * 页签详情id
     */
    @ExcelProperty("页签详情id")
    @Excel(name = "页签详情id", width = 15)
    private java.lang.Long lotteryDetailId;

    /**
     * 排名最小值
     */
    @ExcelProperty("排名最小值")
    @Excel(name = "排名最小值", width = 15)
    private java.lang.Integer minRank;

    /**
     * 排名最大值
     */
    @ExcelProperty("排名最大值")
    @Excel(name = "排名最大值", width = 15)
    private java.lang.Integer maxRank;

    /**
     * 上榜最低积分
     */
    @ExcelProperty("上榜最低积分")
    @Excel(name = "上榜最低积分", width = 15)
    private java.lang.Integer score;

    /**
     * 奖励列表 e.g. [{"itemId":1001, "num":1}]
     */
    @ExcelProperty("奖励列表")
    @Excel(name = "奖励列表", width = 15)
    private java.lang.String reward;

}

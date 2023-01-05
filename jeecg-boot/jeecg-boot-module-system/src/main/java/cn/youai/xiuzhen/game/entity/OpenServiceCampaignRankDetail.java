package cn.youai.xiuzhen.game.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jeecgframework.poi.excel.annotation.Excel;

import java.util.List;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 开服活动-开服排行-活动明细(3级)
 * @date 2020-12-21
 */
@Data
@TableName("game_open_service_campaign_rank_detail")
@EqualsAndHashCode(callSuper = true)
public class OpenServiceCampaignRankDetail extends OpenServiceCampaignDetail {

    private static final long serialVersionUID = 1L;

    public OpenServiceCampaignRankDetail() {
    }

    public OpenServiceCampaignRankDetail(OpenServiceCampaignRankDetail other) {
//        this.id = other.id;
//        this.campaignId = other.campaignId;
//        this.campaignTypeId = other.campaignTypeId;
        super.setName(other.getName());
        super.setTabName(other.getTabName());
        super.setBanner(other.getBanner());
        super.setHelpMsg(other.getHelpMsg());
        super.setTimeType(other.getTimeType());
        super.setStartTime(other.getStartTime());
        super.setEndTime(other.getEndTime());
        super.setStartDay(other.getStartDay());
        super.setDuration(other.getDuration());
        this.rankType = other.rankType;
        this.sort = other.sort;
        this.rewardImg = other.rewardImg;
        this.combatPower = other.combatPower;
        this.rankNum = other.rankNum;
        this.rankRewardEmail = other.rankRewardEmail;
        this.standardRewardEmail = other.standardRewardEmail;
        this.jump = other.jump;
    }

    /**
     * 排行类型 open_service_campaign_rank_type.rank_type e.g. 1.境界冲榜, 2.功法冲榜
     */
    @ExcelProperty("排行类型")
    @Excel(name = "排行类型", width = 15)
    private java.lang.Integer rankType;

    /**
     * 排序
     */
    @ExcelProperty("排序")
    @Excel(name = "排序", width = 15)
    private java.lang.Integer sort;

    /**
     * 活动宣传奖励图
     */
    @ExcelProperty("活动宣传奖励图")
    @Excel(name = "活动宣传奖励图", width = 15)
    private java.lang.String rewardImg;

    /**
     * 活动宣传仙力
     */
    @ExcelProperty("活动宣传仙力")
    @Excel(name = "活动宣传仙力", width = 15)
    private java.lang.Long combatPower;

    /**
     * 排行玩家数量
     */
    @ExcelProperty("排行玩家数量")
    @Excel(name = "排行玩家数量", width = 15)
    private java.lang.Integer rankNum;

    /**
     * 排名奖励邮件id
     */
    @ExcelProperty("排名奖励邮件id")
    @Excel(name = "排名奖励邮件id", width = 15)
    private java.lang.Long rankRewardEmail;

    /**
     * 达标奖励邮件id
     */
    @ExcelProperty("达标奖励邮件id")
    @Excel(name = "达标奖励邮件id", width = 15)
    private java.lang.Long standardRewardEmail;

    /**
     * 跳转id
     */
    @ExcelProperty("跳转id")
    @Excel(name = "跳转id", width = 15)
    private java.lang.String jump;

    @TableField(exist = false)
    private List<OpenServiceCampaignRankDetailMessage> messageList;

    @TableField(exist = false)
    private List<OpenServiceCampaignRankDetailRanking> rankingList;

    @TableField(exist = false)
    private List<OpenServiceCampaignRankDetailScore> scoreList;

    @TableField(exist = false)
    private List<OpenServiceCampaignRankDetailStandard> standardList;

}

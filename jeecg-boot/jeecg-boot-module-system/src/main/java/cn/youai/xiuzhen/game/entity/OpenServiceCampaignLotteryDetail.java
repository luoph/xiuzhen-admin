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
 * @description 开服夺宝详情
 * @date 2020-12-25
 */
@Data
@TableName("game_open_service_campaign_lottery_detail")
@EqualsAndHashCode(callSuper = true)
public class OpenServiceCampaignLotteryDetail extends OpenServiceCampaignDetail {

    private static final long serialVersionUID = 1L;

    public OpenServiceCampaignLotteryDetail() {
    }

    public OpenServiceCampaignLotteryDetail(OpenServiceCampaignLotteryDetail other) {
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
        this.skeleton = other.skeleton;
        this.freeNum = other.freeNum;
        this.rewardRecordNum = other.rewardRecordNum;
        this.rewardRecordMsg = other.rewardRecordMsg;
        this.rewardMsg = other.rewardMsg;
        this.probabilityMsg = other.probabilityMsg;
        this.lotteryType = other.lotteryType;
        this.ssrShowReward = other.ssrShowReward;
        this.srShowReward = other.srShowReward;
        this.showReward = other.showReward;
        this.resetReward = other.resetReward;
        this.rewardPool = other.rewardPool;
        this.rankRewardEmailTitle = other.rankRewardEmailTitle;
        this.rankRewardEmailContent = other.rankRewardEmailContent;
        this.scoreRewardEmailTitle = other.scoreRewardEmailTitle;
        this.scoreRewardEmailContent = other.scoreRewardEmailContent;
    }

    /**
     * 骨骼动画资源图
     */
    @ExcelProperty("骨骼动画资源图")
    @Excel(name = "骨骼动画资源图", width = 15)
    private java.lang.String skeleton;

    /**
     * 免费抽奖次数
     */
    @ExcelProperty("免费抽奖次数")
    @Excel(name = "免费抽奖次数", width = 15)
    private java.lang.String freeNum;

    /**
     * 获奖记录显示数量
     */
    @ExcelProperty("获奖记录显示数量")
    @Excel(name = "获奖记录显示数量", width = 15)
    private java.lang.Integer rewardRecordNum;

    /**
     * 获奖记录内容
     */
    @ExcelProperty("获奖记录内容")
    @Excel(name = "获奖记录内容", width = 15)
    private java.lang.String rewardRecordMsg;

    /**
     * 获奖传闻内容
     */
    @ExcelProperty("获奖传闻内容")
    @Excel(name = "获奖传闻内容", width = 15)
    private java.lang.String rewardMsg;

    /**
     * 概率公示
     */
    @ExcelProperty("概率公示")
    @Excel(name = "概率公示", width = 15)
    private java.lang.String probabilityMsg;

    /**
     * 抽奖设置(单抽/多抽) e.g. [{"itemId":1001, "num":1, "lotteryNum":1, "score":10}]
     */
    @ExcelProperty("抽奖设置(单抽/多抽)")
    @Excel(name = "抽奖设置(单抽/多抽)", width = 15)
    private java.lang.String lotteryType;

    /**
     * 展示特奖 e.g. [{"itemId":1001, "num":1}]
     */
    @ExcelProperty("展示特奖")
    @Excel(name = "展示特奖", width = 15)
    private java.lang.String ssrShowReward;

    /**
     * 展示大奖 e.g. [{"itemId":1001, "num":1}]
     */
    @ExcelProperty("展示大奖")
    @Excel(name = "展示大奖", width = 15)
    private java.lang.String srShowReward;

    /**
     * 展示奖励 e.g. [{"itemId":1001, "num":1}]
     */
    @ExcelProperty("展示奖励")
    @Excel(name = "展示奖励", width = 15)
    private java.lang.String showReward;

    /**
     * 重置大奖 e.g. [1002, 1010, 1021, 1031]
     */
    @ExcelProperty("重置大奖")
    @Excel(name = "重置大奖", width = 15)
    private java.lang.String resetReward;

    /**
     * 奖池 [{"timeMin":1, "timeMax":20, "rewardPool":1}, {"timeMin":21, "timeMax":30, "rewardPool":2}]
     */
    @ExcelProperty("奖池")
    @Excel(name = "奖池", width = 15)
    private java.lang.String rewardPool;

    @ExcelProperty("排名奖励邮件标题")
    @Excel(name = "排名奖励邮件标题", width = 15)
    private java.lang.String rankRewardEmailTitle;

    @ExcelProperty("排名奖励邮件内容")
    @Excel(name = "排名奖励邮件内容", width = 15)
    private java.lang.String rankRewardEmailContent;

    @ExcelProperty("积分奖励邮件标题")
    @Excel(name = "积分奖励邮件标题", width = 15)
    private java.lang.String scoreRewardEmailTitle;

    @ExcelProperty("积分奖励邮件内容")
    @Excel(name = "积分奖励邮件内容", width = 15)
    private java.lang.String scoreRewardEmailContent;

    @TableField(exist = false)
    private List<OpenServiceCampaignLotteryDetailPool> poolList;

    @TableField(exist = false)
    private List<OpenServiceCampaignLotteryDetailRanking> rankingList;

    @TableField(exist = false)
    private List<OpenServiceCampaignLotteryDetailScore> scoreList;
}

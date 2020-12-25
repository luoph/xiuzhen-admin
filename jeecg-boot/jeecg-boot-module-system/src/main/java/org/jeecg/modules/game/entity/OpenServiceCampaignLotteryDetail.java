package org.jeecg.modules.game.entity;

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
 * @description 开服夺宝详情
 * @date 2020-12-25
 */
@Data
@TableName("game_open_service_campaign_lottery_detail")
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class OpenServiceCampaignLotteryDetail extends BaseEntity {

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
    @Excel(name = "typeId", width = 15)
    private java.lang.Integer campaignTypeId;

    /**
     * 开始时间(开服第n天, e.g. 0表示开服第1天)
     */
    @Excel(name = "开始时间(开服第n天)", width = 15)
    private java.lang.Integer startDay;

    /**
     * 持续时间(天)
     */
    @Excel(name = "持续时间(天)", width = 15)
    private java.lang.Integer duration;

    /**
     * 活动页签名称
     */
    @Excel(name = "活动页签名称", width = 15)
    private java.lang.String tabName;

    /**
     * 活动名称
     */
    @Excel(name = "活动名称", width = 15)
    private java.lang.String name;

    /**
     * 活动宣传背景图
     */
    @Excel(name = "活动宣传背景图", width = 15)
    private java.lang.String banner;

    /**
     * 骨骼动画资源图
     */
    @Excel(name = "骨骼动画资源图", width = 15)
    private java.lang.String skeleton;

    /**
     * 获奖记录显示数量
     */
    @Excel(name = "获奖记录显示数量", width = 15)
    private java.lang.Integer rewardRecordNum;

    /**
     * 获奖记录内容
     */
    @Excel(name = "获奖记录内容", width = 15)
    private java.lang.String rewardRecordMsg;

    /**
     * 获奖传闻内容
     */
    @Excel(name = "获奖传闻内容", width = 15)
    private java.lang.String rewardMsg;

    /**
     * 概率公示
     */
    @Excel(name = "概率公示", width = 15)
    private java.lang.String probabilityMsg;

    /**
     * 抽奖设置(单抽/多抽) e.g. [{"itemId":1001, "num":1, "lotteryNum":1, "score":10}]
     */
    @Excel(name = "抽奖设置(单抽/多抽)", width = 15)
    private java.lang.String lotteryType;

    /**
     * 展示特奖 e.g. [{"itemId":1001, "num":1}]
     */
    @Excel(name = "展示特奖", width = 15)
    private java.lang.String ssrShowReward;

    /**
     * 展示大奖 e.g. [{"itemId":1001, "num":1}]
     */
    @Excel(name = "展示大奖", width = 15)
    private java.lang.String srShowReward;

    /**
     * 展示奖励 e.g. [{"itemId":1001, "num":1}]
     */
    @Excel(name = "展示奖励", width = 15)
    private java.lang.String showReward;

    /**
     * 重置大奖 e.g. [1002, 1010, 1021, 1031]
     */
    @Excel(name = "重置大奖", width = 15)
    private java.lang.String resetReward;

    /**
     * 奖池 [{"timeMin":1, "timeMax":20, "rewardPool":1}, {"timeMin":21, "timeMax":30, "rewardPool":2}]
     */
    @Excel(name = "奖池", width = 15)
    private java.lang.String rewardPool;

    /**
     * 帮助信息
     */
    @Excel(name = "帮助信息", width = 15)
    private java.lang.String helpMsg;
}

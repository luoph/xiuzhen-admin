package cn.youai.xiuzhen.game.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 节日砸蛋
 * @date 2021-03-10
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("game_campaign_type_throwing_eggs")
public class GameCampaignTypeThrowingEggs extends GameCampaignTypeBaseDetail {

    private static final long serialVersionUID = 1L;


    public GameCampaignTypeThrowingEggs() {
    }

    public GameCampaignTypeThrowingEggs(GameCampaignTypeThrowingEggs other) {
        this.eggType = other.getEggType();
        this.costItemId = other.getCostItemId();
        this.limitLuckyValue = other.getLimitLuckyValue();
        this.costNum = other.getCostNum();
        this.lotteryIntegralMin = other.getLotteryIntegralMin();
        this.lotteryIntegralMax = other.getLotteryIntegralMax();
        this.luckyProbability = other.getLuckyProbability();
        this.probabilityPublicity = other.getProbabilityPublicity();
        this.rule = other.getRule();
        this.ordinaryPool = other.getOrdinaryPool();
        this.luckyPool = other.getLuckyPool();
        this.ordinaryPoolItem = other.getOrdinaryPoolItem();
        this.luckyPoolItem = other.getLuckyPoolItem();
        this.rewardAnim = other.getRewardAnim();
        this.showOrdinaryReward = other.getShowOrdinaryReward();
        this.showLuckyReward = other.getShowLuckyReward();
        this.throwingEggsValue = other.getThrowingEggsValue();
        this.setMinLevel(other.getMinLevel());
        this.setMaxLevel(other.getMaxLevel());
    }

    /**
     * 砸蛋类型
     */
    @Excel(name = "砸蛋类型", width = 15)
    private java.lang.Integer eggType;

    /**
     * 抽奖所需道具
     */
    @Excel(name = "抽奖所需道具", width = 15)
    private java.lang.Integer costItemId;

    /**
     * 幸运值上限
     */
    @Excel(name = "幸运值上限", width = 15)
    private java.lang.Integer limitLuckyValue;

    /**
     * 抽奖所需道具数量，同时也是每次抽奖掉落的砸蛋值和幸运值
     */
    @Excel(name = "抽奖所需道具数量", width = 15)
    private java.lang.Integer costNum;

    /**
     * 抽奖积分最小值
     */
    @Excel(name = "抽奖积分最小值", width = 15)
    private java.lang.Integer lotteryIntegralMin;

    /**
     * 抽奖积分最大值
     */
    @Excel(name = "抽奖积分最大值", width = 15)
    private java.lang.Integer lotteryIntegralMax;

    /**
     * 幸运奖池概率
     */
    @Excel(name = "幸运奖池概率", width = 15)
    private java.lang.Integer luckyProbability;

    /**
     * 概率公示
     */
    @Excel(name = "概率公示", width = 15)
    private java.lang.String probabilityPublicity;

    /**
     * 玩法规则
     */
    @Excel(name = "玩法规则", width = 15)
    private java.lang.String rule;

    /**
     * 普通奖池
     */
    @Excel(name = "普通奖池", width = 15)
    private java.lang.String ordinaryPool;

    /**
     * 幸运奖池
     */
    @Excel(name = "幸运奖池", width = 15)
    private java.lang.String luckyPool;

    /**
     * 普通奖池掉落
     */
    @Excel(name = "普通奖池掉落", width = 15)
    private java.lang.String ordinaryPoolItem;

    /**
     * 幸运奖池掉落
     */
    @Excel(name = "幸运奖池掉落", width = 15)
    private java.lang.String luckyPoolItem;

    /**
     * 大奖动画
     */
    @Excel(name = "大奖动画", width = 15)
    private java.lang.String rewardAnim;

    /**
     * 普通奖励
     */
    @Excel(name = "普通奖励", width = 15)
    private java.lang.String showOrdinaryReward;

    /**
     * 幸运奖励
     */
    @Excel(name = "幸运奖励", width = 15)
    private java.lang.String showLuckyReward;

    @Excel(name = "砸蛋值", width = 15)
    private java.lang.Integer throwingEggsValue;
}

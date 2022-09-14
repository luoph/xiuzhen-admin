package cn.youai.xiuzhen.game.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 节日砸蛋
 * @date 2021-03-10
 */
@Data
public class GameCampaignTypeThrowingEggsVO {

    /**
     * 砸蛋类型
     */
    @ExcelProperty("砸蛋类型")
    private Integer eggType;

    /**
     * 抽奖所需道具
     */
    @ExcelProperty("消耗道具")
    private Integer costItemId;

    /**
     * 幸运值上限
     */
    @ExcelProperty("幸运值上限")
    private Integer limitLuckyValue;

    /**
     * 抽奖所需道具数量，同时也是每次抽奖掉落的砸蛋值和幸运值
     */
    @ExcelProperty("消耗道具数量")
    private Integer costNum;

    /**
     * 抽奖积分最小值
     */
    @ExcelProperty("抽奖积分最小值")
    private Integer lotteryIntegralMin;

    /**
     * 抽奖积分最大值
     */
    @ExcelProperty("抽奖积分最大值")
    private Integer lotteryIntegralMax;

    /**
     * 幸运奖池概率
     */
    @ExcelProperty("幸运奖池概率")
    private Integer luckyProbability;

    /**
     * 概率公示
     */
    @ExcelProperty("概率公示")
    private String probabilityPublicity;

    /**
     * 玩法规则
     */
    @ExcelProperty("玩法规则")
    private String rule;

    /**
     * 普通奖池
     */
    @ExcelProperty("普通奖池")
    private String ordinaryPool;

    /**
     * 幸运奖池
     */
    @ExcelProperty("幸运奖池")
    private String luckyPool;

    /**
     * 普通奖池掉落
     */
    @ExcelProperty("普通奖池掉落")
    private String ordinaryPoolItem;

    /**
     * 幸运奖池掉落
     */
    @ExcelProperty("幸运奖池掉落")
    private String luckyPoolItem;

    /**
     * 大奖动画
     */
    @ExcelProperty("大奖动画")
    private String rewardAnim;

    /**
     * 普通奖励
     */
    @ExcelProperty("普通奖励")
    private String showOrdinaryReward;

    /**
     * 幸运奖励
     */
    @ExcelProperty("幸运奖励")
    private String showLuckyReward;

    @ExcelProperty("砸蛋值")
    private Integer throwingEggsValue;

    /**
     * 最小世界等级
     */
    @ExcelProperty("最小世界等级")
    private Integer minLevel;

    /**
     * 最大世界等级
     */
    @ExcelProperty("最大世界等级")
    private Integer maxLevel;
}

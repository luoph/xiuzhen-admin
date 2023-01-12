package cn.youai.xiuzhen.game.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * @Description: 节日活动-遗迹夺宝
 * @Author: jeecg-boot
 * @Date: 2023-01-10
 * @Version: V1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("game_campaign_type_relic_lottery")
@ApiModel(value = "game_campaign_type_relic_lottery对象", description = "节日活动-遗迹夺宝")
public class GameCampaignTypeRelicLottery extends GameCampaignTypeBaseDetail {
    private static final long serialVersionUID = 1L;

    /**
     * 活动名称
     */
    @Excel(name = "活动名称", width = 15)
    @ApiModelProperty(value = "活动名称")
    private String name;

    /**
     * 大区间
     */
    @Excel(name = "大区间", width = 15)
    @ApiModelProperty(value = "大区间")
    private Integer area;

    /**
     * 最小层
     */
    @Excel(name = "最小层", width = 15)
    @ApiModelProperty(value = "最小层")
    private Integer minLayer;

    /**
     * 最大层
     */
    @Excel(name = "最大层", width = 15)
    @ApiModelProperty(value = "最大层")
    private Integer maxLayer;

    /**
     * 翻牌消耗 e.g. [{"itemId":1001, "num":1}]
     */
    @Excel(name = "翻牌消耗", width = 15)
    @ApiModelProperty(value = "翻牌消耗")
    private String consume;

    /**
     * 普通奖池 e.g. [{"fall_id":1, "fall_item_id":1001, "fall_item_num":1, "weight":50}]
     */
    @Excel(name = "普通奖池", width = 15)
    @ApiModelProperty(value = "普通奖池")
    private String reward;

    /**
     * 大奖奖池 e.g. [{"fall_id":1, "fall_item_id":1001, "fall_item_num":1}]
     */
    @Excel(name = "大奖奖池", width = 15)
    @ApiModelProperty(value = "大奖奖池")
    private String bigReward;

    /**
     * 暴击概率  e.g. [{"id":1, "crit":1, "weight":50}]
     */
    @Excel(name = "暴击概率", width = 15)
    @ApiModelProperty(value = "暴击概率")
    private String crit;

    /**
     * 概率公示 e.g. [{"itemId":1001, "pr":1}]
     */
    @Excel(name = "概率公示", width = 15)
    @ApiModelProperty(value = "概率公示")
    private String prShow;
}

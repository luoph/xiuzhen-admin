package cn.youai.xiuzhen.game.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * @Description: 节日活动-召唤活动
 * @Author: jeecg-boot
 * @Date: 2023-04-12
 * @Version: V1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("game_campaign_type_summon")
@ApiModel(value = "game_campaign_type_summon对象", description = "节日活动-召唤活动")
public class GameCampaignTypeSummon extends GameCampaignTypeBaseDetail {
    private static final long serialVersionUID = 1L;

    /**
     * 活动名称
     */
    @Excel(name = "活动名称", width = 15)
    @ApiModelProperty(value = "活动名称")
    private String name;

    /**
     * 抽奖消耗道具
     */
    @Excel(name = "抽奖消耗道具", width = 15)
    @ApiModelProperty(value = "抽奖消耗道具")
    private String summonConsume;

    /**
     * 更换心仪大奖消耗
     */
    @Excel(name = "更换心仪大奖消耗", width = 15)
    @ApiModelProperty(value = "更换心仪大奖消耗")
    private String changeFavoriteRewardConsume;

    /**
     * 第N次开始抽大奖奖池
     */
    @Excel(name = "第N次开始抽大奖奖池", width = 15)
    @ApiModelProperty(value = "第N次开始抽大奖奖池")
    private Integer summonBigRewardNum;

    /**
     * 第N次开始抽心仪奖池
     */
    @Excel(name = "第N次开始抽心仪奖池", width = 15)
    @ApiModelProperty(value = "第N次开始抽心仪奖池")
    private Integer summonFavoriteRewardNum;

    /**
     * 普通奖池
     */
    @Excel(name = "普通奖池", width = 15)
    @ApiModelProperty(value = "普通奖池")
    private String reward;

    /**
     * 大奖奖池
     */
    @Excel(name = "大奖奖池", width = 15)
    @ApiModelProperty(value = "大奖奖池")
    private String bigReward;

    /**
     * 心仪奖池
     */
    @Excel(name = "心仪奖池", width = 15)
    @ApiModelProperty(value = "心仪奖池")
    private String favoriteReward;

    /**
     * 概率公示
     */
    @Excel(name = "概率公示", width = 15)
    @ApiModelProperty(value = "概率公示")
    private String prShow;
}

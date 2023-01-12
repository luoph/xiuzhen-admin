package cn.youai.xiuzhen.game.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * @Description: 节日活动-遗迹夺宝-传闻
 * @Author: jeecg-boot
 * @Date: 2023-01-10
 * @Version: V1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("game_campaign_type_relic_lottery_message")
@ApiModel(value = "game_campaign_type_relic_lottery_message对象", description = "节日活动-遗迹夺宝-传闻")
public class GameCampaignTypeRelicLotteryMessage extends GameCampaignTypeBase {
    private static final long serialVersionUID = 1L;

    /**
     * 通关层数推送
     */
    @Excel(name = "通关层数推送", width = 15)
    @ApiModelProperty(value = "通关层数推送")
    private Integer layer;

    /**
     * 传闻内容
     */
    @Excel(name = "传闻内容", width = 15)
    @ApiModelProperty(value = "传闻内容")
    private String content;
}

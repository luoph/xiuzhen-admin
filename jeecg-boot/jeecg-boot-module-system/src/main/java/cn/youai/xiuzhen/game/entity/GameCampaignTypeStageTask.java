package cn.youai.xiuzhen.game.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * @Description: 节日活动-阶段任务
 * @Author: jeecg-boot
 * @Date: 2023-01-13
 * @Version: V1.0
 */
@Data
@TableName("game_campaign_type_stage_task")
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "game_campaign_type_stage_task对象", description = "节日活动-阶段任务")
public class GameCampaignTypeStageTask extends GameCampaignTypeBaseDetail {
    private static final long serialVersionUID = 1L;

    /**
     * 活动名称
     */
    @Excel(name = "活动名称", width = 15)
    @ApiModelProperty(value = "活动名称")
    private String name;

    /**
     * 阶段
     */
    @Excel(name = "阶段", width = 15)
    @ApiModelProperty(value = "阶段")
    private Integer stage;

    /**
     * 阶段奖励 [{"itemId":1001, "num":1}]
     */
    @Excel(name = "阶段奖励", width = 15)
    @ApiModelProperty(value = "阶段奖励")
    private String bigReward;
}

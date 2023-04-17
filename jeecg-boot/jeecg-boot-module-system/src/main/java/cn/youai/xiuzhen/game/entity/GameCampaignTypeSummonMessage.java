package cn.youai.xiuzhen.game.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * @Description: 节日活动-召唤活动-传闻
 * @Author: jeecg-boot
 * @Date: 2023-04-12
 * @Version: V1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("game_campaign_type_summon_message")
@ApiModel(value = "game_campaign_type_summon_message对象", description = "节日活动-召唤活动-传闻")
public class GameCampaignTypeSummonMessage extends GameCampaignTypeBase {
    private static final long serialVersionUID = 1L;

    /**
     * 获取道具id
     */
    @Excel(name = "获取道具id", width = 15)
    @ApiModelProperty(value = "获取道具id")
    private Integer itemId;

    /**
     * 传闻内容
     */
    @Excel(name = "传闻内容", width = 15)
    @ApiModelProperty(value = "传闻内容")
    private String content;
}

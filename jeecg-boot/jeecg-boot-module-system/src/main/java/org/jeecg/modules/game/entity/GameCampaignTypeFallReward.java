package org.jeecg.modules.game.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 掉落奖励组
 * @date 2021-01-15
 */
@Data
@TableName("game_campaign_type_fall_reward")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class GameCampaignTypeFallReward extends GameCampaignTypeBase {

    private static final long serialVersionUID = 1L;

    public GameCampaignTypeFallReward() {
    }

    public GameCampaignTypeFallReward(GameCampaignTypeFallReward other) {
        this.rewardId = other.getRewardId();
        this.reward = other.getReward();
        this.weight = other.getWeight();
        this.message = other.getMessage();
    }

    /**
     * 奖励组id
     */
    @Excel(name = "奖励组id", width = 15)
    private java.lang.Integer rewardId;

    /**
     * 奖励列表 e.g. [{"itemId":1001, "num":1}]
     */
    @Excel(name = "奖励列表", width = 15)
    private java.lang.String reward;

    /**
     * 掉落权重
     */
    @Excel(name = "掉落权重", width = 15)
    private java.lang.Integer weight;

    /**
     * 传闻id
     */
    @Excel(name = "传闻id", width = 15)
    private java.lang.Integer message;
}

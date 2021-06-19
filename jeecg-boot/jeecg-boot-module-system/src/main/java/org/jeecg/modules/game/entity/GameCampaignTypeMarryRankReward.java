package org.jeecg.modules.game.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 节日活动-结义排行榜奖励
 * @date 2021-06-20
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@TableName("game_campaign_type_marry_rank_reward")
public class GameCampaignTypeMarryRankReward extends GameCampaignTypeBase {

    private static final long serialVersionUID = 1L;

    public GameCampaignTypeMarryRankReward() {
    }

    public GameCampaignTypeMarryRankReward(GameCampaignTypeMarryRankReward entity) {
        this.minRank = entity.getMinRank();
        this.maxRank = entity.getMaxRank();
        this.score = entity.getScore();
        this.reward = entity.getReward();
    }

    /**
     * 排名最小值
     */
    @Excel(name = "排名最小值", width = 15)
    private java.lang.Integer minRank;

    /**
     * 排名最大值
     */
    @Excel(name = "排名最大值", width = 15)
    private java.lang.Integer maxRank;

    /**
     * 上榜最低积分
     */
    @Excel(name = "上榜最低积分", width = 15)
    private java.lang.Integer score;

    /**
     * 奖励列表 e.g. [{"itemId":1001,"num":1}]
     */
    @Excel(name = "奖励列表", width = 15)
    private java.lang.String reward;

}

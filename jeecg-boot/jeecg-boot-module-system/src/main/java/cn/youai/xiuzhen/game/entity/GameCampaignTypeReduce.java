package cn.youai.xiuzhen.game.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description game_campaign_type_reduce
 * @date 2021-02-02
 */
@Data
@TableName("game_campaign_type_reduce")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class GameCampaignTypeReduce extends GameCampaignTypeBase {

    public GameCampaignTypeReduce() {
    }

    public GameCampaignTypeReduce(GameCampaignTypeReduce other) {
        this.sort = other.getSort();
        this.reduceItemId = other.getReduceItemId();
        this.limitNum = other.getLimitNum();
        this.reward = other.getReward();
        this.minLevel = other.getMinLevel();
        this.maxLevel = other.getMaxLevel();

    }

    /**
     * 排名序列
     */
    @Excel(name = "排名序列", width = 15)
    private java.lang.Integer sort;

    /**
     * 消耗道具id,item_id
     */
    @Excel(name = "消耗道具id,item_id", width = 15)
    private java.lang.Integer reduceItemId;

    /**
     * 上榜下限数量
     */
    @Excel(name = "上榜下限数量", width = 15)
    private java.lang.Integer limitNum;

    /**
     * 奖励内容[{"itemId":1001,"num":100}]
     */
    @Excel(name = "奖励内容", width = 15)
    private java.lang.String reward;
    /**
     * 最小世界等级
     */
    @Excel(name = "最小世界等级", width = 15)
    private Integer minLevel;

    /**
     * 最大世界等级
     */
    @Excel(name = "最大世界等级", width = 15)
    private Integer maxLevel;
}

package org.jeecg.modules.game.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecg.common.constant.TimeConstant;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 砸蛋榜
 * @date 2021-03-10
 */
@Data
@TableName("game_campaign_type_throwing_eggs_rank")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class GameCampaignTypeThrowingEggsRank extends GameCampaignTypeBase {

    private static final long serialVersionUID = 1L;

    public GameCampaignTypeThrowingEggsRank() {
    }

    public GameCampaignTypeThrowingEggsRank(GameCampaignTypeThrowingEggsRank other) {
        this.sort = other.getSort();
        this.limitNum = other.getLimitNum();
        this.reward = other.getReward();
    }

    /**
     * 排名序列
     */
    @Excel(name = "排名序列", width = 15)
    private java.lang.Integer sort;

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

}

package cn.youai.xiuzhen.game.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 节日派对进度任务
 * @date 2021-03-30
 */
@Data
@TableName("game_campaign_type_party_progress")
@EqualsAndHashCode(callSuper = false)
public class GameCampaignTypePartyProgress extends GameCampaignTypeBase {

    private static final long serialVersionUID = 1L;

    public GameCampaignTypePartyProgress() {
    }

    public GameCampaignTypePartyProgress(GameCampaignTypePartyProgress other) {
        this.target = other.getTarget();
        this.percent = other.getPercent();
        this.reward = other.getReward();
    }

    /**
     * 任务规定数量
     */
    @ExcelProperty("任务规定数量")
    @Excel(name = "任务规定数量", width = 15)
    private java.lang.Integer target;

    /**
     * 进度百分比
     */
    @ExcelProperty("进度百分比")
    @Excel(name = "进度百分比", width = 15)
    private java.lang.Integer percent;

    /**
     * 任务奖励
     */
    @ExcelProperty("任务奖励")
    @Excel(name = "任务奖励", width = 15)
    private java.lang.String reward;

}

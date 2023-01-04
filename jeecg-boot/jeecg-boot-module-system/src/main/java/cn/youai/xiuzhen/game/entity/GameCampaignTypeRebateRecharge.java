package cn.youai.xiuzhen.game.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 狂欢返利
 * @date 2021-05-31
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("game_campaign_type_rebate_recharge")
public class GameCampaignTypeRebateRecharge extends GameCampaignTypeBaseDetail {

    private static final long serialVersionUID = 1L;

    public GameCampaignTypeRebateRecharge() {
    }

    public GameCampaignTypeRebateRecharge(GameCampaignTypeRebateRecharge other) {
        this.rid = other.getRid();
        this.totalDay = other.getTotalDay();
        this.min = other.getMin();
        this.progressDesc = other.getProgressDesc();
        this.reward = other.getReward();
        this.setMinLevel(other.getMinLevel());
        this.setMaxLevel(other.getMaxLevel());
    }

    /**
     * 礼包id
     */
    @Excel(name = "礼包id ", width = 15)
    private java.lang.Integer rid;

    /**
     * 累计充值天数
     */
    @Excel(name = "累计充值天数", width = 15)
    private java.lang.Integer totalDay;

    /**
     * 最低充值金额
     */
    @Excel(name = "最低充值金额", width = 15)
    private java.math.BigDecimal min;

    /**
     * 进度描述
     */
    @Excel(name = "进度描述", width = 15)
    private java.lang.String progressDesc;

    /**
     * 奖励列表
     */
    @Excel(name = "奖励列表", width = 15)
    private java.lang.String reward;
}

package org.jeecg.modules.game.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;
import org.jeecg.common.constant.TimeConstant;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 狂欢返利
 * @date 2021-05-31
 */
@Data
@TableName("game_campaign_type_rebate_recharge")
@Accessors(chain = true)
public class GameCampaignTypeRebateRecharge extends GameCampaignTypeBase {

    private static final long serialVersionUID = 1L;

    public GameCampaignTypeRebateRecharge() {
    }

    public GameCampaignTypeRebateRecharge(GameCampaignTypeRebateRecharge other) {
        this.rid = other.getRid();
        this.totalDay = other.getTotalDay();
        this.min = other.getMin();
        this.progressDesc = other.getProgressDesc();
        this.reward = other.getReward();
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

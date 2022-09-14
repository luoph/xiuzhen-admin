package cn.youai.xiuzhen.game.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 狂欢返利
 * @date 2021-05-31
 */
@Data
@Accessors(chain = true)
@TableName("game_campaign_type_rebate_recharge")
@EqualsAndHashCode(callSuper = true)
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
        this.minLevel = other.getMinLevel();
        this.maxLevel = other.getMaxLevel();
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

    /**
     * 最小世界等级
     */
    @ExcelProperty("最小世界等级")
    @Excel(name = "最小世界等级", width = 15)
    private Integer minLevel;

    /**
     * 最大世界等级
     */
    @ExcelProperty("最大世界等级")
    @Excel(name = "最大世界等级", width = 15)
    private Integer maxLevel;

}

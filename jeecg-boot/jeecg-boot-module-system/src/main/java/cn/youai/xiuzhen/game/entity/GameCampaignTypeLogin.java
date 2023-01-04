package cn.youai.xiuzhen.game.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 登录活动
 * @date 2020-10-15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("game_campaign_type_login")
public class GameCampaignTypeLogin extends GameCampaignTypeBaseDetail {

    private static final long serialVersionUID = 1L;

    public GameCampaignTypeLogin() {
    }

    public GameCampaignTypeLogin(GameCampaignTypeLogin other) {
        this.loginDay = other.getLoginDay();
        this.description = other.getDescription();
        this.reward = other.getReward();
        this.setMinLevel(other.getMinLevel());
        this.setMaxLevel(other.getMaxLevel());
    }

    /**
     * 登录天数
     */
    @ExcelProperty("登录天数")
    @Excel(name = "登录天数", width = 15)
    private Integer loginDay;

    /**
     * 描述
     */
    @ExcelProperty("描述")
    @Excel(name = "描述", width = 15)
    private String description;

    /**
     * 奖励列表
     */
    @ExcelProperty("奖励列表")
    @Excel(name = "奖励列表", width = 15)
    private String reward;
}

package org.jeecg.modules.game.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 登录活动
 * @date 2020-10-15
 */
@Data
@TableName("game_campaign_type_login")
@EqualsAndHashCode(callSuper = true)
public class GameCampaignTypeLogin extends GameCampaignTypeBase {

    private static final long serialVersionUID = 1L;

    public GameCampaignTypeLogin() {
    }

    public GameCampaignTypeLogin(GameCampaignTypeLogin other) {
        this.loginDay = other.getLoginDay();
        this.description = other.getDescription();
        this.reward = other.getReward();
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

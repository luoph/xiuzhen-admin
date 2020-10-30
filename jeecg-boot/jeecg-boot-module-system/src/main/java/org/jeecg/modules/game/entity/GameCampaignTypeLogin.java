package org.jeecg.modules.game.entity;

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
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class GameCampaignTypeLogin extends GameCampaignTypeBase {

    private static final long serialVersionUID = 1L;

    /**
     * 登录天数
     */
    @Excel(name = "登录天数", width = 15)
    private java.lang.Integer loginDay;

    /**
     * 描述
     */
    @Excel(name = "描述", width = 15)
    private java.lang.String description;

    /**
     * 奖励列表
     */
    @Excel(name = "奖励列表", width = 15)
    private java.lang.String reward;
}

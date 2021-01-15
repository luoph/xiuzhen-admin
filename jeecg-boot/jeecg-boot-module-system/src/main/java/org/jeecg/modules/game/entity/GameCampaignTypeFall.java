package org.jeecg.modules.game.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 节日掉落
 * @date 2021-01-15
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@TableName("game_campaign_type_fall")
public class GameCampaignTypeFall extends GameCampaignTypeBase {

    private static final long serialVersionUID = 1L;

    /**
     * 模块
     * 1.仙器秘境
     * 2.仙兽秘境
     * 3.丹药秘境
     * 4.修为秘境
     * 5.灵石秘境
     * 6.北冥魔海
     * 7.不死魔巢
     * 8.蛇陵魔窟
     * 9.魔王入侵
     * 10.剧情挂机
     */
    @Excel(name = "模块", width = 15)
    private java.lang.Integer module;

    /**
     * 奖励类型
     * 1.按比例加成, 2.额外的活动掉落组, 3.剧情挂机奖励
     */
    @Excel(name = "奖励类型", width = 15)
    private java.lang.Integer rewardType;

    /**
     * reward_type 对应的奖励值, e.g. 5,  [1, 2],  [{"time":300, "reward":1}]
     */
    @Excel(name = "奖励", width = 15)
    private java.lang.String reward;
}

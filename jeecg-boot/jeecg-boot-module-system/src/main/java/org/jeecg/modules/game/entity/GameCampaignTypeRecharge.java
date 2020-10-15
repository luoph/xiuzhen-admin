package org.jeecg.modules.game.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecg.common.system.base.entity.BaseEntity;
import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 充值活动
 * @date 2020-10-15
 */
@Data
@TableName("game_campaign_type_recharge")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class GameCampaignTypeRecharge extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private java.lang.Long id;

    /**
     * campaign.id, 活动id
     */
    @Excel(name = "活动id", width = 15)
    private java.lang.Long campaignId;

    /**
     * game_campaign_type.id
     */
    @Excel(name = "typeId", width = 15)
    private java.lang.Long typeId;

    /**
     * 礼包id
     */
    @Excel(name = "礼包id", width = 15)
    private java.lang.Integer rechargeId;

    /**
     * 累计充值额度
     */
    @Excel(name = "累计充值额度", width = 15)
    private java.math.BigDecimal rechargeAmount;

    /**
     * 奖励列表
     */
    @Excel(name = "奖励列表", width = 15)
    private java.lang.String reward;
}

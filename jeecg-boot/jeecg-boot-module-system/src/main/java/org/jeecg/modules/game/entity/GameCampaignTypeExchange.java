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
 * @description 兑换活动
 * @date 2020-10-15
 */
@Data
@TableName("game_campaign_type_exchange")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class GameCampaignTypeExchange extends BaseEntity {

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
     * 兑换id
     */
    @Excel(name = "兑换id", width = 15)
    private java.lang.Integer exchangeId;

    /**
     * 道具名称
     */
    @Excel(name = "道具名称", width = 15)
    private java.lang.String itemName;

    /**
     * 最大兑换数量
     */
    @Excel(name = "最大兑换数量", width = 15)
    private java.lang.Integer maxExchangeNum;

    /**
     * 奖励列表
     */
    @Excel(name = "奖励列表", width = 15)
    private java.lang.String reward;

    /**
     * 消耗列表
     */
    @Excel(name = "消耗列表", width = 15)
    private java.lang.String consume;
}
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
 * @description 开服活动-单比好礼-任务明细
 * @date 2020-12-23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("game_open_service_campaign_single_gift_item")
@Accessors(chain = true)
public class OpenServiceCampaignSingleGiftItem extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private java.lang.Long id;

    /**
     * 开服活动id
     */
    @Excel(name = "开服活动id", width = 15)
    private java.lang.Long campaignId;

    /**
     * typeId
     */
    @Excel(name = "typeId", width = 15)
    private java.lang.Long campaignTypeId;

    /**
     * open_service_single_recharge_gift_detail.id
     */
    @Excel(name = "giftDetailId", width = 15)
    private java.lang.Long giftDetailId;

    /**
     * 任务金额
     */
    @Excel(name = "任务金额", width = 15)
    private java.math.BigDecimal amount;

    /**
     * 领取上限次数
     */
    @Excel(name = "领取上限次数", width = 15)
    private java.lang.Integer limitTimes;

    /**
     * 任务描述
     */
    @Excel(name = "任务描述", width = 15)
    private java.lang.String remark;

    /**
     * 奖励json
     */
    @Excel(name = "奖励列表", width = 15)
    private java.lang.String reward;
}

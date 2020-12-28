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
 * @description 开服活动-开服开服礼包-礼包明细
 * @date 2020-12-21
 */
@Data
@TableName("game_open_service_campaign_gift_detail_item")
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class OpenServiceCampaignGiftDetailItem extends BaseEntity {

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
     * open_service_campaign_gift_detail.id
     */
    @Excel(name = "详情id", width = 15)
    private java.lang.Long giftDetailId;

    /**
     * 排序
     */
    @Excel(name = "排序", width = 15)
    private java.lang.Integer sort;

    /**
     * 礼包类型 0.普通礼包, 1.大奖礼包
     */
    @Excel(name = "礼包类型", width = 15)
    private java.lang.Integer giftType;

    /**
     * 购买数量
     */
    @Excel(name = "购买数量", width = 15)
    private java.lang.Integer buyNum;

    /**
     * 折扣
     */
    @Excel(name = "折扣", width = 15)
    private java.math.BigDecimal discount;

    /**
     * 价格
     */
    @Excel(name = "价格", width = 15)
    private java.lang.String price;

    /**
     * 奖励列表
     */
    @Excel(name = "奖励列表", width = 15)
    private java.lang.String reward;
}

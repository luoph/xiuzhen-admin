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
 * @description 开服活动消耗道具
 * @date 2020-12-28
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@TableName("game_open_service_campaign_consume_detail_item")
public class OpenServiceCampaignConsumeDetailItem extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private java.lang.Integer id;

    /**
     * 开服活动id, open_service_campaign.id
     */
    @Excel(name = "开服活动id", width = 15)
    private java.lang.Integer campaignId;

    /**
     * open_service_campaign_type.id
     */
    @Excel(name = "页签id", width = 15)
    private java.lang.Integer campaignTypeId;

    /**
     * open_service_campaign_consume_detail.id
     */
    @Excel(name = "详情id", width = 15)
    private java.lang.Integer consumeDetailId;

    /**
     * 排序
     */
    @Excel(name = "排序", width = 15)
    private java.lang.Integer sort;

    /**
     * 统计类型 0.个人, 1.全服
     */
    @Excel(name = "统计类型", width = 15)
    private java.lang.Integer consumeType;

    /**
     * 开始时间(子活动开始第n天, e.g. 0表示子活动开始第1天)
     */
    @Excel(name = "开始时间(子活动开始第n天)", width = 15)
    private java.lang.Integer startDay;

    /**
     * 开启前是否统计，全服统计默认是
     */
    @Excel(name = "开启前是否统计", width = 15)
    private java.lang.Integer statisticsNotStart;

    /**
     * 描述
     */
    @Excel(name = "描述", width = 15)
    private java.lang.String description;

    /**
     * 消耗道具 e.g. [{"itemId":1001, "num":1}]
     */
    @Excel(name = "消耗道具", width = 15)
    private java.lang.String consume;

    /**
     * 奖励列表 e.g. [{"itemId":1001, "num":1}]
     */
    @Excel(name = "奖励列表", width = 15)
    private java.lang.String reward;
}

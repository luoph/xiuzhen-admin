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
 * @description 开服活动-开服排行-活动明细(3级)
 * @date 2020-12-21
 */
@Data
@TableName("game_open_service_campaign_gift_detail")
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class OpenServiceCampaignGiftDetail extends BaseEntity {

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
     * 活动名称
     */
    @Excel(name = "活动名称", width = 15)
    private java.lang.String name;

    /**
     * 活动页签名称
     */
    @Excel(name = "活动页签名称", width = 15)
    private java.lang.String tabName;

    /**
     * 活动宣传背景图
     */
    @Excel(name = "活动宣传背景图", width = 15)
    private java.lang.String banner;

    /**
     * 骨骼动画资源
     */
    @Excel(name = "骨骼动画资源", width = 15)
    private java.lang.String skeleton;

    /**
     * 开始时间(开服第n天, e.g. 0表示开服第1天)
     */
    @Excel(name = "开始时间", width = 15)
    private java.lang.Integer startDay;

    /**
     * 排序
     */
    @Excel(name = "排序", width = 15)
    private java.lang.Integer sort;

    /**
     * 持续时间(天)
     */
    @Excel(name = "持续时间(天)", width = 15)
    private java.lang.Integer duration;

}

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
 * @description 开服活动-开服排行-活动明细-消耗道具分数
 * @date 2020-12-21
 */
@Data
@TableName("game_open_service_campaign_rank_detail_score")
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class OpenServiceCampaignRankDetailScore extends BaseEntity {

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
     * open_service_campaign_type.id
     */
    @Excel(name = "open_service_campaign_type.id", width = 15)
    private java.lang.Long campaignTypeId;

    /**
     * open_service_campaign_rank_detail.id
     */
    @Excel(name = "open_service_campaign_rank_detail.id", width = 15)
    private java.lang.Long rankDetailId;

    /**
     * 排行消耗道具id
     */
    @Excel(name = "排行消耗道具id", width = 15)
    private java.lang.Integer itemId;

    /**
     * 排行消耗道具数量
     */
    @Excel(name = "排行消耗道具数量", width = 15)
    private java.lang.Integer num;

    /**
     * 消耗对应积分
     */
    @Excel(name = "消耗对应积分", width = 15)
    private java.lang.Integer score;

    /**
     * 道具积分分类
     */
    @Excel(name = "道具积分分类", width = 15)
    private java.lang.Integer itemType;

    /**
     * 道具积分分类名称
     */
    @Excel(name = "道具积分分类名称", width = 15)
    private java.lang.String itemTypeName;
}

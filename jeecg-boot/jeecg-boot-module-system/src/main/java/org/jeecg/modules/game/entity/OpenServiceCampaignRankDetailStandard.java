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
 * @description 开服活动-开服排行-活动明细-达标奖励
 * @date 2020-12-21
 */
@Data
@TableName("game_open_service_campaign_rank_detail_standard")
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class OpenServiceCampaignRankDetailStandard extends BaseEntity {

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
     * 达标奖励积分
     */
    @Excel(name = "达标奖励积分", width = 15)
    private java.lang.Integer score;

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

    /**
     * 传闻内容
     */
    @Excel(name = "传闻内容", width = 15)
    private java.lang.String message;
}

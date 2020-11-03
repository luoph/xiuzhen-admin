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
 * @description 活动区服配置
 * @date 2020-10-15
 */
@Data
@TableName("game_campaign_support")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class GameCampaignSupport extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * campaign.id, 活动id
     */
    @Excel(name = "campaign.id, 活动id", width = 15)
    private Long campaignId;

    /**
     * 渠道id/服务器id
     */
    @Excel(name = "渠道id/服务器id", width = 15)
    private Integer serverId;
    /**
     * 状态
     */
    @Excel(name = "状态", width = 15)
    private Integer status;

    /**
     * 开启game_campaign_type.id
     */
    @Excel(name = "开启game_campaign_type.id", width = 15)
    private Long typeId;
}

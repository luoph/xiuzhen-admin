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
 * @description 开服夺宝奖池
 * @date 2020-12-25
 */
@Data
@TableName("open_service_campaign_lottery_detail_pool")
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class OpenServiceCampaignLotteryDetailPool extends BaseEntity {

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
    @Excel(name = "typeId", width = 15)
    private java.lang.Integer campaignTypeId;

    /**
     * open_service_campaign_lottery_detail.id
     */
    @Excel(name = "详情页签id", width = 15)
    private java.lang.Integer lotteryDetailId;

    /**
     * 奖池id
     */
    @Excel(name = "奖池id", width = 15)
    private java.lang.Integer poolId;

    /**
     * 奖励列表 e.g. [{"itemId":1001, "num":1}]
     */
    @Excel(name = "奖励列表", width = 15)
    private java.lang.String reward;

    /**
     * 掉落权重
     */
    @Excel(name = "掉落权重", width = 15)
    private java.lang.Integer weight;

    /**
     * 是否记录
     */
    @Excel(name = "是否记录", width = 15)
    private java.lang.Integer record;

    /**
     * 是否传闻
     */
    @Excel(name = "是否传闻", width = 15)
    private java.lang.Integer message;

    /**
     * 是否大奖弹窗
     */
    @Excel(name = "是否大奖弹窗", width = 15)
    private java.lang.Integer showReward;
}

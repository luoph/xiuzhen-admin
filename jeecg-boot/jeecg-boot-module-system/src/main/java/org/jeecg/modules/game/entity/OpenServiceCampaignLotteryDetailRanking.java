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
 * @description 开服夺宝榜单
 * @date 2020-12-25
 */
@Data
@TableName("game_open_service_campaign_lottery_detail_ranking")
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class OpenServiceCampaignLotteryDetailRanking extends BaseEntity {

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
     * 页签详情id
     */
    @Excel(name = "页签详情id", width = 15)
    private java.lang.Integer lotteryDetailId;

    /**
     * 排名最小值
     */
    @Excel(name = "排名最小值", width = 15)
    private java.lang.Integer minRank;

    /**
     * 排名最大值
     */
    @Excel(name = "排名最大值", width = 15)
    private java.lang.Integer maxRank;

    /**
     * 上榜最低积分
     */
    @Excel(name = "上榜最低积分", width = 15)
    private java.lang.Integer score;

    /**
     * 奖励列表 e.g. [{"itemId":1001, "num":1}]
     */
    @Excel(name = "奖励列表", width = 15)
    private java.lang.String reward;

}

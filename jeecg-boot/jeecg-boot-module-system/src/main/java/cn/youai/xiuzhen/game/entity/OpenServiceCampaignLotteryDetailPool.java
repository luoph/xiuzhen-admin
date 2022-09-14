package cn.youai.xiuzhen.game.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jeecg.common.system.base.entity.BaseEntity;
import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 开服夺宝奖池
 * @date 2020-12-25
 */
@Data
@TableName("game_open_service_campaign_lottery_detail_pool")
@EqualsAndHashCode(callSuper = true)
public class OpenServiceCampaignLotteryDetailPool extends BaseEntity {

    private static final long serialVersionUID = 1L;

    public OpenServiceCampaignLotteryDetailPool() {
    }

    public OpenServiceCampaignLotteryDetailPool(OpenServiceCampaignLotteryDetailPool other) {
//        this.id = other.id;
//        this.campaignId = other.campaignId;
//        this.campaignTypeId = other.campaignTypeId;
//        this.lotteryDetailId = other.lotteryDetailId;
        this.poolId = other.poolId;
        this.reward = other.reward;
        this.weight = other.weight;
        this.record = other.record;
        this.message = other.message;
        this.showReward = other.showReward;
    }

    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private java.lang.Long id;

    /**
     * 开服活动id, open_service_campaign.id
     */
    @ExcelProperty("开服活动id")
    @Excel(name = "开服活动id", width = 15)
    private java.lang.Long campaignId;

    /**
     * open_service_campaign_type.id
     */
    @ExcelProperty("页签id")
    @Excel(name = "页签id", width = 15)
    private java.lang.Long campaignTypeId;

    /**
     * open_service_campaign_lottery_detail.id
     */
    @ExcelProperty("详情id")
    @Excel(name = "详情id", width = 15)
    private java.lang.Long lotteryDetailId;

    /**
     * 奖池id
     */
    @ExcelProperty("奖池id")
    @Excel(name = "奖池id", width = 15)
    private java.lang.Integer poolId;

    /**
     * 奖励列表 e.g. [{"itemId":1001, "num":1}]
     */
    @ExcelProperty("奖励列表")
    @Excel(name = "奖励列表", width = 15)
    private java.lang.String reward;

    /**
     * 掉落权重
     */
    @ExcelProperty("掉落权重")
    @Excel(name = "掉落权重", width = 15)
    private java.lang.Integer weight;

    /**
     * 是否记录
     */
    @ExcelProperty("是否记录")
    @Excel(name = "是否记录", width = 15)
    private java.lang.Integer record;

    /**
     * 是否传闻
     */
    @ExcelProperty("是否传闻")
    @Excel(name = "是否传闻", width = 15)
    private java.lang.Integer message;

    /**
     * 是否大奖弹窗
     */
    @ExcelProperty("是否大奖弹窗")
    @Excel(name = "是否大奖弹窗", width = 15)
    private java.lang.Integer showReward;
}

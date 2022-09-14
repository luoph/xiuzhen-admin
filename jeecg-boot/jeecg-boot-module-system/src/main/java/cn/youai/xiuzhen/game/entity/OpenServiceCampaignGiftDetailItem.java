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
 * @description 开服活动-开服开服礼包-礼包明细
 * @date 2020-12-21
 */
@Data
@TableName("game_open_service_campaign_gift_detail_item")
@EqualsAndHashCode(callSuper = true)
public class OpenServiceCampaignGiftDetailItem extends BaseEntity {

    private static final long serialVersionUID = 1L;

    public OpenServiceCampaignGiftDetailItem() {
    }

    public OpenServiceCampaignGiftDetailItem(OpenServiceCampaignGiftDetailItem other) {
//        this.id = other.id;
//        this.campaignId = other.campaignId;
//        this.campaignTypeId = other.campaignTypeId;
//        this.giftDetailId = other.giftDetailId;
        this.sort = other.sort;
        this.giftType = other.giftType;
        this.buyNum = other.buyNum;
        this.discount = other.discount;
        this.price = other.price;
        this.reward = other.reward;
    }

    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private java.lang.Long id;

    /**
     * 开服活动id
     */
    @ExcelProperty("开服活动id")
    @Excel(name = "开服活动id", width = 15)
    private java.lang.Long campaignId;

    /**
     * typeId
     */
    @ExcelProperty("页签id")
    @Excel(name = "页签id", width = 15)
    private java.lang.Long campaignTypeId;

    /**
     * open_service_campaign_gift_detail.id
     */
    @ExcelProperty("详情id")
    @Excel(name = "详情id", width = 15)
    private java.lang.Long giftDetailId;

    /**
     * 排序
     */
    @ExcelProperty("排序")
    @Excel(name = "排序", width = 15)
    private java.lang.Integer sort;

    /**
     * 礼包类型 0.普通礼包, 1.大奖礼包
     */
    @ExcelProperty("礼包类型")
    @Excel(name = "礼包类型", width = 15)
    private java.lang.Integer giftType;

    /**
     * 购买数量
     */
    @ExcelProperty("购买数量")
    @Excel(name = "购买数量", width = 15)
    private java.lang.Integer buyNum;

    /**
     * 折扣
     */
    @ExcelProperty("折扣")
    @Excel(name = "折扣", width = 15)
    private java.math.BigDecimal discount;

    /**
     * 价格
     */
    @ExcelProperty("价格")
    @Excel(name = "价格", width = 15)
    private java.lang.String price;

    /**
     * 奖励列表
     */
    @ExcelProperty("奖励列表")
    @Excel(name = "奖励列表", width = 15)
    private java.lang.String reward;
}

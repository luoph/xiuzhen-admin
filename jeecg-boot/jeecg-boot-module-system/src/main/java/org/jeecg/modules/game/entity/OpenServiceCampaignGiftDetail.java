package org.jeecg.modules.game.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jeecg.common.system.base.entity.BaseEntity;
import org.jeecgframework.poi.excel.annotation.Excel;

import java.util.List;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 开服活动-开服排行-活动明细(3级)
 * @date 2020-12-21
 */
@Data
@TableName("game_open_service_campaign_gift_detail")
@EqualsAndHashCode(callSuper = true)
public class OpenServiceCampaignGiftDetail extends BaseEntity {

    private static final long serialVersionUID = 1L;

    public OpenServiceCampaignGiftDetail() {
    }

    public OpenServiceCampaignGiftDetail(OpenServiceCampaignGiftDetail other) {
//        this.id = other.id;
//        this.campaignId = other.campaignId;
//        this.campaignTypeId = other.campaignTypeId;
        this.name = other.name;
        this.tabName = other.tabName;
        this.banner = other.banner;
        this.resType = other.resType;
        this.skeleton = other.skeleton;
        this.startDay = other.startDay;
        this.sort = other.sort;
        this.duration = other.duration;
        this.helpMsg = other.helpMsg;
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
     * 活动名称
     */
    @ExcelProperty("活动名称")
    @Excel(name = "活动名称", width = 15)
    private java.lang.String name;

    /**
     * 活动页签名称
     */
    @ExcelProperty("活动页签名称")
    @Excel(name = "活动页签名称", width = 15)
    private java.lang.String tabName;

    /**
     * 活动宣传背景图
     */
    @ExcelProperty("活动宣传背景图")
    @Excel(name = "活动宣传背景图", width = 15)
    private java.lang.String banner;

    /**
     * 资源格式: 1.骨骼, 2.序列帧, 3.图片
     */
    @ExcelProperty("资源类型")
    @Excel(name = "资源类型", width = 15)
    private java.lang.String resType;
    /**
     * 骨骼动画资源
     */
    @ExcelProperty("骨骼动画资源")
    @Excel(name = "骨骼动画资源", width = 15)
    private java.lang.String skeleton;

    /**
     * 开始时间(开服第n天, e.g. 0表示开服第1天)
     */
    @ExcelProperty("开始时间")
    @Excel(name = "开始时间", width = 15)
    private java.lang.Integer startDay;

    /**
     * 排序
     */
    @ExcelProperty("排序")
    @Excel(name = "排序", width = 15)
    private java.lang.Integer sort;

    /**
     * 持续时间(天)
     */
    @ExcelProperty("持续时间(天)")
    @Excel(name = "持续时间(天)", width = 15)
    private java.lang.Integer duration;

    /**
     * 帮助信息
     */
    @ExcelProperty("帮助信息")
    @Excel(name = "帮助信息", width = 15)
    private java.lang.String helpMsg;

    @TableField(exist = false)
    private List<OpenServiceCampaignGiftDetailItem> detailItemList;

}

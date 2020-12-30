package org.jeecg.modules.game.entity;

import com.alibaba.excel.annotation.ExcelProperty;
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
 * @description 开服活动-单笔好礼活动参数
 * @date 2020-12-23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("game_open_service_campaign_single_gift_detail")
@Accessors(chain = true)
public class OpenServiceCampaignSingleGiftDetail extends BaseEntity {

    private static final long serialVersionUID = 1L;

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
     * 开始时间(开服第n天, e.g. 0表示开服第1天)
     */
    @ExcelProperty("开始时间(开服第n天)")
    @Excel(name = "开始时间(开服第n天)", width = 15)
    private java.lang.Integer startDay;

    /**
     * 持续时间(天)
     */
    @ExcelProperty("持续时间(天)")
    @Excel(name = "持续时间(天)", width = 15)
    private java.lang.Integer duration;

    /**
     * 活动页签名称
     */
    @ExcelProperty("活动页签名称")
    @Excel(name = "活动页签名称", width = 15)
    private java.lang.String tabName;

    /**
     * 排序
     */
    @ExcelProperty("排序")
    @Excel(name = "排序", width = 15)
    private java.lang.Integer sort;

    /**
     * 活动名称
     */
    @ExcelProperty("活动名称")
    @Excel(name = "活动名称", width = 15)
    private java.lang.String name;

    /**
     * 活动背景图
     */
    @ExcelProperty("活动背景图")
    @Excel(name = "活动背景图", width = 15)
    private java.lang.String banner;

    /**
     * 邮件标题
     */
    @ExcelProperty("邮件标题")
    @Excel(name = "邮件标题", width = 15)
    private java.lang.String emailTitle;

    /**
     * 邮件描述
     */
    @ExcelProperty("邮件描述")
    @Excel(name = "邮件描述", width = 15)
    private java.lang.String emailContent;

    /**
     * 帮助信息
     */
    @ExcelProperty("帮助信息")
    @Excel(name = "帮助信息", width = 15)
    private java.lang.String helpMsg;
}

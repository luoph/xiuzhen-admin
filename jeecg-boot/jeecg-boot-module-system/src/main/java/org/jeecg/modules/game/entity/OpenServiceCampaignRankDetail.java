package org.jeecg.modules.game.entity;

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
 * @description 开服活动-开服排行-活动明细(3级)
 * @date 2020-12-21
 */
@Data
@TableName("game_open_service_campaign_rank_detail")
@EqualsAndHashCode(callSuper = true)
public class OpenServiceCampaignRankDetail extends BaseEntity {

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
     * 排行类型 open_service_campaign_rank_type.rank_type e.g. 1.境界冲榜, 2.功法冲榜
     */
    @ExcelProperty("排行类型")
    @Excel(name = "排行类型", width = 15)
    private java.lang.Integer rankType;

    /**
     * 排序
     */
    @ExcelProperty("排序")
    @Excel(name = "排序", width = 15)
    private java.lang.Integer sort;

    /**
     * 开始时间(开服第n天, e.g. 0表示开服第1天)
     */
    @ExcelProperty("开始时间")
    @Excel(name = "开始时间", width = 15)
    private java.lang.Integer startDay;

    /**
     * 持续时间(天)
     */
    @ExcelProperty("持续时间(天)")
    @Excel(name = "持续时间(天)", width = 15)
    private java.lang.Integer duration;

    /**
     * 活动宣传背景图
     */
    @ExcelProperty("活动宣传背景图")
    @Excel(name = "活动宣传背景图", width = 15)
    private java.lang.String banner;

    /**
     * 活动宣传奖励图
     */
    @ExcelProperty("活动宣传奖励图")
    @Excel(name = "活动宣传奖励图", width = 15)
    private java.lang.String rewardImg;

    /**
     * 活动宣传仙力
     */
    @ExcelProperty("活动宣传仙力")
    @Excel(name = "活动宣传仙力", width = 15)
    private java.lang.Long combatPower;

    /**
     * 排行玩家数量
     */
    @ExcelProperty("排行玩家数量")
    @Excel(name = "排行玩家数量", width = 15)
    private java.lang.Integer rankNum;

    /**
     * 排名奖励邮件id
     */
    @ExcelProperty("排名奖励邮件id")
    @Excel(name = "排名奖励邮件id", width = 15)
    private java.lang.Long rankRewardEmail;

    /**
     * 达标奖励邮件id
     */
    @ExcelProperty("达标奖励邮件id")
    @Excel(name = "达标奖励邮件id", width = 15)
    private java.lang.Long standardRewardEmail;

    /**
     * 帮助信息
     */
    @ExcelProperty("帮助信息")
    @Excel(name = "帮助信息", width = 15)
    private java.lang.String helpMsg;
}

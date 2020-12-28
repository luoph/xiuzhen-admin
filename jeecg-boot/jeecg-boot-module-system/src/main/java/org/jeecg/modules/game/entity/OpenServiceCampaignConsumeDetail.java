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
 * @description 开服互动消耗配置
 * @date 2020-12-28
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@TableName("game_open_service_campaign_consume_detail")
public class OpenServiceCampaignConsumeDetail extends BaseEntity {

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
    @Excel(name = "页签id", width = 15)
    private java.lang.Integer campaignTypeId;

    /**
     * 开始时间(开服第n天, e.g. 0表示开服第1天)
     */
    @Excel(name = "开始时间(开服第n天)", width = 15)
    private java.lang.Integer startDay;

    /**
     * 持续时间(天)
     */
    @Excel(name = "持续时间(天)", width = 15)
    private java.lang.Integer duration;

    /**
     * 活动页签名称
     */
    @Excel(name = "页签名称", width = 15)
    private java.lang.String tabName;

    /**
     * 活动名称
     */
    @Excel(name = "活动名称", width = 15)
    private java.lang.String name;

    /**
     * 活动宣传背景图
     */
    @Excel(name = "活动宣传图", width = 15)
    private java.lang.String banner;

    /**
     * 消耗奖励邮件标题
     */
    @Excel(name = "消耗奖励邮件标题", width = 15)
    private java.lang.String consumeRewardEmailTitle;

    /**
     * 消耗奖励邮件内容
     */
    @Excel(name = "消耗奖励邮件内容", width = 15)
    private java.lang.String consumeRewardEmailContent;

    /**
     * 帮助信息
     */
    @Excel(name = "帮助信息", width = 15)
    private java.lang.String helpMsg;

}

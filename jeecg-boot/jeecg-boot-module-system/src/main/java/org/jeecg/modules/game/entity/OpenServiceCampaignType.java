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
 * @description 开服活动-类型(2级)
 * @date 2020-12-21
 */
@Data
@TableName("game_open_service_campaign_type")
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class OpenServiceCampaignType extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private java.lang.Integer id;

    /**
     * 开服活动id
     */
    @Excel(name = "开服活动id", width = 15)
    private java.lang.Integer campaignId;

    /**
     * 开服活动项类型(1.开服排行，2.开服礼包，3.单笔充值，4.寻宝，5.道具消耗)
     */
    @Excel(name = "开服活动项类型", width = 15)
    private java.lang.Integer type;

    /**
     * 排序
     */
    @Excel(name = "排序", width = 15)
    private java.lang.Integer sort;

    /**
     * 活动备注
     */
    @Excel(name = "活动备注", width = 15)
    private java.lang.String remark;
}

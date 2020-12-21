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
 * @description 开服活动(1级)
 * @date 2020-12-21
 */
@Data
@TableName("game_open_service_campaign")
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class OpenServiceCampaign extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 活动id
     */
    @TableId(type = IdType.AUTO)
    private java.lang.Integer id;

    /**
     * 活动名称
     */
    @Excel(name = "活动名称", width = 15)
    private java.lang.String name;

    /**
     * 服务器id，使用,分割
     */
    @Excel(name = "服务器id", width = 15)
    private java.lang.String serverIds;

    /**
     * 活动图标
     */
    @Excel(name = "活动图标", width = 15)
    private java.lang.String icon;

    /**
     * 活动状态
     */
    @Excel(name = "活动状态", width = 15)
    private java.lang.Integer status;

    /**
     * 自动开启
     */
    @Excel(name = "自动开启", width = 15)
    private java.lang.Integer autoOpen;

    /**
     * 活动备注
     */
    @Excel(name = "活动备注", width = 15)
    private java.lang.String remark;
}

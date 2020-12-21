package org.jeecg.modules.game.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;
import org.jeecg.common.constant.TimeConstant;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 激活码活动
 * @date 2020-01-07
 */
@Data
@Accessors(chain = true)
@TableName("game_redeem_activity")
public class GameRedeemActivity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private java.lang.Long id;

    /**
     * 激活码名称
     */
    @Excel(name = "激活码名称", width = 15)
    private java.lang.String name;

    /**
     * 礼包说明
     */
    @Excel(name = "礼包说明", width = 15)
    private java.lang.String summary;

    /**
     * 限制类型
     */
    @Excel(name = "限制类型", width = 15)
    private java.lang.Integer limitType;

    /**
     * 分组id
     */
    @Excel(name = "分组id", width = 15)
    private java.lang.Integer groupId;

    /**
     * 限制渠道id
     */
    @Excel(name = "限制渠道id", width = 15)
    private java.lang.String channelIds;

    /**
     * 限制区服id
     */
    @Excel(name = "限制区服id", width = 15)
    private java.lang.String serverIds;

    /**
     * 活动状态
     */
    @Excel(name = "活动状态", width = 15)
    private java.lang.Integer status;

    /**
     * 奖励
     */
    @Excel(name = "奖励", width = 15)
    private java.lang.String reward;

    /**
     * 备注
     */
    @Excel(name = "备注", width = 15)
    private java.lang.String remark;

    /**
     * 开始时间
     */
    @Excel(name = "开始时间", width = 15, format = TimeConstant.DEFAULT_TIME_FORMAT)
    @JsonFormat(timezone = TimeConstant.DEFAULT_TIMEZONE, pattern = TimeConstant.DEFAULT_TIME_FORMAT)
    @DateTimeFormat(pattern = TimeConstant.DEFAULT_TIME_FORMAT)
    private java.util.Date startTime;

    /**
     * 结束时间
     */
    @Excel(name = "结束时间", width = 15, format = TimeConstant.DEFAULT_TIME_FORMAT)
    @JsonFormat(timezone = TimeConstant.DEFAULT_TIMEZONE, pattern = TimeConstant.DEFAULT_TIME_FORMAT)
    @DateTimeFormat(pattern = TimeConstant.DEFAULT_TIME_FORMAT)
    private java.util.Date endTime;

    /**
     * 创建时间
     */
    @Excel(name = "创建时间", width = 15, format = TimeConstant.DEFAULT_TIME_FORMAT)
    @JsonFormat(timezone = TimeConstant.DEFAULT_TIMEZONE, pattern = TimeConstant.DEFAULT_TIME_FORMAT)
    @DateTimeFormat(pattern = TimeConstant.DEFAULT_TIME_FORMAT)
    private java.util.Date createTime;

    /**
     * 更新时间
     */
    @Excel(name = "更新时间", width = 15, format = TimeConstant.DEFAULT_TIME_FORMAT)
    @JsonFormat(timezone = TimeConstant.DEFAULT_TIMEZONE, pattern = TimeConstant.DEFAULT_TIME_FORMAT)
    @DateTimeFormat(pattern = TimeConstant.DEFAULT_TIME_FORMAT)
    private java.util.Date updateTime;
}

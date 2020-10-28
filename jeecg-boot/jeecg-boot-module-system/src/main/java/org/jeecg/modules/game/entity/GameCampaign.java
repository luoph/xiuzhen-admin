package org.jeecg.modules.game.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecg.common.constant.TimeConstant;
import org.jeecg.common.system.base.entity.BaseEntity;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.List;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 活动配置
 * @date 2020-10-15
 */
@Data
@TableName("game_campaign")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class GameCampaign extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 活动id
     */
    @TableId(type = IdType.AUTO)
    private java.lang.Long id;

    /**
     * 活动类型: 1.节日活动
     */
    @Excel(name = "活动类型", width = 15)
    private java.lang.Integer type;

    /**
     * 活动名称，后台显示
     */
    @Excel(name = "活动名称（后台显示）", width = 15)
    private java.lang.String name;

    /**
     * 活动标语（描述）
     */
    @Excel(name = "活动标语（描述）", width = 15)
    private java.lang.String description;

    /**
     * 活动名称，游戏中显示
     */
    @Excel(name = "活动名称（游戏中显示）", width = 15)
    private java.lang.String showName;

    /**
     * 活动图标
     */
    @Excel(name = "活动图标", width = 15)
    private java.lang.String icon;

    /**
     * 活动宣传图
     */
    @Excel(name = "活动宣传图", width = 15)
    private java.lang.String banner;

    /**
     * 活动状态: 0.关闭, 1.开启（备用字段，默认全部为开启）
     */
    @Excel(name = "活动状态", width = 15)
    private java.lang.Integer status;

    /**
     * 到达活动时间后自动开启
     */
    @Excel(name = "自动开启", width = 15)
    private java.lang.Integer autoOpen;

    /**
     * 区服Id 使用,分割
     */
    @Excel(name = "区服Id", width = 15)
    private java.lang.String serverIds;

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

    @TableField(exist = false)
    private List<GameCampaignType> typeList;
}

package org.jeecg.modules.game.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import org.jeecg.common.constant.TimeConstant;
import org.jeecg.common.system.base.entity.BaseEntity;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 游戏公告
 * @date 2019-12-13
 */
@Data
@TableName("game_notice")
@Accessors(chain = true)
@ApiModel(value = "GameNotice", description = "游戏公告")
public class GameNotice extends BaseEntity {

    /**
     * 自增主键
     */
    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "自增主键")
    private java.lang.Integer id;
    /**
     * 公告类型 1 - 渠道公告 2 - 滚动公告
     */
    @Excel(name = "公告类型 1 - 渠道公告 2 - 滚动公告", width = 15)
    @ApiModelProperty(value = "公告类型 1 - 渠道公告 2 - 滚动公告")
    private java.lang.Integer noticeType;
    /**
     * 标题
     */
    @Excel(name = "标题", width = 15)
    @ApiModelProperty(value = "标题")
    private java.lang.String title;
    /**
     * 公告内容
     */
    @Excel(name = "公告内容", width = 15)
    @ApiModelProperty(value = "公告内容")
    private java.lang.Object content;
    /**
     * 开始时间
     */
    @Excel(name = "开始时间", width = 20, format = TimeConstant.DEFAULT_TIME_FORMAT)
    @JsonFormat(timezone = TimeConstant.DEFAULT_TIMEZONE, pattern = TimeConstant.DEFAULT_TIME_FORMAT)
    @DateTimeFormat(pattern = TimeConstant.DEFAULT_TIME_FORMAT)
    @ApiModelProperty(value = "开始时间")
    private java.util.Date beginTime;
    /**
     * 结束时间
     */
    @Excel(name = "结束时间", width = 20, format = TimeConstant.DEFAULT_TIME_FORMAT)
    @JsonFormat(timezone = TimeConstant.DEFAULT_TIMEZONE, pattern = TimeConstant.DEFAULT_TIME_FORMAT)
    @DateTimeFormat(pattern = TimeConstant.DEFAULT_TIME_FORMAT)
    @ApiModelProperty(value = "结束时间")
    private java.util.Date endTime;
    /**
     * 状态 1 - 可用 0 - 不可用
     */
    @Excel(name = "状态 1 - 可用 0 - 不可用", width = 15)
    @ApiModelProperty(value = "状态 1 - 可用 0 - 不可用")
    private java.lang.Integer status;
    /**
     * 滚动公告间隔
     */
    @Excel(name = "滚动公告间隔", width = 15)
    @ApiModelProperty(value = "滚动公告间隔")
    private java.lang.Integer interval;
}

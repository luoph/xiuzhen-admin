package cn.youai.xiuzhen.game.entity;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecg.common.constant.TimeConstant;
import org.jeecg.common.system.base.entity.BaseEntity;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 游戏渠道服配置
 * @date 2019-12-11
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@TableName("game_channel_server")
@ApiModel(value = "GameChannelServer", description = "游戏渠道服配置")
public class GameChannelServer extends BaseEntity {

    /**
     * 自增主键
     */
    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "自增主键")
    private java.lang.Long id;
    /**
     * 服务器id
     */
    @Excel(name = "服务器id", width = 15)
    @ApiModelProperty(value = "服务器id")
    private java.lang.Integer serverId;

    @ExcelIgnore
    @TableField(exist = false)
    private String serverIds;

    /**
     * 渠道id
     */
    @Excel(name = "渠道id", width = 15)
    @ApiModelProperty(value = "渠道id")
    private java.lang.Integer channelId;

    @Excel(name = "排序", width = 15)
    @ApiModelProperty(value = "排序")
    private java.lang.Integer position;
    /**
     * 删除状态
     */
    @Excel(name = "删除状态", width = 15)
    @Dict(dicCode = "del_flag")
    @ApiModelProperty(value = "删除状态")
    private java.lang.Integer delFlag;

    @Excel(name = "数据统计状态", width = 15)
    @Dict(dicCode = "no_need_count")
    @ApiModelProperty(value = "数据统计状态")
    private java.lang.Integer noNeedCount;

    @TableField(exist = false)
    private String serverName;

    @TableField(exist = false)
    private String channelSimpleName;

    @TableField(exist = false)
    private Integer serverStatus;

    @TableField(exist = false)
    private Integer isMaintain;

    @TableField(exist = false)
    @JsonFormat(timezone = TimeConstant.DEFAULT_TIMEZONE, pattern = TimeConstant.DEFAULT_DATE_FORMAT)
    @DateTimeFormat(pattern = TimeConstant.DEFAULT_DATE_FORMAT)
    private Date openTime;

    @TableField(exist = false)
    @JsonFormat(timezone = TimeConstant.DEFAULT_TIMEZONE, pattern = TimeConstant.DEFAULT_TIME_FORMAT)
    @DateTimeFormat(pattern = TimeConstant.DEFAULT_TIME_FORMAT)
    private Date onlineTime;

}

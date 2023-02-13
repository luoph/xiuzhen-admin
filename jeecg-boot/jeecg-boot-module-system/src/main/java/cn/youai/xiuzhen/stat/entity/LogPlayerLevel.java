package cn.youai.xiuzhen.stat.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import org.jeecg.common.constant.TimeConstant;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Description: 境界日志
 * @Author: jeecg-boot
 * @Date: 2023-02-10
 * @Version: V1.0
 */
@Data
@Accessors(chain = true)
@TableName("log_player_level")
@ApiModel(value = "log_player_level对象", description = "境界日志")
public class LogPlayerLevel implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private Long id;

    /**
     * 服务器id
     */
    @Excel(name = "服务器id", width = 15)
    @ApiModelProperty(value = "服务器id")
    private Integer serverId;

    /**
     * 玩家id
     */
    @Excel(name = "玩家id", width = 15)
    @ApiModelProperty(value = "玩家id")
    private Long playerId;

    @TableField(exist = false)
    @Excel(name = "玩家昵称", width = 15)
    private String nickname;

    /**
     * 境界等级
     */
    @Excel(name = "境界等级", width = 15)
    @ApiModelProperty(value = "境界等级")
    private Integer level;

    /**
     * 战力
     */
    @Excel(name = "战力", width = 15)
    @ApiModelProperty(value = "战力")
    private Long combatPower;

    /**
     * 战力补偿
     */
    @Excel(name = "战力补偿", width = 15)
    @ApiModelProperty(value = "战力补偿")
    private Long combatPowerCompensation;

    @TableField(exist = false)
    @Excel(name = "总战力", width = 15)
    public Long totalCombatPower;

    /**
     * 累计充值
     */
    @Excel(name = "累计充值", width = 15)
    @ApiModelProperty(value = "累计充值")
    private BigDecimal rechargeAmount;

    /**
     * 创建时间
     */
    @Excel(name = "创建时间", width = 15, format = TimeConstant.DEFAULT_TIME_FORMAT)
    @JsonFormat(timezone = TimeConstant.DEFAULT_TIMEZONE, pattern = TimeConstant.DEFAULT_TIME_FORMAT)
    @DateTimeFormat(pattern = TimeConstant.DEFAULT_TIME_FORMAT)
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    /**
     * 创建日期
     */
    @Excel(name = "创建日期", width = 15, format = TimeConstant.DEFAULT_DATE_FORMAT)
    @JsonFormat(timezone = TimeConstant.DEFAULT_TIMEZONE, pattern = TimeConstant.DEFAULT_DATE_FORMAT)
    @DateTimeFormat(pattern = TimeConstant.DEFAULT_DATE_FORMAT)
    @ApiModelProperty(value = "创建日期")
    private Date createDate;

    @TableField(exist = false)
    @Excel(name = "创角时间", width = 15, format = TimeConstant.DEFAULT_TIME_FORMAT)
    @JsonFormat(timezone = TimeConstant.DEFAULT_TIMEZONE, pattern = TimeConstant.DEFAULT_TIME_FORMAT)
    @DateTimeFormat(pattern = TimeConstant.DEFAULT_TIME_FORMAT)
    private Date registerTime;

    @TableField(exist = false)
    @Excel(name = "创角天数", width = 15)
    private Integer playDays;
}

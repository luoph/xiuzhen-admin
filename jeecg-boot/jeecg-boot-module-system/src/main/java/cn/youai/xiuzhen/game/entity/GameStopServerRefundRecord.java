package cn.youai.xiuzhen.game.entity;

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
import org.jeecg.common.constant.TimeConstant;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;

/**
 * @Description: 删档返还
 * @Author: jeecg-boot
 * @Date: 2022-12-14
 * @Version: V1.0
 */
@Data
@TableName("game_stop_server_refund_record")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "game_stop_server_refund_record对象", description = "删档返还")
public class GameStopServerRefundRecord implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @Excel(name = "id", width = 15)
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private java.lang.Integer id;

    /**
     * 停服区服版本类型: 1.普通服, 2.BT服
     */
    @Excel(name = "停服区服版本类型", width = 15)
    @ApiModelProperty(value = "停服区服版本类型")
    private java.lang.Integer sourceServerVersionType;

    /**
     * 停服的服务器id
     */
    @Excel(name = "停服的服务器id", width = 15)
    @ApiModelProperty(value = "停服的服务器id")
    private java.lang.Integer sourceServerId;

    /**
     * 停服的玩家id
     */
    @Excel(name = "停服的玩家id", width = 15)
    @ApiModelProperty(value = "停服的玩家id")
    private java.lang.Long sourcePlayerId;

    /**
     * 返还的服务器id
     */
    @Excel(name = "返还的服务器id", width = 15)
    @ApiModelProperty(value = "返还的服务器id")
    private java.lang.Integer targetServerId;

    /**
     * 返还的玩家id
     */
    @Excel(name = "返还的玩家id", width = 15)
    @ApiModelProperty(value = "返还的玩家id")
    private java.lang.Long targetPlayerId;

    /**
     * 玩家账号
     */
    @Excel(name = "玩家账号", width = 15)
    @ApiModelProperty(value = "玩家账号")
    @TableField(exist = false)
    private String account;

    /**
     * 角色昵称
     */
    @Excel(name = "角色昵称", width = 15)
    @ApiModelProperty(value = "角色昵称")
    @TableField(exist = false)
    private String nickname;

    /**
     * 充值总金额
     */
    @Excel(name = "充值总金额", width = 15)
    @ApiModelProperty(value = "充值总金额")
    private java.math.BigDecimal sourceAmount;

    /**
     * 返还总仙玉
     */
    @Excel(name = "返还总仙玉", width = 15)
    @ApiModelProperty(value = "返还总仙玉")
    private java.lang.Long targetNum;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    @Excel(name = "创建时间", width = 20, format = TimeConstant.DEFAULT_TIME_FORMAT)
    @JsonFormat(timezone = TimeConstant.DEFAULT_TIMEZONE, pattern = TimeConstant.DEFAULT_TIME_FORMAT)
    @DateTimeFormat(pattern = TimeConstant.DEFAULT_TIME_FORMAT)
    private java.util.Date createTime;
}

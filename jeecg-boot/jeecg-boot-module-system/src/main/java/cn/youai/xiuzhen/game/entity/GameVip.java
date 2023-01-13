package cn.youai.xiuzhen.game.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecg.common.system.base.entity.BaseEntity;
import org.jeecgframework.poi.excel.annotation.Excel;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description VIP
 * @date 2023-01-13
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@TableName("game_vip")
@ApiModel(value = "GameVip", description = "VIP")
public class GameVip extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 自增主键
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 玩家id
     */
    @Excel(name = "玩家ID", width = 15)
    private Long playerId;

    /**
     * 玩家id
     */
    @TableField(exist = false)
    private String playerIds;

    @TableField(exist = false)
    @Excel(name = "渠道", width = 15)
    private String channel;

    @TableField(exist = false)
    @Excel(name = "Sdk渠道", width = 15)
    private String sdkChannel;

    @TableField(exist = false)
    @Excel(name = "最后充值", width = 15)
    private BigDecimal lastPay;

    @TableField(exist = false)
    @Excel(name = "区服id", width = 15)
    private Integer serverId;

    @TableField(exist = false)
    private Long orderId;

    @TableField(exist = false)
    @Excel(name = "玩家昵称", width = 15)
    private String nickname;

    @TableField(exist = false)
    @Excel(name = "玩家等级", width = 15)
    private Integer level;

    @TableField(exist = false)
    @Excel(name = "排名", width = 15)
    private Integer rank;

    /**
     * 充值金额
     */
    @TableField(exist = false)
    @Excel(name = "充值金额", width = 15)
    private BigDecimal payAmount;

    @TableField(exist = false)
    @Excel(name = "最后充值时间", width = 15)
    private Date lastPayTime;

    @TableField(exist = false)
    @Excel(name = "最后登录时间", width = 15)
    private Date lastLoginTime;

    @TableField(exist = false)
    @Excel(name = "创角时间", width = 15)
    private Date registerTime;

    @TableField(exist = false)
    @Excel(name = "创角天数", width = 15)
    private Integer playDays;

    @TableField(exist = false)
    @Excel(name = "充值预警天数", width = 15)
    private Integer lastPayDays;

    @TableField(exist = false)
    @Excel(name = "登录预警天数", width = 15)
    private Integer lastLoginDays;

}

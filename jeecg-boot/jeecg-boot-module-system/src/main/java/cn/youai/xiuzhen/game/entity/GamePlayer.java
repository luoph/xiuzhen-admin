package cn.youai.xiuzhen.game.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import org.jeecgframework.poi.excel.annotation.Excel;

import java.util.Date;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 玩家
 * @date 2019-12-10
 */
@Data
@Accessors(chain = true)
@TableName("game_player")
@ApiModel(value = "GamePlayer", description = "玩家")
public class GamePlayer {

    /**
     * 自增主键
     */
    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "自增主键")
    private Long id;

    /**
     * 玩家id
     */
    @Excel(name = "玩家id", width = 15)
    @ApiModelProperty(value = "玩家id")
    private Long playerId;

    /**
     * 服务器id
     */
    @Excel(name = "服务器id", width = 15)
    @ApiModelProperty(value = "服务器id")
    private Integer serverId;

    /**
     * 初始服务器id
     */
    @Excel(name = "创角区服id", width = 15)
    @ApiModelProperty(value = "创角区服id")
    private Integer sid;

    /**
     * 玩家账号
     */
    @Excel(name = "玩家账号", width = 15)
    @ApiModelProperty(value = "玩家账号")
    private String account;

    /**
     * 角色昵称
     */
    @Excel(name = "角色昵称", width = 15)
    @ApiModelProperty(value = "角色昵称")
    private String nickname;

    /**
     * distinctId
     */
    @Excel(name = "distinctId", width = 15)
    @ApiModelProperty(value = "distinctId")
    private String distinctId;

    /**
     * 1 - 正常 0 - 废弃
     */
    @Excel(name = "状态", width = 15)
    @ApiModelProperty(value = "状态")
    private Integer status;

    /**
     * 玩家渠道
     */
    @Excel(name = "渠道", width = 15)
    @ApiModelProperty(value = "渠道")
    private String channel;

    /**
     * Sdk渠道
     */
    @Excel(name = "Sdk渠道", width = 15)
    @ApiModelProperty(value = "Sdk渠道")
    private String sdkChannel;

    /**
     * 角色头像
     */
    @Excel(name = "角色头像", width = 15)
    @ApiModelProperty(value = "角色头像")
    private String avatar;

    /**
     * 性别
     */
    @Excel(name = "性别", width = 15)
    @ApiModelProperty(value = "性别")
    private Integer sex;

    /**
     * 境界等级
     */
    @Excel(name = "等级", width = 15)
    @ApiModelProperty(value = "等级")
    private Integer level;

    /**
     * 界: 1.人界, 2.飞升灵界, 3.飞升仙界, 4.飞升圣界
     */
    @Excel(name = "境界", width = 15)
    @ApiModelProperty(value = "境界")
    private Integer realm;

    @Excel(name = "累计充值", width = 15)
    @TableField(exist = false)
    private java.math.BigDecimal totalPayAmount;

    /**
     * 是否跳过战斗动画 0-否 1-是
     */
    @Excel(name = "是否跳过战斗动画", width = 15)
    @ApiModelProperty(value = "是否跳过战斗动画")
    private Integer skipCartoon;

    /**
     * 背包大小
     */
    @Excel(name = "背包大小", width = 15)
    @ApiModelProperty(value = "背包大小")
    private Integer backpackSize;

    /**
     * 背包等级
     */
    @Excel(name = "背包等级", width = 15)
    @ApiModelProperty(value = "背包等级")
    private Integer backpackLevel;

    /**
     * 修为值
     */
    @Excel(name = "修为值", width = 15)
    @ApiModelProperty(value = "修为值")
    private Long practiceValue;

    /**
     * 修炼年数
     */
    @Excel(name = "修炼年数", width = 15)
    @ApiModelProperty(value = "修炼年数")
    private Integer practiceYear;

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

    /**
     * 魅力值
     */
    @Excel(name = "魅力值", width = 15)
    @ApiModelProperty(value = "魅力值")
    private Long charmValue;

    /**
     * vip等级
     */
    @Excel(name = "vip等级", width = 15)
    @ApiModelProperty(value = "vip等级")
    private Integer vipLevel;

    /**
     * 种族id
     */
    @Excel(name = "种族", width = 15)
    @ApiModelProperty(value = "种族")
    private Integer race;

    /**
     * 本命灵根
     */
    @Excel(name = "本命灵根", width = 15)
    @ApiModelProperty(value = "本命灵根")
    private Integer spiritRootCode;

    /**
     * 修为加持状态0-未开启 1-已开启 2-已结束
     */
    @Excel(name = "修为加持状态", width = 15)
    @ApiModelProperty(value = "修为加持状态")
    private Integer practiceState;

    /**
     * 醍醐灌顶开启时间戳
     */
    @Excel(name = "醍醐灌顶开启时间", width = 15)
    @ApiModelProperty(value = "醍醐灌顶开启时间")
    private Long openPracticeTime;

    /**
     * 渡劫增加成功率百分比
     */
    @Excel(name = "渡劫成功率", width = 15)
    @ApiModelProperty(value = "渡劫成功率")
    private Integer successRate;

    /**
     * 修炼值结算时间
     */
    @Excel(name = "修炼值结算时间", width = 15)
    @ApiModelProperty(value = "修炼值结算时间")
    private Date settleTime;

    /**
     * 当前主线任务id
     */
    @Excel(name = "当前主线任务id", width = 15)
    @ApiModelProperty(value = "当前主线任务id")
    private Integer mainTaskId;

    /**
     * 登录设备IP
     */
    @Excel(name = "登录设备IP", width = 15)
    @ApiModelProperty(value = "登录设备IP")
    private String loginIp;

    /**
     * 登录时间
     */
    @Excel(name = "登录时间", width = 15)
    @ApiModelProperty(value = "登录时间")
    private Date loginTime;

    /**
     * 登出时间
     */
    @Excel(name = "登出时间", width = 15)
    @ApiModelProperty(value = "登出时间")
    private Date logoutTime;

    /**
     * 境界更新时间
     */
    @Excel(name = "等级更新时间", width = 15)
    @ApiModelProperty(value = "等级更新时间")
    private Date levelUpdateTime;

    /**
     * 创建时间
     */
    @Excel(name = "创角时间", width = 15)
    @ApiModelProperty(value = "创角时间")
    private Date createTime;

    /**
     * 创建日期
     */
    @Excel(name = "创角日期", width = 15)
    @ApiModelProperty(value = "创角日期")
    private Date createDate;

    /**
     * 更新时间
     */
    @Excel(name = "更新时间", width = 15)
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

    @TableField(exist = false)
    private Long vipId;
}

/*
 * create by mybatis-plus-generator  https://github.com/xiweile
 */
package org.jeecg.modules.player.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecgframework.poi.excel.annotation.Excel;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 玩家表
 * </p>
 *
 * @author ocean
 * @since 2020-07-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Player implements Serializable {

    private static final long serialVersionUID = 1L;

    @Excel(name = "玩家id", width = 15)
    @TableId(value = "id", type = IdType.INPUT)
    private Long id;

    /**
     * 玩家账号
     */
    @Excel(name = "玩家帐号", width = 15)
    private String account;

    /**
     * 角色昵称
     */
    @Excel(name = "角色昵称", width = 15)
    private String nickname;

    /**
     * 角色头像
     */
    @Excel(name = "角色头像", width = 15)
    private String avatar;

    /**
     * 性别
     */
    @Excel(name = "性别", width = 15)
    private Integer sex;

    /**
     * 是否开启音乐 0否 1是
     */
    @Excel(name = "音乐开关", width = 15)
    private Integer openMusic;

    /**
     * 是否开启音效 0否 1是
     */
    @Excel(name = "音效开关", width = 15)
    private Integer openSound;

    /**
     * 境界等级
     */
    @Excel(name = "等级", width = 15)
    private Integer level;

    /**
     * 界: 1.人界, 2.飞升灵界, 3.飞升仙界, 4.飞升圣界
     */
    @Excel(name = "境界", width = 15)
    private Integer realm;

    /**
     * 是否跳过战斗动画 0-否 1-是
     */
    @Excel(name = "是否跳过战斗动画", width = 15)
    private Integer skipCartoon;

    /**
     * 背包大小
     */
    @Excel(name = "背包大小", width = 15)
    private Integer backpackSize;

    /**
     * 背包等级
     */
    @Excel(name = "背包等级", width = 15)
    private Integer backpackLevel;

    /**
     * 修为值
     */
    @Excel(name = "修为值", width = 15)
    private Long practiceValue;

    /**
     * 修炼年数
     */
    @Excel(name = "修炼年数", width = 15)
    private Integer practiceYear;

    /**
     * 战力
     */
    @Excel(name = "战力", width = 15)
    private Long combatPower;

    /**
     * vip等级
     */
    @Excel(name = "vip等级", width = 15)
    private Integer vipLevel;

    /**
     * 种族id
     */
    @Excel(name = "种族id", width = 15)
    private Integer race;

    /**
     * 修为加持状态0-未开启 1-已开启 2-已结束
     */
    @Excel(name = "修为加持状态", width = 15)
    private Integer practiceState;

    /**
     * 渡劫增加成功率百分比
     */
    @Excel(name = "渡劫增加成功率", width = 15)
    private Integer successRate;

    /**
     * 修炼值结算时间
     */
    @Excel(name = "修炼值结算时间", width = 15)
    private Date settleTime;

    /**
     * 登录设备IP
     */
    @Excel(name = "登录IP", width = 15)
    private String loginIp;

    /**
     * 登录时间
     */
    @Excel(name = "登录时间", width = 15)
    private Date loginTime;

    /**
     * 登出时间
     */
    @Excel(name = "登出时间", width = 15)
    private Date logoutTime;

    /**
     * 境界更新时间
     */
    @Excel(name = "境界更新时间", width = 15)
    private Date levelUpdateTime;

    /**
     * 创建时间
     */
    @Excel(name = "创建时间", width = 15)
    private Date createTime;

    /**
     * 更新时间
     */
    @Excel(name = "更新时间", width = 15)
    @TableField(update = "now()")
    private Date updateTime;
}

package org.jeecg.modules.player.entity;

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
 * @description 玩家注册信息
 * @date 2020-01-04
 */
@Data
@TableName("player_register_info")
@Accessors(chain = true)
public class PlayerRegisterInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private java.lang.Long id;

    /**
     * 帐号
     */
    @Excel(name = "帐号", width = 15)
    private java.lang.String account;

    /**
     * 玩家id
     */
    @Excel(name = "玩家id", width = 15)
    private java.lang.Long playerId;

    /**
     * 服务器id
     */
    @Excel(name = "服务器id", width = 15)
    private java.lang.Integer serverId;

    /**
     * 出身id
     */
    @Excel(name = "出身id", width = 15)
    private java.lang.Integer birthId;

    /**
     * 角色名称
     */
    @Excel(name = "角色名称", width = 15)
    private java.lang.String name;

    /**
     * 渠道
     */
    @Excel(name = "渠道", width = 15)
    private java.lang.String ip;

    /**
     * 渠道
     */
    @Excel(name = "渠道", width = 15)
    private java.lang.String channel;

    /**
     * imei
     */
    @Excel(name = "imei", width = 15)
    private java.lang.String imei;

    /**
     * mac
     */
    @Excel(name = "mac", width = 15)
    private java.lang.String mac;

    /**
     * idfa
     */
    @Excel(name = "idfa", width = 15)
    private java.lang.String idfa;

    /**
     * 手机品牌
     */
    @Excel(name = "手机品牌", width = 15)
    private java.lang.String vendor;

    /**
     * 手机型号
     */
    @Excel(name = "手机型号", width = 15)
    private java.lang.String model;

    /**
     * 系统名字
     */
    @Excel(name = "系统名字", width = 15)
    private java.lang.String system;

    /**
     * 系统版本
     */
    @Excel(name = "系统版本", width = 15)
    private java.lang.String systemVersion;

    /**
     * 网络类型
     */
    @Excel(name = "网络类型", width = 15)
    private java.lang.String network;

    /**
     * version_name
     */
    @Excel(name = "version_name", width = 15)
    private java.lang.String versionName;

    /**
     * version_code
     */
    @Excel(name = "version_code", width = 15)
    private java.lang.String versionCode;

    /**
     * 平台
     */
    @Excel(name = "平台", width = 15)
    private java.lang.String platform;

    /**
     * 创建日期
     */
    @Excel(name = "创建日期", width = 15, format = TimeConstant.DEFAULT_DATE_FORMAT)
    @JsonFormat(timezone = TimeConstant.DEFAULT_TIMEZONE, pattern = TimeConstant.DEFAULT_DATE_FORMAT)
    @DateTimeFormat(pattern = TimeConstant.DEFAULT_DATE_FORMAT)
    private java.util.Date createDate;

    /**
     * 创建时间
     */
    @Excel(name = "创建时间", width = 15, format = TimeConstant.DEFAULT_DATE_FORMAT)
    @JsonFormat(timezone = TimeConstant.DEFAULT_TIMEZONE, pattern = TimeConstant.DEFAULT_DATE_FORMAT)
    @DateTimeFormat(pattern = TimeConstant.DEFAULT_DATE_FORMAT)
    private java.util.Date createTime;
}

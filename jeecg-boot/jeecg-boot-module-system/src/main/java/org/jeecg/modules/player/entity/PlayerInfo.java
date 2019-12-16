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
 * @description 玩家信息
 * @date 2019-12-14
 */
@Data
@TableName("bs_player_info")
@Accessors(chain = true)
public class PlayerInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private java.lang.Long id;

    /**
     * 全局uuid
     */
    @Excel(name = "全局uuid", width = 15)
    private java.lang.String uuid;

    /**
     * 玩家Id
     */
    @Excel(name = "玩家Id", width = 15)
    private java.lang.Long playerId;

    /**
     * 角色昵称
     */
    @Excel(name = "角色昵称", width = 15)
    private java.lang.String nickname;

    /**
     * 角色头像
     */
    @Excel(name = "角色头像", width = 15)
    private java.lang.String avatar;

    /**
     * 性别 1男 0女
     */
    @Excel(name = "性别 1男 0女", width = 15)
    private java.lang.Integer sex;

    /**
     * 音乐开关
     */
    @Excel(name = "音乐开关", width = 15)
    private java.lang.Integer openMusic;

    /**
     * 音效开关
     */
    @Excel(name = "音效开关", width = 15)
    private java.lang.Integer openSound;

    /**
     * 出身id
     */
    @Excel(name = "出身id", width = 15)
    private java.lang.Integer birthId;

    /**
     * 是否初始化
     */
    @Excel(name = "是否初始化", width = 15)
    private java.lang.Integer initialized;

    /**
     * createTime
     */
    @Excel(name = "createTime", width = 15, format = TimeConstant.DEFAULT_DATE_FORMAT)
    @JsonFormat(timezone = TimeConstant.DEFAULT_TIMEZONE, pattern = TimeConstant.DEFAULT_DATE_FORMAT)
    @DateTimeFormat(pattern = TimeConstant.DEFAULT_DATE_FORMAT)
    private java.util.Date createTime;

    /**
     * updateTime
     */
    @Excel(name = "updateTime", width = 15, format = TimeConstant.DEFAULT_DATE_FORMAT)
    @JsonFormat(timezone = TimeConstant.DEFAULT_TIMEZONE, pattern = TimeConstant.DEFAULT_DATE_FORMAT)
    @DateTimeFormat(pattern = TimeConstant.DEFAULT_DATE_FORMAT)
    private java.util.Date updateTime;
}

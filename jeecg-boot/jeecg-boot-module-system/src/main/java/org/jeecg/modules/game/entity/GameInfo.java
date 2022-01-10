package org.jeecg.modules.game.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecg.common.system.base.entity.BaseEntity;
import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 游戏信息
 * @date 2019-12-11
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@TableName("game_info")
@ApiModel(value = "GameInfo", description = "游戏信息")
public class GameInfo extends BaseEntity {

    /**
     * 自增主键
     */
    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "自增主键")
    private java.lang.Long id;
    /**
     * 游戏名称
     */
    @Excel(name = "游戏名称", width = 15)
    @ApiModelProperty(value = "游戏名称")
    private java.lang.String name;
    /**
     * YA_APPID
     */
    @Excel(name = "YA_APPID", width = 15)
    @ApiModelProperty(value = "YA_APPID")
    private java.lang.String yaAppId;
    /**
     * YA_APPKEY
     */
    @Excel(name = "YA_APPKEY", width = 15)
    @ApiModelProperty(value = "YA_APPKEY")
    private java.lang.String yaAppKey;
    /**
     * 服务器列表地址
     */
    @Excel(name = "服务器列表地址", width = 15)
    @ApiModelProperty(value = "serverUrl")
    private java.lang.String serverUrl;
    /**
     * 公告列表地址
     */
    @Excel(name = "公告列表地址", width = 15)
    @ApiModelProperty(value = "noticeUrl")
    private java.lang.String noticeUrl;
    /**
     * 帐号登录地址
     */
    @Excel(name = "帐号登录地址", width = 15)
    @ApiModelProperty(value = "loginUrl")
    private java.lang.String loginUrl;

    /**
     * 角色信息地址
     */
    @Excel(name = "角色信息地址", width = 15)
    @ApiModelProperty(value = "roleUrl")
    private java.lang.String roleUrl;

    /**
     * 实名认证地址
     */
    @Excel(name = "实名认证地址", width = 15)
    @ApiModelProperty(value = "authUrl")
    private java.lang.String authUrl;
    /**
     * gameSimpleName
     */
    @Excel(name = "gameSimpleName", width = 15)
    @ApiModelProperty(value = "gameSimpleName")
    private java.lang.String yaSimpleName;
    /**
     * gameAppKey
     */
    @Excel(name = "gameAppKey", width = 15)
    @ApiModelProperty(value = "gameAppKey")
    private java.lang.String yaGameKey;
    /**
     * 描述
     */
    @Excel(name = "描述", width = 15)
    @ApiModelProperty(value = "描述")
    private java.lang.String remark;
}

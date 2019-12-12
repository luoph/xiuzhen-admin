package org.jeecg.modules.game.entity;

import com.baomidou.mybatisplus.annotation.IdType;
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

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 游戏服配置
 * @date 2019-12-10
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("game_server")
@Accessors(chain = true)
@ApiModel(value = "GameServer", description = "游戏服配置")
public class GameServer extends BaseEntity {

    /**
     * 自增主键
     */
    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "自增主键")
    private java.lang.Integer id;
    /**
     * 游戏编号
     */
    @Excel(name = "游戏编号", width = 15)
    @ApiModelProperty(value = "游戏编号")
    private java.lang.Integer gameId;
    /**
     * 服务器名字
     */
    @Excel(name = "服务器名字", width = 15)
    @ApiModelProperty(value = "服务器名字")
    private java.lang.String name;
    /**
     * 服务器路径
     */
    @Excel(name = "服务器路径", width = 15)
    @ApiModelProperty(value = "服务器路径")
    private java.lang.String host;
    /**
     * 服务器端口
     */
    @Excel(name = "服务器端口", width = 15)
    @ApiModelProperty(value = "服务器端口")
    private java.lang.Integer port;
    /**
     * 登陆地址和端口
     */
    @Excel(name = "登陆地址和端口", width = 15)
    @ApiModelProperty(value = "登陆地址和端口")
    private java.lang.String loginUrl;
    /**
     * 服务器状态 0-正常 1-流畅 2-火爆 3-维护
     */
    @Excel(name = "服务器状态 0-正常 1-流畅 2-火爆 3-维护", width = 15)
    @ApiModelProperty(value = "服务器状态 0-正常 1-流畅 2-火爆 3-维护")
    @Dict(dicCode = "server_status")
    private java.lang.Integer status;
    /**
     * 推荐标识 0-普遍 1-推荐 2-新服 3-推荐新服
     */
    @Excel(name = "推荐标识 0-普遍 1-推荐 2-新服 3-推荐新服", width = 15)
    @ApiModelProperty(value = "推荐标识 0-普遍 1-推荐 2-新服 3-推荐新服")
    @Dict(dicCode = "recommend_status")
    private java.lang.Integer recommend;
    /**
     * 出错提示信息
     */
    @Excel(name = "出错提示信息", width = 15)
    @ApiModelProperty(value = "出错提示信息")
    private java.lang.String warning;
    /**
     * 客户端最小版本号
     */
    @Excel(name = "客户端最小版本号", width = 15)
    @ApiModelProperty(value = "客户端最小版本号")
    private java.lang.Integer minVersion;
    /**
     * 客户端最大版本号
     */
    @Excel(name = "客户端最大版本号", width = 15)
    @ApiModelProperty(value = "客户端最大版本号")
    private java.lang.Integer maxVersion;
    /**
     * 数据库路径
     */
    @Excel(name = "数据库路径", width = 15)
    @ApiModelProperty(value = "数据库路径")
    private java.lang.String dbHost;
    /**
     * 数据库端口
     */
    @Excel(name = "数据库端口", width = 15)
    @ApiModelProperty(value = "数据库端口")
    private java.lang.Integer dbPort;
    /**
     * 数据库用户名
     */
    @Excel(name = "数据库用户名", width = 15)
    @ApiModelProperty(value = "数据库用户名")
    private java.lang.String dbUser;
    /**
     * 数据库密码
     */
    @Excel(name = "数据库密码", width = 15)
    @ApiModelProperty(value = "数据库密码")
    private java.lang.String dbPassword;
    /**
     * 数据库名
     */
    @Excel(name = "数据库名", width = 15)
    @ApiModelProperty(value = "数据库名")
    private java.lang.String dbName;
    /**
     * 后台HTTP端口
     */
    @Excel(name = "后台HTTP端口", width = 15)
    @ApiModelProperty(value = "后台HTTP端口")
    private java.lang.Integer httpPort;
    /**
     * 排序字段
     */
    @Excel(name = "排序字段", width = 15)
    @ApiModelProperty(value = "排序字段")
    private java.lang.Integer position;
    /**
     * 服务器类型 0-混服 1-专服
     */
    @Excel(name = "服务器类型 0-混服 1-专服", width = 15)
    @ApiModelProperty(value = "服务器类型 0-混服 1-专服")
    @Dict(dicCode = "server_type")
    private java.lang.Integer type;
    /**
     * 合服时母服id
     */
    @Excel(name = "合服时母服id", width = 15)
    @ApiModelProperty(value = "合服时母服id")
    private java.lang.Integer pid;
    /**
     * 合服时间
     */
    @Excel(name = "合服时间", width = 20, format = TimeConstant.DEFAULT_TIME_FORMAT)
    @JsonFormat(timezone = TimeConstant.DEFAULT_TIMEZONE, pattern = TimeConstant.DEFAULT_TIME_FORMAT)
    @DateTimeFormat(pattern = TimeConstant.DEFAULT_TIME_FORMAT)
    @ApiModelProperty(value = "合服时间")
    private java.util.Date mergeTime;
    /**
     * 扩展字段
     */
    @Excel(name = "扩展字段", width = 15)
    @ApiModelProperty(value = "扩展字段")
    private java.lang.Object extra;
    /**
     * 服务器开服时间
     */
    @Excel(name = "服务器开服时间", width = 20, format = TimeConstant.DEFAULT_TIME_FORMAT)
    @JsonFormat(timezone = TimeConstant.DEFAULT_TIMEZONE, pattern = TimeConstant.DEFAULT_TIME_FORMAT)
    @DateTimeFormat(pattern = TimeConstant.DEFAULT_TIME_FORMAT)
    @ApiModelProperty(value = "服务器开服时间")
    private java.util.Date openTime;
}

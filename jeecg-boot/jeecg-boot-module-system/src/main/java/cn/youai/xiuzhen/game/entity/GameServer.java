package cn.youai.xiuzhen.game.entity;

import cn.hutool.core.util.StrUtil;
import cn.youai.enums.OutdatedType;
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
import org.jeecg.common.system.annotation.HiddenField;
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
     * 服务器备注
     */
    @Excel(name = "服务器备注", width = 15)
    @ApiModelProperty(value = "服务器备注")
    private java.lang.String remark;

    /**
     * 标签ID
     */
    @Excel(name = "标签ID", width = 15)
    @ApiModelProperty(value = "标签ID")
    private java.lang.Integer tagId;

    /**
     * 服务器路径
     */
    @Excel(name = "服务器Host", width = 15)
    @ApiModelProperty(value = "服务器Host")
    private java.lang.String host;

    /**
     * 客户端GM地址（处理跨域）
     */
    @Excel(name = "客户端GM", width = 15)
    @ApiModelProperty(value = "客户端GM")
    private java.lang.String clientGm;

    /**
     * 登录地址和端口
     */
    @Excel(name = "登录地址和端口", width = 15)
    @ApiModelProperty(value = "登录地址和端口")
    private java.lang.String loginUrl;

    /**
     * 服务器状态 0-正常 1-流畅 2-火爆 3-维护
     */
    @Excel(name = "服务器状态", width = 15)
    @ApiModelProperty(value = "服务器状态")
    @Dict(dicCode = "server_status")
    private java.lang.Integer status;

    @Excel(name = "维护状态", width = 15)
    @ApiModelProperty(value = "维护状态")
    private java.lang.Integer isMaintain;

    /**
     * 推荐标识 0-普遍 1-推荐 2-新服 3-推荐新服
     */
    @Excel(name = "推荐标识", width = 15)
    @ApiModelProperty(value = "推荐标识")
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
     * 数据库主机
     */
    @HiddenField
    private java.lang.String dbHost;

    /**
     * 数据库端口
     */
    @HiddenField
    private java.lang.Integer dbPort;

    /**
     * 数据库用户名
     */
    @HiddenField
    private java.lang.String dbUser;

    /**
     * 数据库密码
     */
    @HiddenField
    private java.lang.String dbPassword;
    /**
     * 数据库名
     */
    @Excel(name = "数据库名", width = 15)
    @ApiModelProperty(value = "数据库名")
    private java.lang.String dbName;
    /**
     * GM地址
     */
    @Excel(name = "GM地址", width = 15)
    @ApiModelProperty(value = "GM地址")
    private java.lang.String gmUrl;

    /**
     * 是否上传ta统计 0-关 1-开
     */
    @Excel(name = "是否上传ta统计", width = 15)
    @ApiModelProperty(value = "是否上传ta统计")
    private Integer taStatistics;

    /**
     * GM开关 0-关 1-开
     */
    @Excel(name = "GM开关", width = 15)
    @ApiModelProperty(value = "GM开关")
    private Integer gmStatus;

    /**
     * GM可用的IP，半角逗号分割
     */
    @Excel(name = "GM可用的IP", width = 15)
    @ApiModelProperty(value = "GM可用的IP")
    private String gmIp;

    /**
     * GM可用的玩家id，半角逗号分割
     */
    @Excel(name = "GM可用的玩家id", width = 15)
    @ApiModelProperty(value = "GM可用的玩家id")
    private String gmPlayerId;

    /**
     * 处理支付回调开关 0-关 1-开
     */
    @Excel(name = "处理支付回调开关", width = 15)
    @ApiModelProperty(value = "处理支付回调开关")
    private Integer payCallbackStatus;

    /**
     * 是否统计在线人数
     */
    @Excel(name = "是否统计在线人数", width = 15)
    @ApiModelProperty(value = "是否统计在线人数")
    private java.lang.Integer onlineStat;

    /**
     * 在线人数
     */
    @TableField(exist = false)
    @Excel(name = "在线人数", width = 15)
    @ApiModelProperty(value = "在线人数")
    private java.lang.Integer onlineNum;

    /**
     * 服务器类型 0-混服 1-专服
     */
    @Excel(name = "服务器类型 0-混服 1-专服", width = 15)
    @ApiModelProperty(value = "服务器类型 0-混服 1-专服")
    @Dict(dicCode = "server_type")
    private java.lang.Integer type;

    /**
     * 是否废弃 0-正常 1-已废弃
     */
    @Excel(name = "是否废弃", width = 15)
    @ApiModelProperty(value = "是否废弃")
    private java.lang.Integer outdated;

    /**
     * 合服时母服id
     */
    @Excel(name = "合服后的母服id", width = 15)
    @ApiModelProperty(value = "合服后的母服id")
    private java.lang.Integer pid;

    /**
     * 保留玩家id
     */
    @Excel(name = "保留玩家id", width = 15)
    @ApiModelProperty(value = "保留玩家id")
    private java.lang.Long reservePlayerId;
    /**
     * 合服时间
     */
    @Excel(name = "合服时间", width = 20, format = TimeConstant.DEFAULT_TIME_FORMAT)
    @JsonFormat(timezone = TimeConstant.DEFAULT_TIMEZONE, pattern = TimeConstant.DEFAULT_TIME_FORMAT)
    @DateTimeFormat(pattern = TimeConstant.DEFAULT_TIME_FORMAT)
    @ApiModelProperty(value = "合服时间")
    private java.util.Date mergeTime;

    /**
     * 单服活动结算时间
     */
    @Excel(name = "单服活动结算时间", width = 20, format = TimeConstant.DEFAULT_DATE_FORMAT)
    @JsonFormat(timezone = TimeConstant.DEFAULT_TIMEZONE, pattern = TimeConstant.DEFAULT_DATE_FORMAT)
    @DateTimeFormat(pattern = TimeConstant.DEFAULT_DATE_FORMAT)
    @ApiModelProperty(value = "单服活动结算时间")
    private java.util.Date singleSettleTime;

    /**
     * 停服返还充值状态
     */
    @Excel(name = "停服返还充值状态", width = 15)
    @ApiModelProperty(value = "停服返还充值状态")
    private java.lang.Integer stopServerRefund;

    /**
     * 扩展字段
     */
    @Excel(name = "扩展字段", width = 15)
    @ApiModelProperty(value = "扩展字段")
    private java.lang.String extra;

    /**
     * 服务器开服时间
     */
    @Excel(name = "开服时间", width = 20, format = TimeConstant.DEFAULT_DATE_FORMAT)
    @JsonFormat(timezone = TimeConstant.DEFAULT_TIMEZONE, pattern = TimeConstant.DEFAULT_DATE_FORMAT)
    @DateTimeFormat(pattern = TimeConstant.DEFAULT_DATE_FORMAT)
    @ApiModelProperty(value = "开服时间")
    private java.util.Date openTime;

    /**
     * 服务器上线时间
     */
    @Excel(name = "上线时间", width = 20, format = TimeConstant.DEFAULT_TIME_FORMAT)
    @JsonFormat(timezone = TimeConstant.DEFAULT_TIMEZONE, pattern = TimeConstant.DEFAULT_TIME_FORMAT)
    @DateTimeFormat(pattern = TimeConstant.DEFAULT_TIME_FORMAT)
    @ApiModelProperty(value = "上线时间")
    private java.util.Date onlineTime;

    @TableField(exist = false)
    @ApiModelProperty(value = "标签")
    private String tag;

    @TableField(exist = false)
    @ApiModelProperty(value = "渠道")
    private String channel;

    // 本地接口
    public boolean skipCheck() {
        return getOutdated() != OutdatedType.NORMAL.getValue()
                || StrUtil.contains(getGmUrl(), "localhost")
                || StrUtil.contains(getGmUrl(), "127.0.0.1");
    }

    public static boolean skipCallGm(String gmUrl) {
        return StrUtil.isEmpty(gmUrl)
                || StrUtil.contains(gmUrl, "127.0.0.1")
                || StrUtil.contains(gmUrl, "localhost");
    }

    public static boolean skipCallGm(GameServer gameServer) {
        if (gameServer == null || gameServer.getOutdated() != OutdatedType.NORMAL.getValue()) {
            return true;
        }
        return skipCallGm(gameServer.getGmUrl());
    }
}

package cn.youai.xiuzhen.game.entity;

import cn.hutool.core.util.StrUtil;
import cn.youai.basics.utils.StringUtils;
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
import org.jeecg.common.system.base.entity.BaseEntity;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 游戏渠道配置
 * @date 2019-12-11
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@TableName("game_channel")
@ApiModel(value = "GameChannel", description = "游戏渠道配置")
public class GameChannel extends BaseEntity {

    /**
     * 自增主键
     */
    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "自增主键")
    private java.lang.Integer id;

    /**
     * 渠道名称
     */
    @Excel(name = "渠道名称", width = 15)
    @ApiModelProperty(value = "渠道名称")
    private java.lang.String name;

    /**
     * 唯一标识
     */
    @Excel(name = "唯一标识", width = 15)
    @ApiModelProperty(value = "唯一标识")
    private java.lang.String simpleName;

    /**
     * 公告id
     */
    @Excel(name = "公告id", width = 15)
    @ApiModelProperty(value = "公告id")
    private java.lang.Long noticeId;

    /**
     * 版本号
     */
    @Excel(name = "版本号", width = 15)
    @ApiModelProperty(value = "版本号")
    private java.lang.Integer versionCode;

    /**
     * 版本名
     */
    @Excel(name = "版本名", width = 15)
    @ApiModelProperty(value = "版本名")
    private java.lang.String versionName;

    /**
     * 版本更新时间
     */
    @Excel(name = "版本更新时间", width = 20, format = TimeConstant.DEFAULT_TIME_FORMAT)
    @JsonFormat(timezone = TimeConstant.DEFAULT_TIMEZONE, pattern = TimeConstant.DEFAULT_TIME_FORMAT)
    @DateTimeFormat(pattern = TimeConstant.DEFAULT_TIME_FORMAT)
    @ApiModelProperty(value = "版本更新时间")
    private java.util.Date versionUpdateTime;

    /**
     * 是否允许测试登录 0-关 1-开
     */
    @Excel(name = "是否允许测试登录", width = 15)
    @ApiModelProperty(value = "是否允许测试登录")
    private Integer testLogin;

    /**
     * 是否上传ta统计 0-关 1-开
     */
    @Excel(name = "是否上传ta统计", width = 15)
    @ApiModelProperty(value = "是否上传ta统计")
    private Integer taStatistics;

    /**
     * ip白名单
     */
    @Excel(name = "ip白名单", width = 15)
    @ApiModelProperty(value = "ip白名单")
    private java.lang.String ipWhitelist;

    /**
     * 大渠道描述
     */
    @Excel(name = "大渠道描述", width = 15)
    @ApiModelProperty(value = "大渠道描述")
    private java.lang.String remark;

    /**
     * 扩展字段
     */
    @Excel(name = "扩展字段", width = 15)
    @ApiModelProperty(value = "扩展字段")
    private java.lang.String extra;

    /**
     * 游戏编号
     */
    @Excel(name = "游戏编号", width = 15)
    @ApiModelProperty(value = "游戏编号")
    private java.lang.Integer gameId;

    /**
     * 分组
     */
    @Excel(name = "分组", width = 15)
    @ApiModelProperty(value = "分组")
    private java.lang.String groupName;

    @TableField(exist = false)
    private List<GameServer> serverList;

    @TableField(exist = false)
    private List<GameSdkChannel> sdkChannelList;

    public GameChannel sortIpWhitelist() {
        if (StringUtils.isNotEmpty(getIpWhitelist())) {
            List<String> ipList = new ArrayList<>(StringUtils.split2Set(getIpWhitelist()));
            Collections.sort(ipList);
            setIpWhitelist(StrUtil.join(",", ipList));
        }
        return this;
    }
}

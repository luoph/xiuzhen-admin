package org.jeecg.modules.game.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecg.common.system.base.entity.BaseEntity;
import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 游戏渠道服配置
 * @date 2019-12-11
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@TableName("game_channel_server")
@ApiModel(value = "GameChannelServer", description = "游戏渠道服配置")
public class GameChannelServer extends BaseEntity {

    /**
     * 自增主键
     */
    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "自增主键")
    private java.lang.Integer id;
    /**
     * 服务器id
     */
    @Excel(name = "服务器id", width = 15)
    @ApiModelProperty(value = "服务器id")
    private java.lang.Integer serverId;
    /**
     * 渠道id
     */
    @Excel(name = "渠道id", width = 15)
    @ApiModelProperty(value = "渠道id")
    private java.lang.String channelId;
    /**
     * 删除状态
     */
    @Excel(name = "删除状态", width = 15)
    @Dict(dicCode = "del_flag")
    @ApiModelProperty(value = "删除状态")
    private java.lang.Integer delFlag;


    @Excel(name = "数据统计状态", width = 15)
    @Dict(dicCode = "is_counted_data")
    @ApiModelProperty(value = "数据统计状态")
    private java.lang.Integer isCountedData;
}

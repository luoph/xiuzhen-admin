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
    private java.lang.Long id;
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
     * 排序字段
     */
    @Excel(name = "排序字段", width = 15)
    @ApiModelProperty(value = "排序字段")
    private java.lang.Integer position;
    /**
     * 公告id
     */
    @Excel(name = "公告id", width = 15)
    @ApiModelProperty(value = "公告id")
    private java.lang.Integer noticeId;
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
}

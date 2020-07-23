package org.jeecg.modules.player.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.jeecg.common.constant.TimeConstant;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description player_item_log
 * @date 2020-07-21
 */
@Data
@TableName("player_item_log")
@Accessors(chain = true)
public class PlayerItemLog implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 自增id
     */
    @TableId(type = IdType.AUTO)
    private java.lang.Long id;

    /**
     * 服务器id
     */
    @Excel(name = "服务器id", width = 15)
    private java.lang.Long serverId;

    /**
     * 玩家id
     */
    @Excel(name = "玩家id", width = 15)
    private java.lang.Long playerId;

    /**
     * 道具id
     */
    @Excel(name = "道具id", width = 15)
    private java.lang.Integer itemId;

    /**
     * 数量
     */
    @Excel(name = "数量", width = 15)
    private java.lang.Long num;

    /**
     * 途径
     */
    @Excel(name = "途径", width = 15)
    private java.lang.Integer way;

    /**
     * 方式：1-获取 2-使用
     */
    @Excel(name = "方式：1-获取 2-使用", width = 15)
    private java.lang.Integer type;

    /**
     * 同步时间
     */
    @Excel(name = "同步时间", width = 15, format = TimeConstant.DEFAULT_DATE_FORMAT)
    @JsonFormat(timezone = TimeConstant.DEFAULT_TIMEZONE, pattern = TimeConstant.DEFAULT_DATE_FORMAT)
    @DateTimeFormat(pattern = TimeConstant.DEFAULT_DATE_FORMAT)
    private java.util.Date syncTime;

    /**
     * 创建时间
     */
    @Excel(name = "创建时间", width = 15, format = TimeConstant.DEFAULT_DATE_FORMAT)
    @JsonFormat(timezone = TimeConstant.DEFAULT_TIMEZONE, pattern = TimeConstant.DEFAULT_DATE_FORMAT)
    @DateTimeFormat(pattern = TimeConstant.DEFAULT_DATE_FORMAT)
    private java.util.Date createTime;

    /**
     * 更新时间
     */
    @Excel(name = "更新时间", width = 15, format = TimeConstant.DEFAULT_DATE_FORMAT)
    @JsonFormat(timezone = TimeConstant.DEFAULT_TIMEZONE, pattern = TimeConstant.DEFAULT_DATE_FORMAT)
    @DateTimeFormat(pattern = TimeConstant.DEFAULT_DATE_FORMAT)
    private java.util.Date updateTime;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof PlayerItemLog)) {
            return false;
        }

        PlayerItemLog that = (PlayerItemLog) o;

        return new EqualsBuilder()
                .append(getServerId(), that.getServerId())
                .append(getPlayerId(), that.getPlayerId())
                .append(getItemId(), that.getItemId())
                .append(getWay(), that.getWay())
                .append(getType(), that.getType())
                .append(getSyncTime(), that.getSyncTime())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(getServerId())
                .append(getPlayerId())
                .append(getItemId())
                .append(getWay())
                .append(getType())
                .append(getSyncTime())
                .toHashCode();
    }
}

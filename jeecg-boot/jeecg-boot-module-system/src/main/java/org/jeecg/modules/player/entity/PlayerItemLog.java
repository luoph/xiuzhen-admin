package org.jeecg.modules.player.entity;

import cn.youai.xiuzhen.utils.BigDecimalUtil;
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
import java.math.BigDecimal;

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
     * 同步时间
     */
    @Excel(name = "更新前数量", width = 15)
    private java.lang.Long beforeNum;

    /**
     * 同步时间
     */
    @Excel(name = "更新后数量", width = 15)
    private java.lang.Long afterNum;

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

    /**
     * 道具名称
     */
    private String itemName;

    /**
     * 道具的数量汇总
     */
    private BigDecimal itemNum;

    /**
     * 新增道具的数量汇总
     */
    private BigDecimal addItemNum;

    /**
     * 消耗道具的数量汇总
     */
    private BigDecimal consumeItemNum;

    /**
     * 消耗比 (新增/消耗)
     */
    private BigDecimal consumeRate;

    /**
     * 滞留 (新增-消耗)
     */
    private BigDecimal retention;

    /**
     * 时间字符串
     */
    private String dateStr;

    /**
     * 途径类型对应的名称 (产销点)
     */
    private String wayName;

    /**
     * 玩家数量
     */
    private BigDecimal playerNum;

    /**
     * 对应途径下货币产销次数
     */
    private BigDecimal itemCount;

    /**
     * 对应途径下货币产销数量占比
     */
    private BigDecimal itemNumRate;

    /**
     * 全途径下的道具数量总和
     */
    private BigDecimal itemNumSum;

    /**
     * 产销类型名
     */
    private String typeName;

    /**
     * 玩家名字
     */
    private String playerName;

    /**
     * 关联玩家注册信息
     */
    private PlayerRegisterInfo playerRegisterInfo;


//    @Override
//    public boolean equals(Object o) {
//        if (this == o) {
//            return true;
//        }
//
//        if (!(o instanceof PlayerItemLog)) {
//            return false;
//        }
//
//        PlayerItemLog that = (PlayerItemLog) o;
//
//        return new EqualsBuilder()
//                .append(getServerId(), that.getServerId())
//                .append(getPlayerId(), that.getPlayerId())
//                .append(getItemId(), that.getItemId())
//                .append(getWay(), that.getWay())
//                .append(getType(), that.getType())
//                .append(getSyncTime(), that.getSyncTime())
//                .isEquals();
//    }
//
//    @Override
//    public int hashCode() {
//        return new HashCodeBuilder(17, 37)
//                .append(getServerId())
//                .append(getPlayerId())
//                .append(getItemId())
//                .append(getWay())
//                .append(getType())
//                .append(getSyncTime())
//                .toHashCode();
//    }
}

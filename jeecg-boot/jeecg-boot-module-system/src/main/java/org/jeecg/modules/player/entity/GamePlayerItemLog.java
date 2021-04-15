package org.jeecg.modules.player.entity;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
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
@Accessors(chain = true)
@TableName("backpack_log")
public class GamePlayerItemLog implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 自增id
	 */

	@ExcelIgnore
	@TableId(type = IdType.AUTO)
	private java.lang.Long id;

	/**
	 * 服务器id
	 */
	@ExcelIgnore
	@TableField(exist = false)
	private java.lang.Integer serverId;

	/**
	 * 玩家id
	 */
	@ColumnWidth(15)
	@ExcelProperty("玩家id")
	@Excel(name = "玩家id", width = 15)
	private java.lang.Long playerId;

	/**
	 * 道具id
	 */
	@ColumnWidth(15)
	@ExcelProperty("道具id")
	@Excel(name = "道具id", width = 15)
	private java.lang.Integer itemId;

	/**
	 * 道具名称
	 */
	@ColumnWidth(15)
	@ExcelProperty("道具名称")
	@TableField(exist = false)
	@Excel(name = "道具名称", width = 15)
	private java.lang.String itemIdName;

	/**
	 * 数量
	 */
	@ColumnWidth(15)
	@ExcelProperty("数量")
	@Excel(name = "数量", width = 15)
	private java.lang.Long num;

	/**
	 * 途径
	 */
	@ColumnWidth(15)
	@ExcelProperty("途径")
	@Excel(name = "途径", width = 15)
	private java.lang.Integer way;

	/**
	 * 途径名称
	 */
	@ColumnWidth(15)
	@ExcelProperty("途径名称")
	@TableField(exist = false)
	@Excel(name = "途径名称", width = 15)
	private java.lang.String wayName;

	/**
	 * 方式：1-获取 2-使用
	 */
	@ExcelIgnore
	@Excel(name = "产销类型：1-获取 2-使用", width = 20)
	private java.lang.Integer type;

	/**
	 * 同步时间
	 */
	@ExcelIgnore
	@TableField(exist = false)
	@JsonFormat(timezone = TimeConstant.DEFAULT_TIMEZONE, pattern = TimeConstant.DEFAULT_DATE_FORMAT)
	@DateTimeFormat(pattern = TimeConstant.DEFAULT_DATE_FORMAT)
	private java.util.Date syncTime;

	/**
	 * 同步时间
	 */
	@ColumnWidth(15)
	@ExcelProperty("更新前数量")
	@Excel(name = "更新前数量", width = 15)
	private java.lang.Long beforeNum;

	/**
	 * 同步时间
	 */
	@ColumnWidth(15)
	@ExcelProperty("更新后数量")
	@Excel(name = "更新后数量", width = 15)
	private java.lang.Long afterNum;

	/**
	 * 创建时间
	 */
	@ColumnWidth(15)
	@ExcelProperty("统计日期")
	@Excel(name = "创建时间", width = 15, format = TimeConstant.DEFAULT_TIME_FORMAT)
	@JsonFormat(timezone = TimeConstant.DEFAULT_TIMEZONE, pattern = TimeConstant.DEFAULT_TIME_FORMAT)
	@DateTimeFormat(pattern = TimeConstant.DEFAULT_TIME_FORMAT)
	private java.util.Date createTime;

	/**
	 * 更新时间
	 */
	@ExcelIgnore
	@TableField(exist = false)
	@JsonFormat(timezone = TimeConstant.DEFAULT_TIMEZONE, pattern = TimeConstant.DEFAULT_DATE_FORMAT)
	@DateTimeFormat(pattern = TimeConstant.DEFAULT_DATE_FORMAT)
	private java.util.Date updateTime;


	/**
	 * 同步时间
	 */

	@ColumnWidth(15)
	@ExcelIgnore
	@Excel(name = "统计日期", width = 15, format = TimeConstant.DEFAULT_DATE_FORMAT)
	@JsonFormat(timezone = TimeConstant.DEFAULT_TIMEZONE, pattern = TimeConstant.DEFAULT_DATE_FORMAT)
	@DateTimeFormat(pattern = TimeConstant.DEFAULT_DATE_FORMAT)
	private java.util.Date createDate;

	/**
	 * 道具名称
	 */
	@ExcelIgnore
	@TableField(exist = false)
	private String itemName;

	/**
	 * 道具的数量汇总
	 */
	@ExcelIgnore
	@TableField(exist = false)
	private BigDecimal itemNum;

	/**
	 * 新增道具的数量汇总
	 */
	@ExcelIgnore
	@TableField(exist = false)
	private BigDecimal addItemNum;

	/**
	 * 消耗道具的数量汇总
	 */
	@ExcelIgnore
	@TableField(exist = false)
	private BigDecimal consumeItemNum;

	/**
	 * 消耗比 (新增/消耗)
	 */
	@ExcelIgnore
	@TableField(exist = false)
	private BigDecimal consumeRate;

	/**
	 * 滞留 (新增-消耗)
	 */
	@ExcelIgnore
	@TableField(exist = false)
	private BigDecimal retention;

	/**
	 * 时间字符串
	 */
	@ExcelIgnore
	@TableField(exist = false)
	private String dateStr;

	/**
	 * 途径类型对应的名称 (产销点)
	 */
//    @ExcelIgnore
//    @TableField(exist = false)
//    private String wayName;

	/**
	 * 玩家数量
	 */
	@ExcelIgnore
	@TableField(exist = false)
	private BigDecimal playerNum;

	/**
	 * 对应途径下货币产销次数
	 */
	@ExcelIgnore
	@TableField(exist = false)
	private BigDecimal itemCount;

	/**
	 * 对应途径下货币产销数量占比
	 */
	@ExcelIgnore
	@TableField(exist = false)
	private BigDecimal itemNumRate;

	/**
	 * 全途径下的道具数量总和
	 */
	@ExcelIgnore
	@TableField(exist = false)
	private BigDecimal itemNumSum;

	/**
	 * 产销类型名
	 */
	@ExcelIgnore
	@TableField(exist = false)
	private String typeName;

	/**
	 * 玩家名字
	 */
	@ExcelIgnore
	@TableField(exist = false)
	private String playerName;

	/**
	 * 关联玩家注册信息
	 */
	@ExcelIgnore
	@TableField(exist = false)
	private GameRegisterInfo registerInfo;
	@ExcelIgnore
	@TableField(exist = false)
	private String startDate;
	@ExcelIgnore
	@TableField(exist = false)
	private String endDate;


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

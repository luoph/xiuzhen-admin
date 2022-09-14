package cn.youai.xiuzhen.stat.entity;

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
 * @description player_item_log
 * @date 2021-04-08
 */
@Data
@TableName("player_item_log")
@Accessors(chain = true)
public class PlayerItemLog implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private java.lang.Integer id;

    /**
     * 区服id
     */
    @Excel(name = "区服id", width = 15)
    private java.lang.Integer serverId;

    /**
     * 玩家id
     */
    @Excel(name = "玩家id", width = 15)
    private java.lang.Integer playerId;

    /**
     * 道具id
     */
    @Excel(name = "道具id", width = 15)
    private java.lang.Integer itemId;

    /**
     * 道具名
     */
    @Excel(name = "道具名", width = 15)
    private java.lang.String itemName;

    /**
     * 数量
     */
    @Excel(name = "数量", width = 15)
    private java.lang.Integer num;

    /**
     * 途径
     */
    @Excel(name = "途径", width = 15)
    private java.lang.Integer way;

    /**
     * 途径名称
     */
    @Excel(name = "途径名称", width = 15)
    private java.lang.String wayName;

    /**
     * 更新前数量
     */
    @Excel(name = "更新前数量", width = 15)
    private java.lang.Integer beforeNum;

    /**
     * 更新后数量
     */
    @Excel(name = "更新后数量", width = 15)
    private java.lang.Integer afterNum;

    /**
     * 方式
     */
    @Excel(name = "方式", width = 15)
    private java.lang.Integer type;

    /**
     * 统计日期
     */
    @Excel(name = "统计日期", width = 15, format = TimeConstant.DEFAULT_DATE_FORMAT)
    @JsonFormat(timezone = TimeConstant.DEFAULT_TIMEZONE, pattern = TimeConstant.DEFAULT_DATE_FORMAT)
    @DateTimeFormat(pattern = TimeConstant.DEFAULT_DATE_FORMAT)
    private java.util.Date createTime;
}

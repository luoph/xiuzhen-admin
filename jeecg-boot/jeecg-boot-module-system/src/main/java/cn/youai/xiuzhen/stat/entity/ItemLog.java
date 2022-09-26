package cn.youai.xiuzhen.stat.entity;

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
import java.util.Date;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description ItemLog
 * @date 2020-07-21
 */
@Data
@Accessors(chain = true)
@TableName("backpack_log")
public class ItemLog implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 自增id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 服务器id
     */
    @TableField(exist = false)
    private Integer serverId;

    /**
     * 玩家id
     */
    @Excel(name = "玩家id", width = 15)
    private Long playerId;

    /**
     * 道具名称
     */
    @TableField(exist = false)
    @Excel(name = "道具名称", width = 15)
    private String itemName;

    /**
     * 道具id
     */
    @Excel(name = "道具id", width = 15)
    private Integer itemId;

    /**
     * 数量
     */
    @Excel(name = "数量", width = 15)
    private Long num;

    /**
     * 途径
     */
    @Excel(name = "途径", width = 15)
    private Integer way;

    /**
     * 途径名称
     */
    @TableField(exist = false)
    @Excel(name = "途径名称", width = 15)
    private String wayName;

    /**
     * 方式：1-获取 2-使用
     */
    @Excel(name = "产销类型：1-获取 2-使用", width = 20)
    private Integer type;

    /**
     * 同步时间
     */
    @Excel(name = "更新前数量", width = 15)
    private Long beforeNum;

    /**
     * 同步时间
     */
    @Excel(name = "更新后数量", width = 15)
    private Long afterNum;

    /**
     * 创建时间
     */
    @Excel(name = "创建时间", width = 15, format = TimeConstant.DEFAULT_TIME_FORMAT)
    @JsonFormat(timezone = TimeConstant.DEFAULT_TIMEZONE, pattern = TimeConstant.DEFAULT_TIME_FORMAT)
    @DateTimeFormat(pattern = TimeConstant.DEFAULT_TIME_FORMAT)
    private Date createTime;

}

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
 * @description log_player
 * @date 2021-01-14
 */
@Data
@TableName("log_player")
@Accessors(chain = true)
public class LogPlayer implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private java.lang.Integer id;

    /**
     * 玩家id
     */
    @Excel(name = "玩家id", width = 15)
    private java.lang.Long playerId;

    /**
     * 服务器id
     */
    @Excel(name = "服务器id", width = 15)
    private java.lang.Integer serverId;

    /**
     * 统计类型
     */
    @Excel(name = "统计类型", width = 15)
    private java.lang.Integer type;

    /**
     * 数值
     */
    @Excel(name = "数值", width = 15)
    private java.lang.Long value;

    /**
     * 补充参数1
     */
    @Excel(name = "补充参数1", width = 15)
    private java.lang.Integer param1;

    /**
     * 补充参数2
     */
    @Excel(name = "补充参数2", width = 15)
    private java.lang.Integer param2;

    /**
     * 补充参数3
     */
    @Excel(name = "补充参数3", width = 15)
    private java.lang.Integer param3;

    /**
     * 创建时间
     */
    @Excel(name = "创建时间", width = 15, format = TimeConstant.DEFAULT_TIME_FORMAT)
    @JsonFormat(timezone = TimeConstant.DEFAULT_TIMEZONE, pattern = TimeConstant.DEFAULT_TIME_FORMAT)
    @DateTimeFormat(pattern = TimeConstant.DEFAULT_TIME_FORMAT)
    private java.util.Date createTime;

    /**
     * 创建日期
     */
    @Excel(name = "创建日期", width = 15, format = TimeConstant.DEFAULT_DATE_FORMAT)
    @JsonFormat(timezone = TimeConstant.DEFAULT_TIMEZONE, pattern = TimeConstant.DEFAULT_DATE_FORMAT)
    @DateTimeFormat(pattern = TimeConstant.DEFAULT_DATE_FORMAT)
    private java.util.Date createDate;
}

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
@TableName("log_account")
@Accessors(chain = true)
public class LogAccount implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 账号
     */
    @Excel(name = "账号", width = 15)
    private String account;

    /**
     * 玩家id
     */
    @Excel(name = "玩家id", width = 15)
    private Long playerId;

    /**
     * 服务器id
     */
    @Excel(name = "服务器id", width = 15)
    private Integer serverId;

    /**
     * 渠道
     */
    @Excel(name = "渠道", width = 15)
    private String channel;

    /**
     * 1-创角、2-登录、3-退出
     */
    @Excel(name = "操作类型", width = 15)
    private Integer type;

    /**
     * 数值记录
     */
    @Excel(name = "数值记录", width = 15)
    private Long value;

    /**
     * IP地址
     */
    @Excel(name = "IP地址", width = 15)
    private String ip;

    /**
     * 创建时间
     */
    @Excel(name = "创建时间", width = 15, format = TimeConstant.DEFAULT_DATE_FORMAT)
    @JsonFormat(timezone = TimeConstant.DEFAULT_TIMEZONE, pattern = TimeConstant.DEFAULT_DATE_FORMAT)
    @DateTimeFormat(pattern = TimeConstant.DEFAULT_DATE_FORMAT)
    private java.util.Date createTime;

    /**
     * 创建日期
     */
    @Excel(name = "创建日期", width = 15, format = TimeConstant.DEFAULT_DATE_FORMAT)
    @JsonFormat(timezone = TimeConstant.DEFAULT_TIMEZONE, pattern = TimeConstant.DEFAULT_DATE_FORMAT)
    @DateTimeFormat(pattern = TimeConstant.DEFAULT_DATE_FORMAT)
    private java.util.Date createDate;
}

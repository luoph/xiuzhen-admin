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
import java.math.BigDecimal;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 在线情况
 * @date 2020-10-15
 */
@Data
@TableName("game_online_num")
@Accessors(chain = true)
public class GameOnlineNum implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 渠道
     */
    @Excel(name = "渠道", width = 15)
    private String channel;

    /**
     * 服务器id
     */
    @Excel(name = "服务器id", width = 15)
    private Integer serverId;

    /**
     * 在线人数
     */
    @Excel(name = "在线人数", width = 15)
    private Long onlineNum;

    /**
     * 获取在线人数的时间点
     */
    @Excel(name = "获取在线人数的时间点", width = 15, format = TimeConstant.DEFAULT_DATE_FORMAT)
    @JsonFormat(timezone = TimeConstant.DEFAULT_TIMEZONE, pattern = TimeConstant.DEFAULT_DATE_FORMAT)
    @DateTimeFormat(pattern = TimeConstant.DEFAULT_DATE_FORMAT)
    private java.util.Date getTime;

    /**
     * 更新时间
     */
    @Excel(name = "更新时间", width = 15, format = TimeConstant.DEFAULT_TIME_FORMAT)
    @JsonFormat(timezone = TimeConstant.DEFAULT_TIMEZONE, pattern = TimeConstant.DEFAULT_TIME_FORMAT)
    @DateTimeFormat(pattern = TimeConstant.DEFAULT_TIME_FORMAT)
    private java.util.Date updateTime;

    /**
     * 创建时间
     */
    @Excel(name = "创建时间", width = 15, format = TimeConstant.DEFAULT_TIME_FORMAT)
    @JsonFormat(timezone = TimeConstant.DEFAULT_TIMEZONE, pattern = TimeConstant.DEFAULT_TIME_FORMAT)
    @DateTimeFormat(pattern = TimeConstant.DEFAULT_TIME_FORMAT)
    private java.util.Date createTime;

    /**
     * dau
     */
    private BigDecimal dau;

    /**
     * pcu
     */
    private BigDecimal pcu;

    /**
     * acu
     */
    private BigDecimal acu;

}

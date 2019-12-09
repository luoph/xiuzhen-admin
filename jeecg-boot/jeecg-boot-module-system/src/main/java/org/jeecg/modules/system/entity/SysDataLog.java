package org.jeecg.modules.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecg.common.constant.TimeConstant;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SysDataLog implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.ID_WORKER_STR)
    /**
     * id'
     */
    private String id;
    /**
     * 创建人登录名称
     */
    private String createBy;
    @JsonFormat(timezone = TimeConstant.DEFAULT_TIMEZONE, pattern = TimeConstant.DEFAULT_TIME_FORMAT)
    @DateTimeFormat(pattern = TimeConstant.DEFAULT_TIME_FORMAT)
    /**
     * 创建日期
     */
    private Date createTime;
    /**
     * 更新人登录名称
     */
    private String updateBy;
    @JsonFormat(timezone = TimeConstant.DEFAULT_TIMEZONE, pattern = TimeConstant.DEFAULT_TIME_FORMAT)
    @DateTimeFormat(pattern = TimeConstant.DEFAULT_TIME_FORMAT)
    /**
     * 更新日期
     */
    private Date updateTime;
    /**
     * 表名
     */
    private String dataTable;
    /**
     * 数据ID
     */
    private String dataId;
    /**
     * 数据内容
     */
    private String dataContent;
    /**
     * 版本号
     */
    private String dataVersion;
}

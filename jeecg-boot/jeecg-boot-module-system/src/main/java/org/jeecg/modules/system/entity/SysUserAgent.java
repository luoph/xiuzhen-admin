package org.jeecg.modules.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.jeecg.common.constant.TimeConstant;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 用户代理人设置
 * @date 2019-04-17
 */
@Data
@TableName("sys_user_agent")
public class SysUserAgent implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 序号
     */
    @TableId(type = IdType.ID_WORKER_STR)
    private java.lang.String id;
    /**
     * 用户名
     */
    @Excel(name = "用户名", width = 15)
    private java.lang.String userName;
    /**
     * 代理人用户名
     */
    @Excel(name = "代理人用户名", width = 15)
    private java.lang.String agentUserName;
    /**
     * 代理开始时间
     */
    @Excel(name = "代理开始时间", width = 20, format = TimeConstant.DEFAULT_TIME_FORMAT)
    @JsonFormat(timezone = TimeConstant.DEFAULT_TIMEZONE, pattern = TimeConstant.DEFAULT_TIME_FORMAT)
    @DateTimeFormat(pattern = TimeConstant.DEFAULT_TIME_FORMAT)
    private java.util.Date startTime;
    /**
     * 代理结束时间
     */
    @Excel(name = "代理结束时间", width = 20, format = TimeConstant.DEFAULT_TIME_FORMAT)
    @JsonFormat(timezone = TimeConstant.DEFAULT_TIMEZONE, pattern = TimeConstant.DEFAULT_TIME_FORMAT)
    @DateTimeFormat(pattern = TimeConstant.DEFAULT_TIME_FORMAT)
    private java.util.Date endTime;
    /**
     * 状态0无效1有效
     */
    @Excel(name = "状态0无效1有效", width = 15)
    private java.lang.String status;
    /**
     * 创建人名称
     */
    @Excel(name = "创建人名称", width = 15)
    private java.lang.String createName;
    /**
     * 创建人登录名称
     */
    @Excel(name = "创建人登录名称", width = 15)
    private java.lang.String createBy;
    /**
     * 创建日期
     */
    @Excel(name = "创建日期", width = 20, format = TimeConstant.DEFAULT_TIME_FORMAT)
    @JsonFormat(timezone = TimeConstant.DEFAULT_TIMEZONE, pattern = TimeConstant.DEFAULT_TIME_FORMAT)
    @DateTimeFormat(pattern = TimeConstant.DEFAULT_TIME_FORMAT)
    private java.util.Date createTime;
    /**
     * 更新人名称
     */
    @Excel(name = "更新人名称", width = 15)
    private java.lang.String updateName;
    /**
     * 更新人登录名称
     */
    @Excel(name = "更新人登录名称", width = 15)
    private java.lang.String updateBy;
    /**
     * 更新日期
     */
    @Excel(name = "更新日期", width = 20, format = TimeConstant.DEFAULT_TIME_FORMAT)
    @JsonFormat(timezone = TimeConstant.DEFAULT_TIMEZONE, pattern = TimeConstant.DEFAULT_TIME_FORMAT)
    @DateTimeFormat(pattern = TimeConstant.DEFAULT_TIME_FORMAT)
    private java.util.Date updateTime;
    /**
     * 所属部门
     */
    @Excel(name = "所属部门", width = 15)
    private java.lang.String sysOrgCode;
    /**
     * 所属公司
     */
    @Excel(name = "所属公司", width = 15)
    private java.lang.String sysCompanyCode;
}

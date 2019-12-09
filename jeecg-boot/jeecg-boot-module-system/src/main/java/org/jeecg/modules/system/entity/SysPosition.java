package org.jeecg.modules.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecg.common.constant.TimeConstant;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 职务表
 * @date 2019-09-19
 */
@Data
@TableName("sys_position")
@Accessors(chain = true)
@ApiModel(value = "sys_position对象", description = "职务表")
public class SysPosition {

    /**
     * id
     */
    @TableId(type = IdType.ID_WORKER_STR)
    @ApiModelProperty(value = "id")
    private java.lang.String id;
    /**
     * 职务编码
     */
    @Excel(name = "职务编码", width = 15)
    @ApiModelProperty(value = "职务编码")
    private java.lang.String code;
    /**
     * 职务名称
     */
    @Excel(name = "职务名称", width = 15)
    @ApiModelProperty(value = "职务名称")
    private java.lang.String name;
    /**
     * 职级
     */
    @Excel(name = "职级", width = 15, dicCode = "position_rank")
    @ApiModelProperty(value = "职级")
    @Dict(dicCode = "position_rank")
    private java.lang.String postRank;
    /**
     * 公司id
     */
    @Excel(name = "公司id", width = 15)
    @ApiModelProperty(value = "公司id")
    private java.lang.String companyId;
    /**
     * 创建人
     */
    @Excel(name = "创建人", width = 15)
    @ApiModelProperty(value = "创建人")
    private java.lang.String createBy;
    /**
     * 创建时间
     */
    @Excel(name = "创建时间", width = 20, format = TimeConstant.DEFAULT_TIME_FORMAT)
    @JsonFormat(timezone = TimeConstant.DEFAULT_TIMEZONE, pattern = TimeConstant.DEFAULT_TIME_FORMAT)
    @DateTimeFormat(pattern = TimeConstant.DEFAULT_TIME_FORMAT)
    @ApiModelProperty(value = "创建时间")
    private java.util.Date createTime;
    /**
     * 修改人
     */
    @Excel(name = "修改人", width = 15)
    @ApiModelProperty(value = "修改人")
    private java.lang.String updateBy;
    /**
     * 修改时间
     */
    @Excel(name = "修改时间", width = 20, format = TimeConstant.DEFAULT_TIME_FORMAT)
    @JsonFormat(timezone = TimeConstant.DEFAULT_TIMEZONE, pattern = TimeConstant.DEFAULT_TIME_FORMAT)
    @DateTimeFormat(pattern = TimeConstant.DEFAULT_TIME_FORMAT)
    @ApiModelProperty(value = "修改时间")
    private java.util.Date updateTime;
    /**
     * 组织机构编码
     */
    @Excel(name = "组织机构编码", width = 15)
    @ApiModelProperty(value = "组织机构编码")
    private java.lang.String sysOrgCode;
}

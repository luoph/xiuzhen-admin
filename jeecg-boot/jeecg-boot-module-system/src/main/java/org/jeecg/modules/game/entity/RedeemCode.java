package org.jeecg.modules.game.entity;

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
 * @description 激活码
 * @date 2020-01-07
 */
@Data
@TableName("redeem_code")
@Accessors(chain = true)
public class RedeemCode implements Serializable {

    private static final long serialVersionUID = 1L;
	
	/**
	 * id
	 */
	@TableId(type = IdType.AUTO)
    private java.lang.Long id;
	
	/**
	 * 激活码活动id
	 */
	@Excel(name = "激活码活动id", width = 15)
    private java.lang.Integer activityId;
	
	/**
	 * 激活码
	 */
	@Excel(name = "激活码", width = 15)
    private java.lang.String code;
	
	/**
	 * 状态
	 */
	@Excel(name = "状态", width = 15)
    private java.lang.Integer status;
	
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
}

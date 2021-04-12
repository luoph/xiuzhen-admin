package org.jeecg.modules.game.entity;

import com.alibaba.excel.annotation.ExcelIgnore;
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

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 剧情分析
 * @date 2021-03-26
 */
@Data
@TableName("main_story_level")
@Accessors(chain = true)
public class GameStoryAnalysis implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键ID
	 */
	@TableId(type = IdType.AUTO)
	private java.lang.Long id;

	/**
	 * 玩家id
	 */
	@ExcelIgnore
	private java.lang.Long playerId;

	/**
	 * 大关卡
	 */
	@ExcelIgnore
	private java.lang.Integer primeLevel;

	/**
	 * 大关卡
	 */
	@ExcelIgnore
	private java.lang.Integer minorLevel;

	/**
	 * 剧情
	 */
	@ExcelIgnore
	private java.lang.Integer plotId;

	/**
	 * 状态: 1.未完成, 2.已完成
	 */
	@ExcelIgnore
	private java.lang.Integer status;

	/**
	 * 进行中的阶段(story.group)
	 */
	@ExcelIgnore
	private java.lang.Integer stage;

	/**
	 * 创建日期
	 */
	@ExcelIgnore
	@JsonFormat(timezone = TimeConstant.DEFAULT_TIMEZONE, pattern = TimeConstant.DEFAULT_DATE_FORMAT)
	@DateTimeFormat(pattern = TimeConstant.DEFAULT_DATE_FORMAT)
	private java.util.Date createTime;

	/**
	 * 更新日期
	 */
	@ExcelIgnore
	@JsonFormat(timezone = TimeConstant.DEFAULT_TIMEZONE, pattern = TimeConstant.DEFAULT_DATE_FORMAT)
	@DateTimeFormat(pattern = TimeConstant.DEFAULT_DATE_FORMAT)
	private java.util.Date updateTime;
}

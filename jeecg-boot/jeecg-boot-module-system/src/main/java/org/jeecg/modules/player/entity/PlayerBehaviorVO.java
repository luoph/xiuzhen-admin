package org.jeecg.modules.player.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecg.common.constant.TimeConstant;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @author lijunchi
 * @version 1.0
 * @description: 行为分析VO
 * @date 2021-4-15 15:31
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class PlayerBehaviorVO implements Serializable {

	private static final long serialVersionUID = 6026758120001880420L;

	/**
	 * 渠道id
	 */
	private Integer channelId;

	/**
	 * 服务器id
	 */
	private Integer serverId;

	/**
	 * 角色昵称
	 */
	private String nickname;

	/**
	 * 角色id
	 */
	private Long playerId;

	/**
	 * 创建日期-开始日期
	 */
	@JsonFormat(timezone = TimeConstant.DEFAULT_TIMEZONE, pattern = TimeConstant.DEFAULT_DATE_FORMAT)
	@DateTimeFormat(pattern = TimeConstant.DEFAULT_DATE_FORMAT)
	private Date rangeDateBegin;

	/**
	 * 创建日期-结束日期
	 */
	@JsonFormat(timezone = TimeConstant.DEFAULT_TIMEZONE, pattern = TimeConstant.DEFAULT_DATE_FORMAT)
	@DateTimeFormat(pattern = TimeConstant.DEFAULT_DATE_FORMAT)
	private Date rangeDateEnd;

	/**
	 * 选择就近天数
	 * 0-不选择天数
	 * 7-7天
	 * 15-15天
	 * 30-近一个月
	 * 60-近两个月
	 */
	private Integer daysType;

}

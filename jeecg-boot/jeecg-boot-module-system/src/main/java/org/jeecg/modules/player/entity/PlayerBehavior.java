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
 * @author xubodong
 * @version 1.0.0
 * @date 2020/10/23 17:52
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class PlayerBehavior implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;

	/**
	 * 服务器id
	 */
	private Integer serverId;

	/**
	 * 玩家id
	 */
	private Long playerId;

	/**
	 * 玩家相关信息分类类型
	 */
	private int type;

	/**
	 * type对应的值
	 */
	private Long value;

	/**
	 * 玩家名
	 */
	private String nickname;


	/**
	 * 修炼年数
	 */
	private Long practiceYear;

	/**
	 * 境界等级
	 */
	private Long reamLevel;

	/**
	 * 斗法次数
	 */
	private Long arenaBattle;

	/**
	 * 仙兽秘境
	 */
	private Long mapExplore;

	/**
	 * 剧情关数
	 */
	private Long mainStoryLevel;

	/**
	 * 灵界器灵
	 */
	private Long spiritualWorldoss;

	/**
	 * 魔王入侵
	 */
	private Long worldBoss;

	/**
	 * 仙盟仙宴
	 */
	private Long factionBanquet;

	/**
	 * 当天充值
	 */
	private Long recharge;

	/**
	 * 玉髓消耗
	 */
	private Long consumeMoney;

	/**
	 * 阅历值
	 */
	private Long experience;

	/**
	 * 在线时长
	 */
	private Long onlineTime;

	/**
	 * 日期
	 */
	@JsonFormat(timezone = TimeConstant.DEFAULT_TIMEZONE, pattern = TimeConstant.DEFAULT_DATE_FORMAT)
	@DateTimeFormat(pattern = TimeConstant.DEFAULT_DATE_FORMAT)
	private Date createDate;


}

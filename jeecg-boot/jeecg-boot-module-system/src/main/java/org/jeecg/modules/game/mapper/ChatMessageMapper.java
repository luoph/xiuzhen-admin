package org.jeecg.modules.game.mapper;

import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.game.entity.ChatMessage;
import org.jeecg.modules.game.entity.ChatMessageVO;

import java.util.Date;
import java.util.List;

/**
 * @author xubodong
 * @version 1.0.0
 * @date 2020/10/20 21:22
 */
public interface ChatMessageMapper {

	List<ChatMessage> queryForList(@Param("rangeTimeBeginDate") Date rangeTimeBeginDate,
	                               @Param("rangeTimeEndDate") Date rangeTimeEndDate,
	                               @Param("channelId") Integer channelId,
	                               @Param("playerId") Long playerId,
	                               @Param("nickname") String nickname,
	                               @Param("message") String message);

}

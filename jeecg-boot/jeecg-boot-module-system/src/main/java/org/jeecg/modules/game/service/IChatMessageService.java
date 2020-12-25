package org.jeecg.modules.game.service;

import org.jeecg.modules.game.entity.ChatMessageVO;

import java.util.List;

/**
 * @author xubodong
 * @version 1.0.0
 * @date 2020/10/20 21:25
 */
public interface IChatMessageService {

    /**
     * 查询公共聊天
     *
     * @param rangeTimeBegin
     * @param rangeTimeEnd
     * @param channelId
     * @param serverId
     * @param playerId
     * @param message
     * @return
     */
    List<ChatMessageVO> queryForList(String rangeTimeBegin, String rangeTimeEnd, Integer channelId, Integer serverId, Long playerId, String nickname, String message);

    /**
     * 通过发送者和接收者玩家名模糊查询聊天列表
     *
     * @param rangeTimeBegin
     * @param rangeTimeEnd
     * @param channelId
     * @param serverId
     * @param nickname
     * @param message
     * @return
     */
    List<ChatMessageVO> queryListBySenderAndReceive(String rangeTimeBegin, String rangeTimeEnd, Integer channelId, Integer serverId, String nickname, Long playerId, String message);


}

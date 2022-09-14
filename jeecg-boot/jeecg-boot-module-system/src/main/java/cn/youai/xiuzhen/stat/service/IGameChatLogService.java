package cn.youai.xiuzhen.stat.service;

import cn.youai.xiuzhen.stat.entity.ChatMessageVO;

import java.util.List;

/**
 * @author huli
 * @version 1.0.0
 */
public interface IGameChatLogService {

    /**
     * 查询世界聊天信息
     *
     * @param rangeTimeBegin
     * @param rangeTimeEnd
     * @param channelId
     * @param serverId
     * @param playerId
     * @param message
     * @return
     */
    List<ChatMessageVO> queryCommonChatLogList(String rangeTimeBegin, String rangeTimeEnd, Integer channelId, Integer serverId, Long playerId, String nickname, String message);

    /**
     * 查询仙盟聊天信息
     *
     * @param rangeTimeBegin
     * @param rangeTimeEnd
     * @param channelId
     * @param serverId
     * @param nickname
     * @param message
     * @return
     */
    List<ChatMessageVO> queryImmortalChatLogList(String rangeTimeBegin, String rangeTimeEnd, Integer channelId, Integer serverId, String nickname, Long playerId, String message);

    /**
     * 查询个人私聊信息
     *
     * @param rangeTimeBegin
     * @param rangeTimeEnd
     * @param channelId
     * @param serverId
     * @param nickname
     * @param message
     * @return
     */
    List<ChatMessageVO> querySelfChatLogList(String rangeTimeBegin, String rangeTimeEnd, Integer channelId, Integer serverId, String nickname, Long playerId, String message);

    /**
     * 查询跨服聊天信息
     *
     * @param rangeTimeBegin
     * @param rangeTimeEnd
     * @param channelId
     * @param serverId
     * @param playerId
     * @param message
     * @return
     */
    List<ChatMessageVO> queryCrossServerChatLogList(String rangeTimeBegin, String rangeTimeEnd, Integer channelId, Integer serverId, Long playerId, String nickname, String message);

}

package org.jeecg.modules.game.service.impl;

import cn.youai.xiuzhen.utils.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.database.DataSourceHelper;
import org.jeecg.modules.game.entity.ChatMessage;
import org.jeecg.modules.game.entity.ChatMessageVO;
import org.jeecg.modules.game.entity.FriendChatChannel;
import org.jeecg.modules.game.entity.FriendChatMessage;
import org.jeecg.modules.game.mapper.ChatMessageMapper;
import org.jeecg.modules.game.mapper.FriendChatChannelMapper;
import org.jeecg.modules.game.mapper.FriendChatMessageMapper;
import org.jeecg.modules.game.service.IChatMessageService;
import org.jeecg.modules.player.mapper.PlayerMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author xubodong
 * @version 1.0.0
 * @date 2020/10/20 21:24
 */
@Service
@Slf4j
public class ChatMessageServiceImpl implements IChatMessageService {

    @Resource
    private ChatMessageMapper chatMessageMapper;

    @Resource
    private FriendChatChannelMapper friendChatChannelMapper;

    @Resource
    private FriendChatMessageMapper friendChatMessageMapper;

    @Resource
    private PlayerMapper playerMapper;


    @Override
    public List<ChatMessageVO> queryForList(String rangeTimeBegin, String rangeTimeEnd, Integer channelId, Integer serverId, Long playerId, String nickname, String message) {
        List<ChatMessageVO> chatMessageVOList = new ArrayList<>();
        Date rangeTimeBeginDate = null;
        Date rangeTimeEndDate = null;
        DataSourceHelper.useServerDatabase(serverId);
        try {
            rangeTimeBeginDate = DateUtils.dateOnly(DateUtils.parseDate(rangeTimeBegin));
            rangeTimeEndDate = DateUtils.dateOnly(DateUtils.parseDate(rangeTimeEnd));
            List<ChatMessage> list = chatMessageMapper.queryForList(rangeTimeBeginDate, rangeTimeEndDate, channelId, playerId, nickname, message);
            for (ChatMessage chatMessage : list) {
                ChatMessageVO chatMessageVO = new ChatMessageVO();
                chatMessageVO.setChatChannel("公共频道");
                chatMessageVO.setReceivePlayerName(chatMessage.getPlayer().getNickname());
                chatMessageVO.setSendPlayerName("公共");
                chatMessageVO.setMessage(chatMessage.getMessage());
                chatMessageVO.setMessageTime(chatMessage.getCreateTime());
                chatMessageVOList.add(chatMessageVO);
            }
        } catch (Exception e) {
            log.error("ChatMessageServiceImpl.queryForList():查询公共聊天报错", e);
        } finally {
            DataSourceHelper.useDefaultDatabase();
        }
        return chatMessageVOList;
    }

    @Override
    public List<ChatMessageVO> queryListBySenderAndReceive(String rangeTimeBegin, String rangeTimeEnd, Integer channelId, Integer serverId, String nickname, Long playerId, String message) {
        List<ChatMessageVO> chatMessageVOList = new ArrayList<>();
        Date rangeTimeBeginDate = null;
        Date rangeTimeEndDate = null;
        try {
            rangeTimeBeginDate = DateUtils.dateOnly(DateUtils.parseDate(rangeTimeBegin));
            rangeTimeEndDate = DateUtils.dateOnly(DateUtils.parseDate(rangeTimeEnd));
            DataSourceHelper.useServerDatabase(serverId);
            // 玩家名字作为发送方模糊匹配查询
            List<FriendChatChannel> senderlist = friendChatChannelMapper.queryListBySender(rangeTimeBeginDate, rangeTimeEndDate, channelId, nickname, playerId, message);
            for (FriendChatChannel friendChatChannel : senderlist) {
                ChatMessageVO chatMessageVO = new ChatMessageVO();
                chatMessageVO.setChatChannel("私聊频道");
                Long receivePlayerId = friendChatChannel.getFriendChatMessage().getPlayerId();
                String receivePlayerName = playerMapper.getNameById(receivePlayerId);
                chatMessageVO.setReceivePlayerName(receivePlayerName);
                chatMessageVO.setSendPlayerName(friendChatChannel.getPlayer().getNickname());
                chatMessageVO.setMessage(friendChatChannel.getFriendChatMessage().getMessage());
                chatMessageVO.setMessageTime(friendChatChannel.getFriendChatMessage().getCreateTime());
                chatMessageVOList.add(chatMessageVO);
            }
            // 玩家名字作为接收方模糊匹配查询
            List<FriendChatMessage> receivelist = friendChatMessageMapper.queryListByReceive(rangeTimeBeginDate, rangeTimeEndDate, channelId, nickname, playerId, message);
            for (FriendChatMessage friendChatMessage : receivelist) {
                ChatMessageVO chatMessageVO = new ChatMessageVO();
                chatMessageVO.setChatChannel("私聊频道");
                Long senderPlayerId = friendChatMessage.getFriendChatChannel().getPlayerId();
                String senderPlayerName = playerMapper.getNameById(senderPlayerId);
                chatMessageVO.setReceivePlayerName(friendChatMessage.getPlayer().getNickname());
                chatMessageVO.setSendPlayerName(senderPlayerName);
                chatMessageVO.setMessage(friendChatMessage.getMessage());
                chatMessageVO.setMessageTime(friendChatMessage.getCreateTime());
                chatMessageVOList.add(chatMessageVO);
            }
        } catch (Exception e) {
            log.error("ChatMessageServiceImpl.queryListBySenderAndReceive():查询聊天列表报错", e);
        } finally {
            DataSourceHelper.useDefaultDatabase();
        }
        return chatMessageVOList;
    }


}

package cn.youai.xiuzhen.stat.service.impl;

import cn.youai.server.utils.DateUtils;
import cn.youai.xiuzhen.core.database.DataSourceHelper;
import cn.youai.xiuzhen.game.service.IGamePlayerService;
import cn.youai.xiuzhen.stat.entity.ChatMessage;
import cn.youai.xiuzhen.stat.entity.ChatMessageVO;
import cn.youai.xiuzhen.stat.entity.FriendChatChannel;
import cn.youai.xiuzhen.stat.entity.FriendChatMessage;
import cn.youai.xiuzhen.stat.mapper.ChatMessageMapper;
import cn.youai.xiuzhen.stat.mapper.FriendChatChannelMapper;
import cn.youai.xiuzhen.stat.mapper.FriendChatMessageMapper;
import cn.youai.xiuzhen.stat.service.IChatMessageService;
import com.baomidou.dynamic.datasource.annotation.DS;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
@Slf4j
@Service
@DS("master")
public class ChatMessageServiceImpl implements IChatMessageService {

    @Resource
    private ChatMessageMapper chatMessageMapper;

    @Resource
    private FriendChatChannelMapper friendChatChannelMapper;

    @Resource
    private FriendChatMessageMapper friendChatMessageMapper;

    @Autowired
    private IGamePlayerService playerService;


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
                String receivePlayerName = playerService.queryPlayer(receivePlayerId).getNickname();
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
                String senderPlayerName = playerService.queryPlayer(senderPlayerId).getNickname();
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

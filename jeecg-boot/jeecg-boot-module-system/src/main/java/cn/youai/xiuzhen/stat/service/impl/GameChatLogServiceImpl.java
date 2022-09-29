package cn.youai.xiuzhen.stat.service.impl;

import cn.youai.server.utils.DateUtils;
import cn.youai.xiuzhen.core.database.DataSourceHelper;
import cn.youai.xiuzhen.stat.entity.ChatMessageVO;
import cn.youai.xiuzhen.stat.mapper.GameChatLogMapper;
import cn.youai.xiuzhen.stat.service.IGameChatLogService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author huli
 * @version 1.0.0
 */
@Slf4j
@Service
public class GameChatLogServiceImpl implements IGameChatLogService {
    @Resource
    private GameChatLogMapper gameChatLogMapper;

    /**
     * 查询世界聊天信息
     */
    @Override
    public List<ChatMessageVO> queryCommonChatLogList(String rangeTimeBegin, String rangeTimeEnd, Integer channelId, Integer serverId, Long playerId, String nickname, String message) {
        List<ChatMessageVO> chatMessageVOList = new ArrayList<>();
        try {
            // 通过serverId切换数据源
            DataSourceHelper.useServerDatabase(serverId);
            //查询所有用户信息
            List<Map> allPlayerInfoList = gameChatLogMapper.selectPlayerInfoByPlayerId();
            //用户信息以play_id分组
            Map<String, List<Map>> allPlayerInfoMapPlayId = allPlayerInfoList.stream().collect(Collectors.groupingBy(map -> map.get("id").toString()));
            //用户信息以nickname分组
            Map<String, List<Map>> allPlayerInfoMapNikename = allPlayerInfoList.stream().collect(Collectors.groupingBy(map -> map.get("nickname").toString()));
            //处理查询条件没有player_id,但是有nickname的情况
            if (null == playerId && !StringUtils.isEmpty(nickname)) {
                playerId = Long.parseLong(allPlayerInfoMapNikename.get(nickname).get(0).get("id").toString());
            }
            //查询符合条件的所有世界聊天信息
            List<Map> worldMassageList = gameChatLogMapper.selectWorldMassageByAllKindOf(playerId, message, rangeTimeBegin, rangeTimeEnd);
            //遍历信息，并装填信息给前端返回
            long i = 1;
            for (Map map : worldMassageList) {
                ChatMessageVO chatMessageVO = new ChatMessageVO();
                chatMessageVO.setId(i++);
                chatMessageVO.setSendPlayerId((Long) map.get("player_id"));
                chatMessageVO.setServerId(serverId);
                chatMessageVO.setChatChannel("世界");
                chatMessageVO.setSendPlayerName(allPlayerInfoMapPlayId.get(map.get("player_id").toString()).get(0).get("nickname").toString());
                chatMessageVO.setReceivePlayerName("公共");
                chatMessageVO.setMessage(map.get("message").toString());
                chatMessageVO.setMessageTime(DateUtils.parseDate(map.get("create_time").toString()));
                chatMessageVOList.add(chatMessageVO);
            }
        } catch (Exception e) {
            log.error("通过服务器id:" + serverId + ",切换数据源异常", e);
        } finally {
            DataSourceHelper.useDefaultDatabase();
        }
        return chatMessageVOList;
    }

    /**
     * 查询仙盟聊天信息
     */
    @Override
    public List<ChatMessageVO> queryImmortalChatLogList(String rangeTimeBegin, String rangeTimeEnd, Integer channelId, Integer serverId, String nickname, Long playerId, String message) {
        List<ChatMessageVO> chatMessageVOList = new ArrayList<>();
        List<Map> worldMassageList;
        try {
            // 通过serverId切换数据源
            DataSourceHelper.useServerDatabase(serverId);

            //查询所有用户信息
            List<Map> allPlayerInfoList = gameChatLogMapper.selectPlayerInfoByPlayerId();
            //用户信息以play_id分组
            Map<String, List<Map>> allPlayerInfoMapPlayId = allPlayerInfoList.stream().collect(Collectors.groupingBy(map -> map.get("id").toString()));
            //用户信息以nickname分组
            Map<String, List<Map>> allPlayerInfoMapNikename = allPlayerInfoList.stream().collect(Collectors.groupingBy(map -> map.get("nickname").toString()));

            //查询所有仙盟会话配置
            List<Map> allChatChannelList = gameChatLogMapper.selectAllImmortalChatChannel();
            //会话配置以channel_id分组
            Map<String, List<Map>> allChatChannelMapChannelId = allChatChannelList.stream().collect(Collectors.groupingBy(map -> map.get("channel_id").toString()));

            //处理查询条件没有player_id,但是有nickname的情况
            if (null == playerId && !StringUtils.isEmpty(nickname)) {
                playerId = Long.parseLong(allPlayerInfoMapNikename.get(nickname).get(0).get("id").toString());
            }
            //查询符合条件的所有仙盟聊天信息
            worldMassageList = gameChatLogMapper.selectImmortalMessageByAllKindOf(playerId, message, rangeTimeBegin, rangeTimeEnd);
            long i = 1;
            //遍历信息，并装填信息给前端返回
            for (Map map : worldMassageList) {
                //排除系统消息
                if ("0".equals(map.get("player_id").toString())) {
                    continue;
                }
                //排除非仙盟群聊信息(确保allChatChannelMap_ChannelId有值才进行&&后面的判断)
                if (allChatChannelMapChannelId.size() > 0 && null == allChatChannelMapChannelId.get(map.get("channel_id").toString())) {
                    continue;
                }
                //装填信息
                ChatMessageVO chatMessageVO = new ChatMessageVO();
                chatMessageVO.setId(i++);
                chatMessageVO.setServerId(serverId);
                chatMessageVO.setSendPlayerId((Long) map.get("player_id"));
                chatMessageVO.setChatChannel("仙盟");
                chatMessageVO.setSendPlayerName(allPlayerInfoMapPlayId.get(map.get("player_id").toString()).get(0).get("nickname").toString());
                chatMessageVO.setReceivePlayerName("公共");
                chatMessageVO.setMessage(map.get("message").toString());
                chatMessageVO.setMessageTime(DateUtils.parseDate(map.get("create_time").toString()));
                chatMessageVOList.add(chatMessageVO);
            }
        } catch (Exception e) {
            log.error("通过服务器id:" + serverId + ",切换数据源异常", e);
        } finally {
            DataSourceHelper.useDefaultDatabase();
        }
        return chatMessageVOList;
    }

    /**
     * 查询个人私聊信息
     */
    @Override
    public List<ChatMessageVO> querySelfChatLogList(String rangeTimeBegin, String rangeTimeEnd, Integer channelId, Integer serverId, String nickname, Long playerId, String message) {
        List<ChatMessageVO> chatMessageVOList = new ArrayList<>();
        List<Map> worldMassageList;
        try {
            // 通过serverId切换数据源
            DataSourceHelper.useServerDatabase(serverId);

            //查询所有用户信息
            List<Map> allPlayerInfoList = gameChatLogMapper.selectPlayerInfoByPlayerId();
            //用户信息以play_id分组
            Map<String, List<Map>> allPlayerInfoMapPlayId = allPlayerInfoList.stream().collect(Collectors.groupingBy(map -> map.get("id").toString()));
            //用户信息以nickname分组
            Map<String, List<Map>> allPlayerInfoMapNikename = allPlayerInfoList.stream().collect(Collectors.groupingBy(map -> map.get("nickname").toString()));

            //查询所有私人会话配置
            List<Map> allChatChannelList = gameChatLogMapper.selectAllSelfChatChannel();
            //会话配置以channel_id分组
            Map<String, List<Map>> allChatChannelMapChannelId = allChatChannelList.stream().collect(Collectors.groupingBy(map -> map.get("channel_id").toString()));

            //处理查询条件没有player_id,但是有nickname的情况
            if (null == playerId && !StringUtils.isEmpty(nickname)) {
                playerId = Long.parseLong(allPlayerInfoMapNikename.get(nickname).get(0).get("id").toString());
            }
            //查询符合条件的所有私人聊天信息
            worldMassageList = gameChatLogMapper.selectSelfMessageByAllKindOf(playerId, message, rangeTimeBegin, rangeTimeEnd);
            long i = 1;
            //遍历信息，并装填信息给前端返回
            for (Map map : worldMassageList) {
                //排除系统消息
                if ("0".equals(map.get("player_id").toString())) {
                    continue;
                }
                //排除非私聊信息(确保allChatChannelMap_ChannelId有值才进行&&后面的判断)
                if (allChatChannelMapChannelId.size() > 0 && null == allChatChannelMapChannelId.get(map.get("channel_id").toString())) {
                    continue;
                }
                //获取 被那个player_id 接收了消息(通过私人会话配置)
                String receviePlayerId = "";
                List<Map> chatChanneSendAndReceive = allChatChannelMapChannelId.get(map.get("channel_id").toString());
                for (Map map1 : chatChanneSendAndReceive) {
                    if (!map1.get("player_id").toString().equals(map.get("player_id").toString())) {
                        receviePlayerId = map1.get("player_id").toString();
                    }
                }
                //装填信息
                ChatMessageVO chatMessageVO = new ChatMessageVO();
                chatMessageVO.setId(i++);
                chatMessageVO.setServerId(serverId);
                chatMessageVO.setSendPlayerId((Long) map.get("player_id"));
                chatMessageVO.setChatChannel("私人");
                chatMessageVO.setSendPlayerName(allPlayerInfoMapPlayId.get(map.get("player_id").toString()).get(0).get("nickname").toString());
                chatMessageVO.setReceivePlayerName(allPlayerInfoMapPlayId.get(receviePlayerId).get(0).get("nickname").toString());
                chatMessageVO.setMessage(map.get("message").toString());
                chatMessageVO.setMessageTime(DateUtils.parseDate(map.get("create_time").toString()));
                chatMessageVOList.add(chatMessageVO);
            }
        } catch (Exception e) {
            log.error("通过服务器id:" + serverId + ",切换数据源异常", e);
        } finally {
            DataSourceHelper.useDefaultDatabase();
        }
        return chatMessageVOList;
    }

    @Override
    public List<ChatMessageVO> queryCrossServerChatLogList(String rangeTimeBegin, String rangeTimeEnd, Integer channelId, Integer serverId, Long playerId, String nickname, String message) {
        // TODO 支持跨服聊天查询
        List<ChatMessageVO> result = new ArrayList<>();
        return result;
    }
}
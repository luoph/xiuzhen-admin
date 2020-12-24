package org.jeecg.modules.game.service.impl;

import cn.youai.xiuzhen.utils.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.database.DataSourceHelper;
import org.jeecg.modules.game.entity.ChatMessage;
import org.jeecg.modules.game.entity.ChatMessageVO;
import org.jeecg.modules.game.entity.FriendChatChannel;
import org.jeecg.modules.game.entity.FriendChatMessage;
import org.jeecg.modules.game.mapper.ChatMessageMapper;
import org.jeecg.modules.game.mapper.FriendChatChannelMapper;
import org.jeecg.modules.game.mapper.FriendChatMessageMapper;
import org.jeecg.modules.game.mapper.GameChatLogMapper;
import org.jeecg.modules.game.service.IChatMessageService;
import org.jeecg.modules.game.service.IGameChatLogService;
import org.jeecg.modules.player.mapper.PlayerMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author huli
 * @version 1.0.0
 */
@Service
@Slf4j
public class GameChatLogServiceImpl implements IGameChatLogService {
	@Resource
	private GameChatLogMapper gameChatLogMapper;


	/**
	 * 查询世界聊天信息
	 *
	 * @param rangeTimeBegin
	 * @param rangeTimeEnd
	 * @param channelId
	 * @param serverId
	 * @param playerId
	 * @param nickname
	 * @param message
	 * @return
	 */
	@Override
	public List<ChatMessageVO> queryCommonChatLogList(String rangeTimeBegin, String rangeTimeEnd, Integer channelId, Integer serverId, Long playerId, String nickname, String message) {
		List<ChatMessageVO> chatMessageVOList = new ArrayList<>();
		List<Map> worldMassageList = null;
		try {
			// 通过serverId切换数据源
			DataSourceHelper.useServerDatabase(serverId);
			//查询所有用户信息
			List<Map> allPlayerInfoList = gameChatLogMapper.selectPlayerInfoByPlayerId();
			//用户信息以play_id分组
			Map<String, List<Map>> allPlayerInfoMap_PlayId= allPlayerInfoList.stream().collect(Collectors.groupingBy(map -> map.get("id").toString()));
			//用户信息以nickname分组
			Map<String, List<Map>> allPlayerInfoMap_nikename= allPlayerInfoList.stream().collect(Collectors.groupingBy(map -> map.get("nickname").toString()));
			//处理查询条件没有player_id,但是有nickname的情况
			if(null == playerId && !StringUtils.isEmpty(nickname)){ playerId = Long.parseLong(allPlayerInfoMap_nikename.get(nickname).get(0).get("id").toString());}
			//查询符合条件的所有世界聊天信息
			worldMassageList = gameChatLogMapper.selectWorldMassageByAllKindOf(playerId, message, rangeTimeBegin, rangeTimeEnd);
			//遍历信息，并装填信息给前端返回
			for (Map map : worldMassageList) {
				ChatMessageVO chatMessageVO = new ChatMessageVO();
				chatMessageVO.setChatChannel("世界");
				chatMessageVO.setSendPlayerName(allPlayerInfoMap_PlayId.get(map.get("player_id").toString()).get(0).get("nickname").toString());
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
	 *	逻辑：
	 * @param rangeTimeBegin
	 * @param rangeTimeEnd
	 * @param channelId
	 * @param serverId
	 * @param nickname
	 * @param playerId
	 * @param message
	 * @return
	 */
	@Override
	public List<ChatMessageVO> queryImmortalChatLogList(String rangeTimeBegin, String rangeTimeEnd, Integer channelId, Integer serverId, String nickname, Long playerId, String message) {
		List<ChatMessageVO> chatMessageVOList = new ArrayList<>();
		List<Map> worldMassageList = null;
		try {
			// 通过serverId切换数据源
			DataSourceHelper.useServerDatabase(serverId);

			//查询所有用户信息
			List<Map> allPlayerInfoList = gameChatLogMapper.selectPlayerInfoByPlayerId();
			//用户信息以play_id分组
			Map<String, List<Map>> allPlayerInfoMap_PlayId= allPlayerInfoList.stream().collect(Collectors.groupingBy(map -> map.get("id").toString()));
			//用户信息以nickname分组
			Map<String, List<Map>> allPlayerInfoMap_nikename= allPlayerInfoList.stream().collect(Collectors.groupingBy(map -> map.get("nickname").toString()));

			//查询所有仙盟会话配置
			List<Map> allChatChannelList = gameChatLogMapper.selectAllImmortalChatChannel();
			//会话配置以channel_id分组
			Map<String,List<Map>> allChatChannelMap_ChannelId = allChatChannelList.stream().collect(Collectors.groupingBy(map -> map.get("channel_id").toString()));

			//处理查询条件没有player_id,但是有nickname的情况
			if(null == playerId && !StringUtils.isEmpty(nickname)){ playerId = Long.parseLong(allPlayerInfoMap_nikename.get(nickname).get(0).get("id").toString());}
			//查询符合条件的所有仙盟聊天信息
			worldMassageList = gameChatLogMapper.selectImmortalMessageByAllKindOf(playerId, message, rangeTimeBegin, rangeTimeEnd);
			//遍历信息，并装填信息给前端返回
			for (Map map : worldMassageList) {
				//排除系统消息
				if("0".equals(map.get("player_id").toString())){
					continue;
				}
				//排除非仙盟群聊信息(确保allChatChannelMap_ChannelId有值才进行&&后面的判断)
				if(allChatChannelMap_ChannelId.size() > 0 && null == allChatChannelMap_ChannelId.get(map.get("channel_id").toString())){
					continue;
				}
				//装填信息
				ChatMessageVO chatMessageVO = new ChatMessageVO();
				chatMessageVO.setChatChannel("仙盟");
				chatMessageVO.setSendPlayerName(allPlayerInfoMap_PlayId.get(map.get("player_id").toString()).get(0).get("nickname").toString());
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
	 *
	 * @param rangeTimeBegin
	 * @param rangeTimeEnd
	 * @param channelId
	 * @param serverId
	 * @param nickname
	 * @param playerId
	 * @param message
	 * @return
	 */
	@Override
	public List<ChatMessageVO> querySelfChatLogList(String rangeTimeBegin, String rangeTimeEnd, Integer channelId, Integer serverId, String nickname, Long playerId, String message) {
		List<ChatMessageVO> chatMessageVOList = new ArrayList<>();
		List<Map> worldMassageList = null;
		try {
			// 通过serverId切换数据源
			DataSourceHelper.useServerDatabase(serverId);

			//查询所有用户信息
			List<Map> allPlayerInfoList = gameChatLogMapper.selectPlayerInfoByPlayerId();
			//用户信息以play_id分组
			Map<String, List<Map>> allPlayerInfoMap_PlayId= allPlayerInfoList.stream().collect(Collectors.groupingBy(map -> map.get("id").toString()));
			//用户信息以nickname分组
			Map<String, List<Map>> allPlayerInfoMap_nikename= allPlayerInfoList.stream().collect(Collectors.groupingBy(map -> map.get("nickname").toString()));

			//查询所有私人会话配置
			List<Map> allChatChannelList = gameChatLogMapper.selectAllSelfChatChannel();
			//会话配置以channel_id分组
			Map<String,List<Map>> allChatChannelMap_ChannelId = allChatChannelList.stream().collect(Collectors.groupingBy(map -> map.get("channel_id").toString()));

			//处理查询条件没有player_id,但是有nickname的情况
			if(null == playerId && !StringUtils.isEmpty(nickname)){ playerId = Long.parseLong(allPlayerInfoMap_nikename.get(nickname).get(0).get("id").toString());}
			//查询符合条件的所有私人聊天信息
			worldMassageList = gameChatLogMapper.selectSelfMessageByAllKindOf(playerId, message, rangeTimeBegin, rangeTimeEnd);
			//遍历信息，并装填信息给前端返回
			for (Map map : worldMassageList) {
				//排除系统消息
				if("0".equals(map.get("player_id").toString())){
					continue;
				}
				//排除非私聊信息(确保allChatChannelMap_ChannelId有值才进行&&后面的判断)
				if(allChatChannelMap_ChannelId.size() > 0 && null == allChatChannelMap_ChannelId.get(map.get("channel_id").toString())){
					continue;
				}
				//获取 被那个player_id 接收了消息(通过私人会话配置)
				String receviePlayerId = "";
				List<Map> chatChanneSendAndReceive = allChatChannelMap_ChannelId.get(map.get("channel_id").toString());
				for (Map map1 : chatChanneSendAndReceive) {
					if(!map1.get("player_id").toString().equals(map.get("player_id").toString())){receviePlayerId = map1.get("player_id").toString();}
				}
				//装填信息
				ChatMessageVO chatMessageVO = new ChatMessageVO();
				chatMessageVO.setChatChannel("私人");
				chatMessageVO.setSendPlayerName(allPlayerInfoMap_PlayId.get(map.get("player_id").toString()).get(0).get("nickname").toString());
				chatMessageVO.setReceivePlayerName(allPlayerInfoMap_PlayId.get(receviePlayerId).get(0).get("nickname").toString());
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
}

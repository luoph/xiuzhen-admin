package org.jeecg.modules.game.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.modules.game.entity.ChatMessageVO;
import org.jeecg.modules.game.service.IFriendChatMessageService;
import org.jeecg.modules.game.service.IGameChatLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 胡立
 * @version 聊天日志
 */
@Slf4j
@RestController
@RequestMapping("game/chatLog")
public class GameChatLogController {

	@Autowired
	private IFriendChatMessageService friendChatMessageService;
	@Autowired
	private IGameChatLogService iGameChatLogService ;


	/**
	 * 聊天日志
	 *
	 * @param pageNo   页码
	 * @param pageSize 分页大小
	 * @return {@linkplain Result}
	 */
	@AutoLog(value = "聊天日志-列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(String rangeTimeBegin,
								   String rangeTimeEnd,
	                               @RequestParam(name = "serverId",defaultValue = "0") Integer serverId,
	                               @RequestParam(name = "channelId",defaultValue = "0") Integer channelId,
	                               Long playerId,
	                               String nickname,
	                               @RequestParam(name = "type", defaultValue = "0") Integer type,
	                               String message,
	                               @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
	                               @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize
	) {
		Page<ChatMessageVO> pageVo = new Page<>(pageNo, pageSize);
		List<ChatMessageVO> list = new ArrayList<>();
		if(0 == type){
			return Result.error("请选择聊天频道！");
		}
		if(0 == serverId){
			return Result.error("请选择服务器！");
		}
		// 如果选择开始时间和结束时间是同一天
		if (!StringUtils.isEmpty(rangeTimeBegin) && !StringUtils.isEmpty(rangeTimeEnd) && rangeTimeBegin.equals(rangeTimeEnd)) {
			rangeTimeBegin = rangeTimeBegin + " 00:00:00";
			rangeTimeEnd = rangeTimeEnd + " 23:59:59";
		}

		if (type == 1) {
			// 公共聊天
			list = iGameChatLogService.queryCommonChatLogList(rangeTimeBegin, rangeTimeEnd, channelId, serverId, playerId, nickname, message);
			pageVo.setRecords(list).setTotal(list.size());
		} else if (type == 2) {
			// 仙盟聊天（帮派）
			list = iGameChatLogService.queryImmortalChatLogList(rangeTimeBegin, rangeTimeEnd, channelId, serverId, nickname, playerId, message);
			pageVo.setRecords(list).setTotal(list.size());
		}else if (type == 3) {
			// 私聊
			list = iGameChatLogService.querySelfChatLogList(rangeTimeBegin, rangeTimeEnd, channelId, serverId, nickname, playerId, message);
			pageVo.setRecords(list).setTotal(list.size());
		}else if(type == 4) {
			// 公共聊天
			List<ChatMessageVO> list1 = iGameChatLogService.queryCommonChatLogList(rangeTimeBegin, rangeTimeEnd, channelId, serverId, playerId, nickname, message);
			// 仙盟聊天（帮派）
			List<ChatMessageVO> list2 = iGameChatLogService.queryImmortalChatLogList(rangeTimeBegin, rangeTimeEnd, channelId, serverId, nickname, playerId, message);
			// 私聊
			List<ChatMessageVO> list3 = iGameChatLogService.querySelfChatLogList(rangeTimeBegin, rangeTimeEnd, channelId, serverId, nickname, playerId, message);
			// 整合
			list.addAll(list1);
			list.addAll(list2);
			list.addAll(list3);
			List<ChatMessageVO> listAll =list.stream().sorted((s1, s2) -> s2.getMessageTime().compareTo(s1.getMessageTime())).collect(Collectors.toList());
			pageVo.setRecords(listAll).setTotal(listAll.size());
		}
			return Result.ok(pageVo);
	}
}

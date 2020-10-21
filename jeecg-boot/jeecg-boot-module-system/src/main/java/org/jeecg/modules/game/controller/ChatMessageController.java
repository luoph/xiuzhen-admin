package org.jeecg.modules.game.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.modules.game.entity.*;
import org.jeecg.modules.game.service.IChatMessageService;
import org.jeecg.modules.game.service.IFriendChatMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xubodong
 * @version 1.0.0
 * @date 2020/10/20 21:22
 */
@Slf4j
@RestController
@RequestMapping("game/chatMessage")
public class ChatMessageController {

	@Autowired
	private IFriendChatMessageService friendChatMessageService;
	@Autowired
	private IChatMessageService chatMessageService;


	/**
	 * 分页列表查询
	 *
	 * @param pageNo   页码
	 * @param pageSize 分页大小
	 * @return {@linkplain Result}
	 */
	@AutoLog(value = "聊天日志-列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(@RequestParam(name = "rangeTimeBegin") String rangeTimeBegin,
	                               @RequestParam(name = "rangeTimeEnd") String rangeTimeEnd,
	                               @RequestParam(name = "serverId") Integer serverId,
	                               @RequestParam(name = "channelId") Integer channelId,
	                               @RequestParam(name = "playerId") Long playerId,
	                               @RequestParam(name = "nickname") String nickname,
	                               @RequestParam(name = "type", defaultValue = "0") Integer type,
	                               @RequestParam(name = "message") String message,
	                               @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
	                               @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize
	) {
		Page<ChatMessageVO> pageVo = new Page<>(pageNo, pageSize);
		List<ChatMessageVO> list = new ArrayList<>();
		if (StringUtils.isEmpty(rangeTimeBegin) && StringUtils.isEmpty(rangeTimeEnd) && serverId == 0 && channelId == 0
				) {
			return Result.ok(pageVo);
		}
		// 如果选择开始时间和结束时间是同一天
		if (rangeTimeBegin.equals(rangeTimeEnd)) {
			rangeTimeBegin = rangeTimeBegin + " 00:00:00";
			rangeTimeEnd = rangeTimeEnd + " 23:59:59";
		}
		// 公共聊天
		if (type == 1) {
			list = chatMessageService.queryForList(rangeTimeBegin, rangeTimeEnd, channelId, serverId, playerId, nickname, message);
			pageVo.setRecords(list).setTotal(list.size());
		} else if (type == 2) {
			// 私聊
			// 把玩家信息作为发送方条件和接收方方条件
			list = chatMessageService.queryListBySenderAndReceive(rangeTimeBegin, rangeTimeEnd, channelId, serverId, nickname, playerId, message);
			pageVo.setRecords(list).setTotal(list.size());
		}
		return Result.ok(pageVo);
	}
}

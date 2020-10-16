package org.jeecg.modules.game.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.modules.game.entity.GameOnlineNum;
import org.jeecg.modules.game.service.IGameChannelService;
import org.jeecg.modules.game.service.IGameOnlineNumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 在线情况
 * @date 2020-10-15
 */
@Slf4j
@RestController
@RequestMapping("game/gameOnlineNum")
public class GameOnlineNumController extends JeecgController<GameOnlineNum, IGameOnlineNumService> {

	@Autowired
	private IGameOnlineNumService gameOnlineNumService;

	@Autowired
	private IGameChannelService gameChannelService;

	/**
	 * 分页列表查询
	 *
	 * @param pageNo   页码
	 * @param pageSize 分页大小
	 * @return {@linkplain Result}
	 */
	@AutoLog(value = "在线情况-列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(GameOnlineNum gameOnlineNum,
	                               @RequestParam(name = "rangeDateBegin", defaultValue = "") String rangeDateBegin,
	                               @RequestParam(name = "rangeDateEnd", defaultValue = "") String rangeDateEnd,
	                               @RequestParam(name = "days", defaultValue = "0") int days,
	                               @RequestParam(name = "serverId", defaultValue = "0") Integer serverId,
	                               @RequestParam(name = "channelId", defaultValue = "0") Integer channelId,
	                               @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
	                               @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize
	) {
		Page<GameOnlineNum> page = new Page<>(pageNo, pageSize);
		if (StringUtils.isEmpty(rangeDateBegin) && StringUtils.isEmpty(rangeDateEnd) && serverId == 0 && channelId == 0 && days == 0) {
			return Result.ok(page);
		}
		String channel = gameChannelService.queryChannelNameById(channelId);
		List<GameOnlineNum> gameOnlineNumList = gameOnlineNumService.queryGameOnlineNumByRangDate(rangeDateBegin, rangeDateEnd, days, serverId, channel);
		page.setRecords(gameOnlineNumList).setTotal(gameOnlineNumList.size());
		return Result.ok(page);
	}

	/**
	 * 分页列表查询
	 *
	 * @param pageNo   页码
	 * @param pageSize 分页大小
	 * @return {@linkplain Result}
	 */
	@AutoLog(value = "在线情况统计-列表查询")
	@GetMapping(value = "/collectList")
	public Result<?> collectList(GameOnlineNum gameOnlineNum,
	                             @RequestParam(name = "rangeDateBegin", defaultValue = "") String rangeDateBegin,
	                             @RequestParam(name = "rangeDateEnd", defaultValue = "") String rangeDateEnd,
	                             @RequestParam(name = "days", defaultValue = "0") int days,
	                             @RequestParam(name = "serverId", defaultValue = "0") Integer serverId,
	                             @RequestParam(name = "channelId", defaultValue = "0") Integer channelId,
	                             @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
	                             @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize
	) {
		Page<GameOnlineNum> page = new Page<>(pageNo, pageSize);
		if (StringUtils.isEmpty(rangeDateBegin) && StringUtils.isEmpty(rangeDateEnd) && serverId == 0 && channelId == 0 && days == 0) {
			return Result.ok(page);
		}
		String channel = gameChannelService.queryChannelNameById(channelId);
		List<GameOnlineNum> gameOnlineNumList = gameOnlineNumService.queryGameOnlineCollectByRangDate(rangeDateBegin, rangeDateEnd, days, serverId, channel);
		page.setRecords(gameOnlineNumList).setTotal(gameOnlineNumList.size());
		return Result.ok(page);
	}


}

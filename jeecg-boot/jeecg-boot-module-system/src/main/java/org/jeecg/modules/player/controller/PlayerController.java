package org.jeecg.modules.player.controller;

import cn.youai.commons.model.DataResponse;
import cn.youai.xiuzhen.entity.pojo.RoleAttr;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.okhttp.OkHttpHelper;
import org.jeecg.modules.base.MultiDataSourceController;
import org.jeecg.modules.game.entity.GameServer;
import org.jeecg.modules.game.service.IGameServerService;
import org.jeecg.modules.player.entity.Player;
import org.jeecg.modules.player.entity.PlayerDTO;
import org.jeecg.modules.player.entity.PlayerRegisterInfo;
import org.jeecg.modules.player.service.IPlayerRegisterInfoService;
import org.jeecg.modules.player.service.IPlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 玩家
 * @date 2020-10-19
 */
@Slf4j
@RestController
@RequestMapping("game/player")
public class PlayerController extends MultiDataSourceController<Player, IPlayerService> {

	@Autowired
	private IPlayerService playerService;

	@Autowired
	private IGameServerService gameServerService;

	@Autowired
	private IPlayerRegisterInfoService playerRegisterInfoService;

	/**
	 * 分页列表查询
	 *
	 * @param pageNo   页码
	 * @param pageSize 分页大小
	 * @return {@linkplain Result}
	 */
	@AutoLog(value = "玩家-列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(PlayerDTO playerDTO,
	                               @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
	                               @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
		Page<Player> page = new Page<>(pageNo, pageSize);
		if (playerDTO == null) {
			return Result.ok(page);
		}
		// 如果选择开始时间和结束时间是同一天
		List<Player> list = playerService.queryForList(playerDTO);
		page.setRecords(list).setTotal(list.size());
		return Result.ok(page);
	}

	/**
	 * 分页列表查询
	 *
	 * @return {@linkplain Result}
	 */
	@AutoLog(value = "玩家详情-查询")
	@GetMapping(value = "/detail")
	public Result<?> queryDetai(@RequestParam(name = "playerId", defaultValue = "0") Integer playerId
	) {
		LambdaQueryWrapper<PlayerRegisterInfo> queryWrapper = Wrappers.<PlayerRegisterInfo>lambdaQuery().eq(PlayerRegisterInfo::getPlayerId, playerId);
		PlayerRegisterInfo registerInfo = playerRegisterInfoService.getOne(queryWrapper);
		if (null == registerInfo) {
			return Result.error("玩家信息不存在");
		}
		int serverId = registerInfo.getServerId();
		GameServer gameServer = gameServerService.getById(serverId);
		// http调用查询玩家详情
		String str = OkHttpHelper.get(gameServer.getGmUrl() + "/player/info?playerId=" + playerId);
		JSONObject jsonObject = JSONObject.parseObject(JSONObject.toJSONString(JSON.parseObject(str, DataResponse.class).getData()));
		String roleAttrStr = jsonObject.getString("roleAttr");
		RoleAttr roleAttr = JSONObject.parseObject(roleAttrStr, RoleAttr.class);
		return Result.ok(roleAttr);
	}

}

















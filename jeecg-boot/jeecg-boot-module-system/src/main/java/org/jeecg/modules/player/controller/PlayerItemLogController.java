package org.jeecg.modules.player.controller;

import cn.youai.commons.model.ResponseCode;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.game.entity.GameServer;
import org.jeecg.modules.game.service.IGameServerService;
import org.jeecg.modules.player.entity.BackpackLog;
import org.jeecg.modules.player.entity.PlayerItemLog;
import org.jeecg.modules.player.service.BackpackLogService;
import org.jeecg.modules.player.service.IPlayerItemLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description player_item_log
 * @date 2020-07-21
 */
@Slf4j
@RestController
@RequestMapping("player/playerItemLog")
public class PlayerItemLogController extends JeecgController<PlayerItemLog, IPlayerItemLogService> {

    @Autowired
    private IPlayerItemLogService playerItemLogService;
    @Autowired
    private IGameServerService gameServerService;
    @Autowired
    private BackpackLogService backpackLogService;

    /**
     * 分页列表查询
     *
     * @param playerItemLog 数据实体
     * @param pageNo        页码
     * @param pageSize      分页大小
     * @param req           请求
     * @return {@linkplain Result}
     */
    @AutoLog(value = "player_item_log-列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(PlayerItemLog playerItemLog,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<PlayerItemLog> queryWrapper = QueryGenerator.initQueryWrapper(playerItemLog, req.getParameterMap());
        queryWrapper.orderByDesc("create_time");
        Page<PlayerItemLog> page = new Page<>(pageNo, pageSize);
        IPage<PlayerItemLog> pageList = playerItemLogService.page(page, queryWrapper);
        return Result.ok(pageList);
    }

    /**
     * 编辑
     *
     * @return {@linkplain Result}
     */
    @AutoLog(value = "手动同步日志-编辑")
    @GetMapping(value = "/sync")
    public Result<?> edit(BackpackLog backpackLog,
                          @RequestParam(name = "syncTimeBegin") String syncTimeBegin,
                          @RequestParam(name = "syncTimeEnd") String syncTimeEnd,
                          @RequestParam(name = "serverId") Integer serverId,
                          @RequestParam(name = "playerId", defaultValue = "0", required = false) Integer playerId,
                          HttpServletRequest req
    ) {
        GameServer gameServer = gameServerService.getOne(Wrappers.<GameServer>lambdaQuery().eq(GameServer::getId, serverId));
        if (gameServer == null) {
            return Result.error("所选服务器不存在！");
        }
        ResponseCode responseCode = backpackLogService.syncBackpackLog(backpackLog, playerId, serverId, req.getParameterMap(), syncTimeBegin, syncTimeEnd);
        if (responseCode.isSuccess()) {
            return Result.ok("同步成功!");
        } else {
            return Result.error(responseCode.getDesc());
        }
    }
}
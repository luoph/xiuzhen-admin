package org.jeecg.modules.player.controller;

import cn.youai.commons.model.ResponseCode;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.game.entity.GameServer;
import org.jeecg.modules.game.service.IGameServerService;
import org.jeecg.modules.player.entity.BackpackLog;
import org.jeecg.modules.player.entity.GamePlayerItemLog;
import org.jeecg.modules.player.service.BackpackLogService;
import org.jeecg.modules.player.service.IGamePlayerItemLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description player_item_log
 * @date 2020-07-21
 */
@Slf4j
@RestController
@RequestMapping("player/playerItemLog")
public class PlayerItemLogController extends JeecgController<GamePlayerItemLog, IGamePlayerItemLogService> {

    @Autowired
    private IGamePlayerItemLogService playerItemLogService;
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
    @AutoLog(value = "game_player_item_log-列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(GamePlayerItemLog playerItemLog,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<GamePlayerItemLog> queryWrapper = QueryGenerator.initQueryWrapper(playerItemLog, req.getParameterMap());
        queryWrapper.orderByDesc("create_time");
        Page<GamePlayerItemLog> page = new Page<>(pageNo, pageSize);
        if (playerItemLog.getPlayerId() == null || playerItemLog.getServerId() == null || playerItemLog.getItemId() == null || playerItemLog.getSyncTime() == null) {
            page.setRecords(new ArrayList<>()).setTotal(0);
            return Result.ok(page);
        }
        IPage<GamePlayerItemLog> pageList = playerItemLogService.page(page, queryWrapper);
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
                          @RequestParam(name = "playerId", defaultValue = "0", required = false) Long playerId,
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

    /**
     * 分页列表查询
     *
     * @param pageNo   页码
     * @param pageSize 分页大小
     * @return {@linkplain Result}
     */
    @AutoLog(value = "货币产销-列表查询")
    @GetMapping(value = "/currencyPayIncomeList")
    public Result<?> currencyPayIncomeList(@RequestParam(name = "rangeDateBegin", defaultValue = "") String rangeDateBegin,
                                           @RequestParam(name = "rangeDateEnd", defaultValue = "") String rangeDateEnd,
                                           @RequestParam(name = "days", defaultValue = "0") int days,
                                           @RequestParam(name = "itemId", defaultValue = "0") int itemId,
                                           @RequestParam(name = "serverId", defaultValue = "0") Integer serverId,
                                           @RequestParam(name = "channelId", defaultValue = "0") Integer channelId,
                                           @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                           @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        Page<GamePlayerItemLog> page = new Page<>(pageNo, pageSize);
        if (StringUtils.isEmpty(rangeDateBegin) && StringUtils.isEmpty(rangeDateEnd) && serverId == 0 && channelId == 0 && days == 0 && itemId == 0) {
            return Result.ok(page);
        }
        // 如果选择开始时间和结束时间是同一天
        if (rangeDateBegin.equals(rangeDateEnd)) {
            rangeDateBegin = rangeDateBegin + " 00:00:00";
            rangeDateEnd = rangeDateEnd + " 23:59:59";
        }
        List<GamePlayerItemLog> playerItemLogs = playerItemLogService.queryCurrencyPayIncomeList(rangeDateBegin, rangeDateEnd, days, serverId, itemId);
        page.setRecords(playerItemLogs).setTotal(playerItemLogs.size());
        return Result.ok(page);
    }


    /**
     * 分页列表查询
     *
     * @param pageNo   页码
     * @param pageSize 分页大小
     * @return {@linkplain Result}
     */
    @AutoLog(value = "途径分布-列表查询")
    @GetMapping(value = "/wayDistributeList")
    public Result<?> wayDistributeList(@RequestParam(name = "rangeDateBegin", defaultValue = "") String rangeDateBegin,
                                       @RequestParam(name = "rangeDateEnd", defaultValue = "") String rangeDateEnd,
                                       @RequestParam(name = "days", defaultValue = "0") int days,
                                       @RequestParam(name = "itemId", defaultValue = "0") int itemId,
                                       @RequestParam(name = "type", defaultValue = "0") int type,
                                       @RequestParam(name = "serverId", defaultValue = "0") Integer serverId,
                                       @RequestParam(name = "channelId", defaultValue = "0") Integer channelId,
                                       @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                       @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        Page<GamePlayerItemLog> page = new Page<>(pageNo, pageSize);
        if (StringUtils.isEmpty(rangeDateBegin) && StringUtils.isEmpty(rangeDateEnd) && serverId == 0
                && channelId == 0 && days == 0 && itemId == 0 && type == 0) {
            return Result.ok(page);
        }
        // 如果选择开始时间和结束时间是同一天
        if (rangeDateBegin.equals(rangeDateEnd)) {
            rangeDateBegin = rangeDateBegin + " 00:00:00";
            rangeDateEnd = rangeDateEnd + " 23:59:59";
        }
        List<GamePlayerItemLog> playerItemLogs = playerItemLogService.queryWayDistributeList(rangeDateBegin, rangeDateEnd, days, serverId, itemId, type);
        page.setRecords(playerItemLogs).setTotal(playerItemLogs.size());
        return Result.ok(page);
    }

    /**
     * 分页列表查询
     *
     * @param pageNo   页码
     * @param pageSize 分页大小
     * @return {@linkplain Result}
     */
    @AutoLog(value = "物品流水-列表查询")
    @GetMapping(value = "/itemBillList")
    public Result<?> itemBillList(@RequestParam(name = "rangeDateBegin", defaultValue = "") String rangeDateBegin,
                                  @RequestParam(name = "rangeDateEnd", defaultValue = "") String rangeDateEnd,
                                  @RequestParam(name = "way", defaultValue = "0") int way,
                                  @RequestParam(name = "itemId", defaultValue = "0") int itemId,
                                  @RequestParam(name = "type", defaultValue = "0") int type,
                                  @RequestParam(name = "serverId", defaultValue = "0") Integer serverId,
                                  @RequestParam(name = "playerId", defaultValue = "0") Long playerId,
                                  @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                  @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        Page<GamePlayerItemLog> page = new Page<>(pageNo, pageSize);
        if (StringUtils.isEmpty(rangeDateBegin) && StringUtils.isEmpty(rangeDateEnd) && serverId == 0
                && playerId == 0 && way == 0 && itemId == 0 && type == 0) {
            return Result.ok(page);
        }
        // 如果选择开始时间和结束时间是同一天
        if (rangeDateBegin.equals(rangeDateEnd)) {
            rangeDateBegin = rangeDateBegin + " 00:00:00";
            rangeDateEnd = rangeDateEnd + " 23:59:59";
        }
        List<GamePlayerItemLog> playerItemLogs = playerItemLogService.queryItemBillList(rangeDateBegin, rangeDateEnd, way, serverId, itemId, type, playerId);
        page.setRecords(playerItemLogs).setTotal(playerItemLogs.size());
        return Result.ok(page);
    }


}

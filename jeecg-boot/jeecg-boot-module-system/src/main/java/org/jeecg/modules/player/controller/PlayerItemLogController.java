package org.jeecg.modules.player.controller;

import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.modules.base.PlayerDataSourceController;
import org.jeecg.modules.game.service.IGamePlayerService;
import org.jeecg.modules.player.entity.GamePlayerItemLog;
import org.jeecg.modules.player.service.IGamePlayerItemLogService;
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
public class PlayerItemLogController extends PlayerDataSourceController<GamePlayerItemLog, IGamePlayerItemLogService> {

    @Autowired
    private IGamePlayerService playerService;

    /**
     * 分页列表查询
     *
     * @param entity 数据实体
     * @param req    请求
     * @return {@linkplain Result}
     */
    @AutoLog(value = "game_player_item_log-列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(GamePlayerItemLog entity,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "20") Integer pageSize,
                                   HttpServletRequest req) {
        if (null == entity.getPlayerId()) {
            return Result.error("请输入玩家id！");
        }

        return queryPageList(entity.getPlayerId(), entity, pageNo, pageSize, req);
    }

}

package org.jeecg.modules.player.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.player.entity.PlayerItemLog;
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
        Page<PlayerItemLog> page = new Page<>(pageNo, pageSize);
        IPage<PlayerItemLog> pageList = playerItemLogService.page(page, queryWrapper);
        return Result.ok(pageList);
    }

}

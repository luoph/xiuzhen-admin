package org.jeecg.modules.game.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DatePattern;
import cn.youai.commons.model.ResponseCode;
import cn.youai.xiuzhen.utils.DateUtils;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.game.entity.*;
import org.jeecg.modules.game.mapper.GameDataRemainMapper;
import org.jeecg.modules.game.mapper.GameDayDataCountMapper;
import org.jeecg.modules.game.mapper.GameLtvCountMapper;
import org.jeecg.modules.game.service.*;
import org.jeecg.modules.game.util.ParamValidUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @ClassName GameDataCountController
 * @Description 游戏数据统计
 * @Author buliangliang
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2020-08-20 17:31
 */
@Slf4j
@RestController
@Api(tags = "游戏统计")
@RequestMapping("game/gameDataCountController")
public class GameDataCountController {

    @Autowired
    private IGameDataCountService gameDataCountService;
    @Autowired
    private IGameDayDataCountService gameDayDataCountService;
    @Autowired
    private IGameDataRemainService gameDataRemainService;
    @Autowired
    private IGameLtvCountService gameLtvCountService;
    @Autowired
    private IGameChannelServerService gameChannelServerService;
    @Autowired
    private IGameServerService gameServerService;
    @Resource
    private GameDayDataCountMapper gameDayDataCountMapper;
    @Resource
    private GameLtvCountMapper gameLtvCountMapper;
    @Resource
    private GameDataRemainMapper gameDataRemainMapper;


    @GetMapping(value = "/dayCount")
    public Result<?> queryGameDataCountList(
            @RequestParam(value = "channelId", defaultValue = "0") Integer channelId,
            @RequestParam(value = "serverId", defaultValue = "0") Integer serverId,
            @RequestParam(value = "rangeDateBegin", defaultValue = "") String rangeDateBegin,
            @RequestParam(value = "rangeDateEnd", defaultValue = "") String rangeDateEnd,
            @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
            HttpServletRequest req) {
        Page<GameDayDataCount> page = new Page<>(pageNo, pageSize);
        boolean paramValidCheck = ParamValidUtil.isParamInValidCheck(channelId, serverId, rangeDateBegin, rangeDateEnd);
        if (!paramValidCheck && DateUtils.isSameDay(DateUtils.dateOnly(new Date()), DateUtils.parseDate(rangeDateBegin)) && DateUtils.isSameDay(DateUtils.dateOnly(new Date()), DateUtils.parseDate(rangeDateEnd))) {
            // 验证通过
            ResponseCode responseCode = ParamValidUtil.dateRangeValid(rangeDateBegin, rangeDateEnd);
            if (!responseCode.isSuccess()) {
                return Result.error(responseCode.getDesc());
            }
            List<GameDayDataCount> gameDataCounts = gameDataCountService.queryDateRangeDataCount(channelId, serverId, rangeDateBegin, rangeDateEnd);
            page.setRecords(gameDataCounts).setTotal(gameDataCounts.size());
            return Result.ok(page);
        } else {
            IPage<GameDayDataCount> list = gameDayDataCountService.selectList(page, channelId, serverId, rangeDateBegin, rangeDateEnd);
            if (StringUtils.isBlank(rangeDateBegin) && StringUtils.isBlank(rangeDateEnd) && CollUtil.isEmpty(list.getRecords())) {
                //同步
                List<GameChannelServer> channelServers = gameChannelServerService.list();
                List<GameDayDataCount> allCount = new ArrayList<>();
                for (GameChannelServer channelServer : channelServers) {
                    GameServer gameServer = gameServerService.getById(channelServer.getServerId());
                    rangeDateBegin = DateUtils.formatDate(gameServer.getOpenTime(), DatePattern.NORM_DATE_PATTERN);
                    rangeDateEnd = DateUtils.formatDate(DateUtils.addDays(DateUtils.now(), -1), DatePattern.NORM_DATE_PATTERN);
                    List<GameDayDataCount> gameDayCounts = gameDataCountService.queryDateRangeDataCount(Integer.valueOf(channelServer.getChannelId()), channelServer.getServerId(), rangeDateBegin, rangeDateEnd);
                    allCount.addAll(gameDayCounts);
                }
                list.setRecords(allCount).setTotal(allCount.size());
                gameDayDataCountMapper.updateOrInsert(allCount);
            }
            return Result.ok(list);
        }
    }

    @GetMapping(value = "/remainRate")
    public Result<?> queryGameRemainCount(
            @RequestParam(value = "channelId", defaultValue = "0") Integer channelId,
            @RequestParam(value = "serverId", defaultValue = "0") Integer serverId,
            @RequestParam(value = "rangeDateBegin", defaultValue = "") String rangeDateBegin,
            @RequestParam(value = "rangeDateEnd", defaultValue = "") String rangeDateEnd,
            @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
            HttpServletRequest req) {
        Page<GameDataRemain> page = new Page<>(pageNo, pageSize);
        boolean paramValidCheck = ParamValidUtil.isParamInValidCheck(channelId, serverId, rangeDateBegin, rangeDateEnd);
        if (!paramValidCheck && DateUtils.isSameDay(DateUtils.dateOnly(new Date()), DateUtils.parseDate(rangeDateBegin)) && DateUtils.isSameDay(DateUtils.dateOnly(new Date()), DateUtils.parseDate(rangeDateEnd))) {
            // 验证通过
            ResponseCode responseCode = ParamValidUtil.dateRangeValid(rangeDateBegin, rangeDateEnd);
            if (!responseCode.isSuccess()) {
                return Result.error(responseCode.getDesc());
            }
            List<GameDataRemain> gameDataRemains = gameDataCountService.queryDataRemainCount(channelId, serverId, rangeDateBegin, rangeDateEnd);
            page.setRecords(gameDataRemains).setTotal(gameDataRemains.size());
            return Result.ok(page);
        } else {
            IPage<GameDataRemain> list = gameDataRemainService.selectList(page, channelId, serverId, rangeDateBegin, rangeDateEnd);
            if (StringUtils.isBlank(rangeDateBegin) && StringUtils.isBlank(rangeDateEnd) && CollUtil.isEmpty(list.getRecords())) {
                // 同步
                List<GameChannelServer> channelServers = gameChannelServerService.list();
                List<GameDataRemain> allCount = new ArrayList<>();
                for (GameChannelServer channelServer : channelServers) {
                    GameServer gameServer = gameServerService.getById(channelServer.getServerId());
                    rangeDateBegin = DateUtils.formatDate(gameServer.getOpenTime(), DatePattern.NORM_DATE_PATTERN);
                    rangeDateEnd = DateUtils.formatDate(DateUtils.addDays(DateUtils.now(), -1), DatePattern.NORM_DATE_PATTERN);
                    List<GameDataRemain> gameLtvCounts = gameDataCountService.queryDataRemainCount(Integer.valueOf(channelServer.getChannelId()), channelServer.getServerId(), rangeDateBegin, rangeDateEnd);
                    allCount.addAll(gameLtvCounts);
                }
                list.setRecords(allCount).setTotal(allCount.size());
                gameDataRemainMapper.updateOrInsert(allCount);
            }
            return Result.ok(list);
        }

    }

    @GetMapping(value = "/ltvCount")
    public Result<?> queryGameLtvCount(@RequestParam(value = "channelId", defaultValue = "0") Integer channelId,
                                       @RequestParam(value = "serverId", defaultValue = "0") Integer serverId,
                                       @RequestParam(value = "rangeDateBegin", defaultValue = "") String rangeDateBegin,
                                       @RequestParam(value = "rangeDateEnd", defaultValue = "") String rangeDateEnd,
                                       @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
                                       @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                                       HttpServletRequest req) {

        Page<GameLtvCount> page = new Page<>(pageNo, pageSize);
        boolean paramValidCheck = ParamValidUtil.isParamInValidCheck(channelId, serverId, rangeDateBegin, rangeDateEnd);
        if (!paramValidCheck && DateUtils.isSameDay(DateUtils.dateOnly(new Date()), DateUtils.parseDate(rangeDateBegin)) && DateUtils.isSameDay(DateUtils.dateOnly(new Date()), DateUtils.parseDate(rangeDateEnd))) {
            // 验证通过
            ResponseCode responseCode = ParamValidUtil.dateRangeValid(rangeDateBegin, rangeDateEnd);
            if (!responseCode.isSuccess()) {
                return Result.error(responseCode.getDesc());
            }
            List<GameLtvCount> gameLtvCounts = gameDataCountService.queryDataLtvCount(channelId, serverId, rangeDateBegin, rangeDateEnd);
            page.setRecords(gameLtvCounts).setTotal(gameLtvCounts.size());
            return Result.ok(page);
        } else {
            IPage<GameLtvCount> list = gameLtvCountService.selectList(page, channelId, serverId, rangeDateBegin, rangeDateEnd);
            if (StringUtils.isBlank(rangeDateBegin) && StringUtils.isBlank(rangeDateEnd) && CollUtil.isEmpty(list.getRecords())) {
                // 同步
                List<GameChannelServer> channelServers = gameChannelServerService.list();
                List<GameLtvCount> allCount = new ArrayList<>();
                for (GameChannelServer channelServer : channelServers) {
                    GameServer gameServer = gameServerService.getById(channelServer.getServerId());
                    rangeDateBegin = DateUtils.formatDate(gameServer.getOpenTime(), DatePattern.NORM_DATE_PATTERN);
                    rangeDateEnd = DateUtils.formatDate(DateUtils.addDays(DateUtils.now(), -1), DatePattern.NORM_DATE_PATTERN);
                    List<GameLtvCount> gameLtvCounts = gameDataCountService.queryDataLtvCount(Integer.valueOf(channelServer.getChannelId()), channelServer.getServerId(), rangeDateBegin, rangeDateEnd);
                    allCount.addAll(gameLtvCounts);
                }
                list.setRecords(allCount).setTotal(allCount.size());
                gameLtvCountMapper.updateOrInsert(allCount);
            }
            return Result.ok(list);
        }
    }
}

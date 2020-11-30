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
import org.jeecg.modules.game.mapper.GameCountOngoingMapper;
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
import java.util.stream.Collectors;

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
    @Autowired
    private IGameChannelService gameChannelService;
    @Autowired
    private IGameCountOngoingService gameCountOngoingService;
    @Resource
    private GameCountOngoingMapper gameCountOngoingMapper;


    @GetMapping(value = "/dayCount")
    public Result<?> queryGameDataCountList(
            @RequestParam(value = "channelId", defaultValue = "0") Integer channelId,
            @RequestParam(value = "serverId", defaultValue = "0") Integer serverId,
            @RequestParam(value = "rangeDateBegin", defaultValue = "") String rangeDateBegin,
            @RequestParam(value = "rangeDateEnd", defaultValue = "") String rangeDateEnd,
            @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
            HttpServletRequest req) {
        Page<GameStatDayDataCount> page = new Page<>(pageNo, pageSize);
        boolean paramValidCheck = ParamValidUtil.isParamInValidCheck(channelId, serverId, rangeDateBegin, rangeDateEnd);
        if (!paramValidCheck && DateUtils.isSameDay(DateUtils.dateOnly(new Date()), DateUtils.parseDate(rangeDateBegin)) && DateUtils.isSameDay(DateUtils.dateOnly(new Date()), DateUtils.parseDate(rangeDateEnd))) {
            // 验证通过
            return rsp(serverId, channelId, rangeDateBegin, rangeDateEnd, page, 1);
        } else {
            if (paramValidCheck) {
                return Result.ok(page);
            }
            IPage<GameStatDayDataCount> list = gameDayDataCountService.selectList(page, channelId, serverId, rangeDateBegin, rangeDateEnd);
            if (StringUtils.isBlank(rangeDateBegin) && StringUtils.isBlank(rangeDateEnd) && CollUtil.isEmpty(list.getRecords())) {
                //同步
                List<GameChannelServer> channelServers = gameChannelServerService.list();
                channelServers = channelServers.stream().filter(gameChannelServer -> gameChannelServer.getDelFlag() == 0 && gameChannelServer.getNoNeedCount() == 0).collect(Collectors.toList());
                List<GameStatDayDataCount> allCount = new ArrayList<>();
                for (GameChannelServer channelServer : channelServers) {
                    GameServer gameServer = gameServerService.getById(channelServer.getServerId());
                    GameChannel gameChannel = gameChannelService.getById(channelServer.getChannelId());
                    rangeDateBegin = DateUtils.formatDate(gameServer.getOpenTime(), DatePattern.NORM_DATE_PATTERN);
                    rangeDateEnd = DateUtils.formatDate(DateUtils.addDays(DateUtils.now(), -1), DatePattern.NORM_DATE_PATTERN);
                    List<GameStatDayDataCount> gameDayCounts = gameDataCountService.queryDateRangeDataCount(gameChannel, gameServer, rangeDateBegin, rangeDateEnd);
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
        Page<GameStatDataRemain> page = new Page<>(pageNo, pageSize);
        boolean paramValidCheck = ParamValidUtil.isParamInValidCheck(channelId, serverId, rangeDateBegin, rangeDateEnd);
        if (!paramValidCheck && DateUtils.isSameDay(DateUtils.dateOnly(new Date()), DateUtils.parseDate(rangeDateBegin)) && DateUtils.isSameDay(DateUtils.dateOnly(new Date()), DateUtils.parseDate(rangeDateEnd))) {
            // 验证通过
            return rsp(serverId, channelId, rangeDateBegin, rangeDateEnd, page, 2);
        } else {
            if (paramValidCheck) {
                return Result.ok(page);
            }
            IPage<GameStatDataRemain> list = gameDataRemainService.selectList(page, channelId, serverId, rangeDateBegin, rangeDateEnd);
            if (StringUtils.isBlank(rangeDateBegin) && StringUtils.isBlank(rangeDateEnd) && CollUtil.isEmpty(list.getRecords())) {
                // 同步
                List<GameChannelServer> channelServers = gameChannelServerService.list();
                channelServers = channelServers.stream().filter(gameChannelServer -> gameChannelServer.getDelFlag() == 0 && gameChannelServer.getNoNeedCount() == 0).collect(Collectors.toList());
                List<GameStatDataRemain> allCount = new ArrayList<>();
                for (GameChannelServer channelServer : channelServers) {
                    GameServer gameServer = gameServerService.getById(channelServer.getServerId());
                    GameChannel gameChannel = gameChannelService.getById(channelServer.getChannelId());
                    rangeDateBegin = DateUtils.formatDate(gameServer.getOpenTime(), DatePattern.NORM_DATE_PATTERN);
                    rangeDateEnd = DateUtils.formatDate(DateUtils.addDays(DateUtils.now(), -1), DatePattern.NORM_DATE_PATTERN);
                    List<GameStatDataRemain> gameLtvCounts = gameDataCountService.queryDataRemainCount(gameChannel, gameServer, rangeDateBegin, rangeDateEnd);
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

        Page<GameStatLtvCount> page = new Page<>(pageNo, pageSize);
        boolean paramValidCheck = ParamValidUtil.isParamInValidCheck(channelId, serverId, rangeDateBegin, rangeDateEnd);
        if (!paramValidCheck && DateUtils.isSameDay(DateUtils.dateOnly(new Date()), DateUtils.parseDate(rangeDateBegin)) && DateUtils.isSameDay(DateUtils.dateOnly(new Date()), DateUtils.parseDate(rangeDateEnd))) {
            // 验证通过
            return rsp(serverId, channelId, rangeDateBegin, rangeDateEnd, page, 3);
        } else {
            if (paramValidCheck) {
                return Result.ok(page);
            }
            IPage<GameStatLtvCount> list = gameLtvCountService.selectList(page, channelId, serverId, rangeDateBegin, rangeDateEnd);
            if (StringUtils.isBlank(rangeDateBegin) && StringUtils.isBlank(rangeDateEnd) && CollUtil.isEmpty(list.getRecords())) {
                // 同步
                List<GameChannelServer> channelServers = gameChannelServerService.list();
                channelServers = channelServers.stream().filter(gameChannelServer -> gameChannelServer.getDelFlag() == 0 && gameChannelServer.getNoNeedCount() == 0).collect(Collectors.toList());
                List<GameStatLtvCount> allCount = new ArrayList<>();
                for (GameChannelServer channelServer : channelServers) {
                    GameServer gameServer = gameServerService.getById(channelServer.getServerId());
                    GameChannel gameChannel = gameChannelService.getById(channelServer.getChannelId());
                    rangeDateBegin = DateUtils.formatDate(gameServer.getOpenTime(), DatePattern.NORM_DATE_PATTERN);
                    rangeDateEnd = DateUtils.formatDate(DateUtils.addDays(DateUtils.now(), -1), DatePattern.NORM_DATE_PATTERN);
                    List<GameStatLtvCount> gameLtvCounts = gameDataCountService.queryDataLtvCount(gameChannel, gameServer, rangeDateBegin, rangeDateEnd);
                    allCount.addAll(gameLtvCounts);
                }
                list.setRecords(allCount).setTotal(allCount.size());
                gameLtvCountMapper.updateOrInsert(allCount);
            }
            return Result.ok(list);
        }
    }

    /**
     * @param serverId
     * @param channelId
     * @param rangeDateBegin
     * @param rangeDateEnd
     * @param page
     * @param type           1-daily 2-remain 3-ltv
     * @return
     */
    private Result<?> rsp(int serverId, int channelId, String rangeDateBegin, String rangeDateEnd, IPage page, int type) {
        GameServer gameServer = gameServerService.getById(serverId);
        GameChannel gameChannel = gameChannelService.getById(channelId);
        ResponseCode responseCode = ParamValidUtil.dateRangeValid(rangeDateBegin, rangeDateEnd);
        if (!responseCode.isSuccess()) {
            return Result.error(responseCode.getDesc());
        }
        if (type == 1) {
            List<GameStatDayDataCount> gameDayDataCounts = gameDataCountService.queryDateRangeDataCount(gameChannel, gameServer, rangeDateBegin, rangeDateEnd);
            page.setRecords(gameDayDataCounts).setTotal(gameDayDataCounts.size());
        } else if (type == 2) {
            List<GameStatDataRemain> gameDataRemains = gameDataCountService.queryDataRemainCount(gameChannel, gameServer, rangeDateBegin, rangeDateEnd);
            page.setRecords(gameDataRemains).setTotal(gameDataRemains.size());
        } else {
            List<GameStatLtvCount> gameLtvCounts = gameDataCountService.queryDataLtvCount(gameChannel, gameServer, rangeDateBegin, rangeDateEnd);
            page.setRecords(gameLtvCounts).setTotal(gameLtvCounts.size());
        }
        return Result.ok(page);
    }

    @GetMapping(value = "/ongoing")
    public Result<?> queryGameCountOngoing(@RequestParam(value = "channelId", defaultValue = "0") Integer channelId,
                                           @RequestParam(value = "serverId", defaultValue = "0") Integer serverId,
                                           @RequestParam(value = "type", defaultValue = "0") Integer type,
                                           @RequestParam(value = "rangeDateBegin", defaultValue = "") String rangeDateBegin,
                                           @RequestParam(value = "rangeDateEnd", defaultValue = "") String rangeDateEnd,
                                           @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
                                           @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                                           HttpServletRequest req) {

        Page<GameStatCountOngoing> page = new Page<>(pageNo, pageSize);
        boolean paramValidCheck = ParamValidUtil.isParamInValidCheck(channelId, serverId, rangeDateBegin, rangeDateEnd);
        if (!paramValidCheck && DateUtils.isSameDay(DateUtils.dateOnly(new Date()), DateUtils.parseDate(rangeDateBegin)) && DateUtils.isSameDay(DateUtils.dateOnly(new Date()), DateUtils.parseDate(rangeDateEnd))) {
            // 直接取数据 实时的
            GameServer gameServer = gameServerService.getById(serverId);
            GameChannel gameChannel = gameChannelService.getById(channelId);
            ResponseCode responseCode = ParamValidUtil.dateRangeValid(rangeDateBegin, rangeDateEnd);
            if (!responseCode.isSuccess() || type <= 0) {
                return Result.error(responseCode.getDesc());
            }
            List<GameStatCountOngoing> ongoings = gameDataCountService.queryCountOnGoing(type, gameChannel, gameServer, rangeDateBegin, rangeDateEnd);
            page.setRecords(ongoings).setTotal(ongoings.size());
            return Result.ok(page);
        } else {
            if (paramValidCheck) {
                return Result.ok(page);
            }
            IPage<GameStatCountOngoing> list = gameCountOngoingService.selectList(page, channelId, serverId, type, rangeDateBegin, rangeDateEnd);
            if (StringUtils.isBlank(rangeDateBegin) && StringUtils.isBlank(rangeDateEnd) && CollUtil.isEmpty(list.getRecords())) {
                // 同步
                List<GameStatCountOngoing> countOngoings = gameDataCountService.countOngoings();
                list.setRecords(countOngoings).setTotal(countOngoings.size());
                gameCountOngoingMapper.insertOrUpdateList(countOngoings);
            }
            return Result.ok(list);
        }
    }
}

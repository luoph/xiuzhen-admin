package org.jeecg.modules.game.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.youai.basics.model.ResponseCode;
import cn.youai.server.utils.DateUtils;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.game.entity.*;
import org.jeecg.modules.game.mapper.GameDataRemainMapper;
import org.jeecg.modules.game.service.*;
import org.jeecg.modules.game.util.ParamValidUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
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
@RequestMapping("game/statistics")
public class GameDataCountController {

    @Autowired
    private IGameDataCountService dataCountService;
    @Autowired
    private IGameDataRemainService dataRemainService;
    @Autowired
    private IGameChannelServerService channelServerService;
    @Autowired
    private IGameServerService serverService;
    @Resource
    private GameDataRemainMapper dataRemainMapper;

    @GetMapping(value = "/dayCount")
    public Result<?> queryGameDataCountList(@RequestParam(value = "serverId", defaultValue = "0") Integer serverId,
                                            @RequestParam(value = "rangeDateBegin", defaultValue = "") String rangeDateBegin,
                                            @RequestParam(value = "rangeDateEnd", defaultValue = "") String rangeDateEnd,
                                            @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
                                            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                                            HttpServletRequest req) {
        Page<GameStatDaily> page = new Page<>(pageNo, pageSize);
        if (serverId <= 0) {
            return Result.ok(page);
        }

        Date now = DateUtils.now();
        // 默认查询最近两天的数据
        if (StrUtil.isEmpty(rangeDateBegin)) {
            rangeDateBegin = DateUtil.formatDate(DateUtils.addDays(now, -1));
        }
        if (StrUtil.isEmpty(rangeDateEnd)) {
            rangeDateEnd = DateUtil.formatDate(DateUtils.addDays(now, 1));
        }

        return queryDailyData(serverId, rangeDateBegin, rangeDateEnd, page, 1);
    }

    @GetMapping(value = "/remainRate")
    public Result<?> queryGameRemainCount(@RequestParam(value = "channelId", defaultValue = "0") Integer channelId,
                                          @RequestParam(value = "serverId", defaultValue = "0") Integer serverId,
                                          @RequestParam(value = "rangeDateBegin", defaultValue = "") String rangeDateBegin,
                                          @RequestParam(value = "rangeDateEnd", defaultValue = "") String rangeDateEnd,
                                          @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
                                          @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize, HttpServletRequest req) {
        Page<GameStatRemain> page = new Page<>(pageNo, pageSize);

        boolean paramValidCheck = ParamValidUtil.isParamInValid(channelId, serverId, rangeDateBegin, rangeDateEnd);
        if (!paramValidCheck && DateUtils.isSameDay(DateUtils.dateOnly(new Date()), DateUtils.parseDate(rangeDateBegin)) && DateUtils.isSameDay(DateUtils.dateOnly(new Date()), DateUtils.parseDate(rangeDateEnd))) {
            // 验证通过
            return queryDailyData(serverId, rangeDateBegin, rangeDateEnd, page, 2);
        } else {
            if (paramValidCheck) {
                return Result.ok(page);
            }
            IPage<GameStatRemain> list = dataRemainService.selectList(page, serverId, rangeDateBegin, rangeDateEnd);
            if (StringUtils.isBlank(rangeDateBegin) && StringUtils.isBlank(rangeDateEnd) && CollUtil.isEmpty(list.getRecords())) {
                // 同步
                List<GameChannelServer> channelServers = channelServerService.list();
                channelServers = channelServers.stream().filter(gameChannelServer -> gameChannelServer.getDelFlag() == 0 && gameChannelServer.getNoNeedCount() == 0).collect(Collectors.toList());
                List<GameStatRemain> allCount = new ArrayList<>();
                Map<String, Object> context = new HashMap<>();
                context.put(IGameDataCountService.KEY_GAME_STAT_REMAIN_COUNT_MAP, dataCountService.remainCountMap(false));
                for (GameChannelServer channelServer : channelServers) {
                    GameServer gameServer = serverService.getById(channelServer.getServerId());
                    rangeDateBegin = DateUtils.formatDate(gameServer.getOpenTime(), DatePattern.NORM_DATE_PATTERN);
                    rangeDateEnd = DateUtils.formatDate(DateUtils.addDays(DateUtils.now(), -1), DatePattern.NORM_DATE_PATTERN);
                    List<GameStatRemain> gameLtvCounts = dataCountService.queryDataRemainCount(channelServer.getServerId(), rangeDateBegin, rangeDateEnd, DateUtils.todayDate(), false);
                    allCount.addAll(gameLtvCounts);
                }
                list.setRecords(allCount).setTotal(allCount.size());
                dataRemainMapper.updateOrInsert(allCount);
            }
            return Result.ok(list);
        }

    }

    @GetMapping(value = "/ltvCount")
    public Result<?> queryGameLtvCount(@RequestParam(value = "serverId", defaultValue = "0") Integer serverId,
                                       @RequestParam(value = "rangeDateBegin", defaultValue = "") String rangeDateBegin,
                                       @RequestParam(value = "rangeDateEnd", defaultValue = "") String rangeDateEnd,
                                       @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
                                       @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize, HttpServletRequest req) {

        Page<GameStatLtv> page = new Page<>(pageNo, pageSize);
        if (serverId <= 0) {
            return Result.ok(page);
        }

        Date now = DateUtils.now();
        // 默认查询最近两天的数据
        if (StrUtil.isEmpty(rangeDateBegin)) {
            rangeDateBegin = DateUtil.formatDate(DateUtils.addDays(now, -1));
        }
        if (StrUtil.isEmpty(rangeDateEnd)) {
            rangeDateEnd = DateUtil.formatDate(DateUtils.addDays(now, 1));
        }
        // 验证通过
        return queryDailyData(serverId, rangeDateBegin, rangeDateEnd, page, 3);
    }

    /**
     * @param type 1-daily 2-remain 3-ltv
     */
    private Result<?> queryDailyData(int serverId, String rangeDateBegin, String rangeDateEnd, IPage page, int type) {
        ResponseCode responseCode = ParamValidUtil.dateRangeValid(rangeDateBegin, rangeDateEnd);
        if (!responseCode.isSuccess()) {
            return Result.error(responseCode.getDesc());
        }
        if (type == 1) {
            List<GameStatDaily> gameDayDataCounts = dataCountService.queryDateRangeDataCount(serverId, rangeDateBegin, rangeDateEnd, false);
            page.setRecords(gameDayDataCounts).setTotal(gameDayDataCounts.size());
        } else if (type == 2) {
            List<GameStatRemain> gameDataRemains = dataCountService.queryDataRemainCount(serverId, rangeDateBegin, rangeDateEnd, false);
            page.setRecords(gameDataRemains).setTotal(gameDataRemains.size());
        } else {
            List<GameStatLtv> gameLtvCounts = dataCountService.queryDataLtvCount(serverId, rangeDateBegin, rangeDateEnd, DateUtils.now());
            page.setRecords(gameLtvCounts).setTotal(gameLtvCounts.size());
        }
        return Result.ok(page);
    }

    @GetMapping(value = "/ongoing")
    public Result<?> queryGameCountOngoing(@RequestParam(value = "serverId", defaultValue = "0") Integer serverId,
                                           @RequestParam(value = "type", defaultValue = "0") Integer type,
                                           @RequestParam(value = "rangeDateBegin", defaultValue = "") String rangeDateBegin,
                                           @RequestParam(value = "rangeDateEnd", defaultValue = "") String rangeDateEnd,
                                           @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
                                           @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize, HttpServletRequest req) {

        Page<GameStatOngoing> page = new Page<>(pageNo, pageSize);
        if (serverId <= 0) {
            return Result.ok(page);
        }

        Date now = DateUtils.now();
        // 默认查询最近两天的数据
        if (StrUtil.isEmpty(rangeDateBegin)) {
            rangeDateBegin = DateUtil.formatDate(DateUtils.addDays(now, -1));
        }
        if (StrUtil.isEmpty(rangeDateEnd)) {
            rangeDateEnd = DateUtil.formatDate(DateUtils.addDays(now, 1));
        }

        List<GameStatOngoing> ongoings = dataCountService.queryCountOnGoing(type, serverId, rangeDateBegin, rangeDateEnd);
        page.setRecords(ongoings).setTotal(ongoings.size());
        return Result.ok(page);
    }
}

package org.jeecg.modules.game.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.youai.basics.model.ResponseCode;
import cn.youai.server.utils.DateUtils;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.game.constant.CoreStatisticType;
import org.jeecg.modules.game.entity.GameStatDaily;
import org.jeecg.modules.game.entity.GameStatLtv;
import org.jeecg.modules.game.entity.GameStatOngoing;
import org.jeecg.modules.game.entity.GameStatRemain;
import org.jeecg.modules.game.service.IGameDataCountService;
import org.jeecg.modules.game.util.ParamValidUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
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
@RequestMapping("game/statistics")
public class GameDataCountController {

    @Autowired
    private IGameDataCountService dataCountService;

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
        return Result.ok(page);
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
        if (type == CoreStatisticType.DAILY.getValue()) {
            List<GameStatDaily> gameDayDataCounts = dataCountService.queryDateRangeDataCount(serverId, rangeDateBegin, rangeDateEnd, false);
            page.setRecords(gameDayDataCounts).setTotal(gameDayDataCounts.size());
        } else if (type == CoreStatisticType.REMAIN.getValue()) {
            List<GameStatRemain> gameDataRemains = dataCountService.queryDataRemainCount(serverId, rangeDateBegin, rangeDateEnd, false);
            page.setRecords(gameDataRemains).setTotal(gameDataRemains.size());
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
        return Result.ok(page);
    }
}

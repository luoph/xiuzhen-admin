package org.jeecg.modules.game.controller;

import cn.youai.commons.model.ResponseCode;
import cn.youai.xiuzhen.utils.DateUtils;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.game.entity.GameDayDataCount;
import org.jeecg.modules.game.service.IGameDataCountService;
import org.jeecg.modules.game.service.IGameDayDataCountService;
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
@RequestMapping("game/gameDataCountController")
public class GameDataCountController {

    @Autowired
    private IGameDataCountService gameDataCountService;
    @Autowired
    private IGameDayDataCountService gameDayDataCountService;


    @GetMapping(value = "/list")
    public Result<?> queryGameDataCountList(
            @RequestParam(value = "channelId", defaultValue = "0") Integer channelId,
            @RequestParam(value = "serverId", defaultValue = "0") Integer serverId,
            @RequestParam(value = "rangeDateBegin", defaultValue = "") String rangeDateBegin,
            @RequestParam(value = "rangeDateEnd", defaultValue = "") String rangeDateEnd,
            @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
            HttpServletRequest req) {
        Page<GameDayDataCount> page = new Page<>(pageNo, pageSize);
        boolean paramValidCheck = gameDataCountService.isParamValidCheck(channelId, serverId, rangeDateBegin, rangeDateEnd);
        if (!paramValidCheck && DateUtils.isSameDay(DateUtils.dateOnly(new Date()), DateUtils.parseDate(rangeDateBegin)) && DateUtils.isSameDay(DateUtils.dateOnly(new Date()), DateUtils.parseDate(rangeDateEnd))) {
            // 验证通过
            ResponseCode responseCode = gameDataCountService.dateRangeValid(rangeDateBegin, rangeDateEnd);
            if (!responseCode.isSuccess()) {
                return Result.error(responseCode.getDesc());
            }
            List<GameDayDataCount> gameDataCounts = gameDataCountService.queryDateRangeDataCount(channelId, serverId, rangeDateBegin, rangeDateEnd);
            page.setRecords(gameDataCounts).setTotal(gameDataCounts.size());
            return Result.ok(page);
        } else {
            IPage<GameDayDataCount> selectList = gameDayDataCountService.selectList(page, channelId, serverId, rangeDateBegin, rangeDateEnd);
            return Result.ok(selectList);
        }
    }
}

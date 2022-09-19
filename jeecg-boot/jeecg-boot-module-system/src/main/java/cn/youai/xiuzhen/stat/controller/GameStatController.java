package cn.youai.xiuzhen.stat.controller;

import cn.hutool.core.collection.CollUtil;
import cn.youai.basics.model.DateRange;
import cn.youai.server.utils.DateUtils;
import cn.youai.xiuzhen.stat.entity.GameStatDaily;
import cn.youai.xiuzhen.stat.entity.GameStatOngoing;
import cn.youai.xiuzhen.stat.service.IGameStatDailyService;
import cn.youai.xiuzhen.utils.QueryUtils;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
@RequestMapping("game/stat")
public class GameStatController extends JeecgController<GameStatDaily, IGameStatDailyService> {

//    /**
//     * @param type 1-daily 2-S 3-ltv
//     */
//    private Result<?> queryDailyData(int serverId, String rangeDateBegin, String rangeDateEnd, IPage page, int type) {
//        ResponseCode responseCode = ParamUtils.dateRangeValid(rangeDateBegin, rangeDateEnd);
//        if (!responseCode.isSuccess()) {
//            return Result.error(responseCode.getDesc());
//        }
//        if (type == CoreStatisticType.DAILY.getValue()) {
//            List<GameStatDaily> gameDayDataCounts = service.queryDateRangeDataCount(serverId, rangeDateBegin, rangeDateEnd, false);
//            page.setRecords(gameDayDataCounts).setTotal(gameDayDataCounts.size());
//        } else if (type == CoreStatisticType.REMAIN.getValue()) {
//            List<GameStatRemain> gameDataRemains = service.queryDataRemainCount(serverId, rangeDateBegin, rangeDateEnd, false);
//            page.setRecords(gameDataRemains).setTotal(gameDataRemains.size());
//        }
//        return Result.ok(page);
//    }

    @GetMapping(value = "/ongoing")
    public Result<?> queryGameCountOngoing(
            @RequestParam(value = "serverId", defaultValue = "0") Integer serverId,
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

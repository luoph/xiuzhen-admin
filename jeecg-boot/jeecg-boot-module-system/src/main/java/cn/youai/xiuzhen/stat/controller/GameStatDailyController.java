package cn.youai.xiuzhen.stat.controller;

import cn.hutool.core.collection.CollUtil;
import cn.youai.basics.model.DateRange;
import cn.youai.server.utils.DateUtils;
import cn.youai.xiuzhen.stat.entity.GameStatDaily;
import cn.youai.xiuzhen.stat.service.IGameStatDailyService;
import cn.youai.xiuzhen.utils.PageQueryUtils;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.aspect.annotation.PermissionData;
import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author luopeihuan
 * @date 2022/09/19
 */
@Slf4j
@RestController
@Api(tags = "游戏日常统计")
@RequestMapping("game/stat/daily")
public class GameStatDailyController extends JeecgController<GameStatDaily, IGameStatDailyService> {

    private static final int MAX_DAYS = 60;

    @GetMapping(value = "/list")
    @AutoLog(value = "游戏日常统计-列表")
    @PermissionData(value = "game/GameStatDailyList")
    public Result<?> list(GameStatDaily entity,
                          @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                          @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                          HttpServletRequest req) {
        return queryPageList(entity, pageNo, pageSize, req);
    }

    private List<GameStatDaily> update(GameStatDaily entity, DateRange dateRange, HttpServletRequest req) {
        Date current = dateRange.getEnd();
        List<GameStatDaily> list = new ArrayList<>();
        while (!current.before(dateRange.getStart())) {
            list.add(service.getGameStatDaily(entity.getChannel(), entity.getSdkChannel(), entity.getServerId(), current));
            current = DateUtils.addDays(current, -1);
        }

        if (CollUtil.isNotEmpty(list)) {
            service.updateOrInsert(list);
        }
        return list;
    }

    @AutoLog(value = "游戏日常统计-导出")
    @RequestMapping(value = "/exportXls")
    @PermissionData(value = "game/GameStatDailyList")
    public ModelAndView exportXls(HttpServletRequest request, GameStatDaily entity) {
        return super.exportXls(request, entity, GameStatDaily.class, "日常统计");
    }

    @Override
    @SuppressWarnings("DuplicatedCode")
    public Result<?> queryPageList(GameStatDaily entity, Integer pageNo, Integer pageSize, HttpServletRequest req) {
        // 刷新统计数据
        Date endDate = DateUtils.todayDate();
        Date startDate = DateUtils.addDays(endDate, -2);
        DateRange dateRange = PageQueryUtils.parseRange(req.getParameterMap(), "countDate", startDate, endDate);
        int days = DateUtils.daysBetween(dateRange.getStart(), dateRange.getEnd());
        if (days > MAX_DAYS) {
            return Result.error("不支持超过" + MAX_DAYS + "天的数据查询");
        }
        List<GameStatDaily> records = update(entity, dateRange, req);
        return Result.ok(PageQueryUtils.makePage(records));
    }
}

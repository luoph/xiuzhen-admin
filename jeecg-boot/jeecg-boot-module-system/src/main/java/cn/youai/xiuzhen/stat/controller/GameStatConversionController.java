package cn.youai.xiuzhen.stat.controller;

import cn.hutool.core.collection.CollUtil;
import cn.youai.basics.model.DateRange;
import cn.youai.server.utils.DateUtils;
import cn.youai.xiuzhen.stat.entity.GameStatConversion;
import cn.youai.xiuzhen.stat.service.IGameStatConversionService;
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
@Api(tags = "新增转化统计")
@RequestMapping("game/stat/conversion")
public class GameStatConversionController extends JeecgController<GameStatConversion, IGameStatConversionService> {
    private static final int MAX_DAYS = 60;

    @GetMapping(value = "/list")
    @AutoLog(value = "新增转化统计-列表")
    @PermissionData(value = "game/GameStatConversionList")
    public Result<?> list(GameStatConversion entity,
                          @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                          @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                          HttpServletRequest req) {
        return queryPageList(entity, pageNo, pageSize, req);
    }

    public List<GameStatConversion> update(GameStatConversion entity, DateRange dateRange, HttpServletRequest req) {
        Date current = dateRange.getStart();
        List<GameStatConversion> list = new ArrayList<>();
        while (!current.after(dateRange.getEnd())) {
            list.add(service.getGameStatConversion(entity.getChannel(), entity.getSdkChannel(), entity.getServerId(), current));
            current = DateUtils.addDays(current, 1);
        }

        if (CollUtil.isNotEmpty(list)) {
            service.updateOrInsert(list);
        }
        return list;
    }

    @AutoLog(value = "新增转化统计-导出")
    @RequestMapping(value = "/exportXls")
    @PermissionData(value = "game/GameStatConversionList")
    public ModelAndView exportXls(HttpServletRequest request, GameStatConversion entity) {
        return super.exportXls(request, entity, GameStatConversion.class, "新增转化统计");
    }

    @Override
    @SuppressWarnings("DuplicatedCode")
    public Result<?> queryPageList(GameStatConversion entity, Integer pageNo, Integer pageSize, HttpServletRequest req) {
        // 刷新统计数据
        Date endDate = DateUtils.todayDate();
        Date startDate = DateUtils.addDays(endDate, -2);
        DateRange dateRange = PageQueryUtils.parseRange(req.getParameterMap(), "countDate", startDate, endDate);
        int days = DateUtils.daysBetween(dateRange.getStart(), dateRange.getEnd());
        if (days > MAX_DAYS) {
            return Result.error("不支持超过" + MAX_DAYS + "天的数据查询");
        }

        List<GameStatConversion> records = update(entity, dateRange, req);
        return Result.ok(PageQueryUtils.makePage(records));
    }
}

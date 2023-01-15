package cn.youai.xiuzhen.stat.controller;

import cn.hutool.core.collection.CollUtil;
import cn.youai.basics.model.DateRange;
import cn.youai.server.utils.DateUtils;
import cn.youai.xiuzhen.stat.constant.StatisticType;
import cn.youai.xiuzhen.stat.entity.GameStatDaily;
import cn.youai.xiuzhen.stat.service.IGameStatDailyService;
import cn.youai.xiuzhen.utils.PageQueryUtils;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
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

    @GetMapping(value = "/list")
    public Result<?> list(GameStatDaily entity,
                          @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                          @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                          HttpServletRequest req) {
        if (entity.getChannel() == null) {
            entity.setChannel(StatisticType.DEFAULT_CHANNEL);
        }

        if (entity.getServerId() == null) {
            entity.setServerId(StatisticType.DEFAULT_SERVER_ID);
        }
        IPage<GameStatDaily> pageList = pageList(entity, pageNo, pageSize, req);
        return Result.ok(pageList);
    }

    @AutoLog(value = "游戏日常统计-更新")
    @GetMapping(value = "/update")
    public Result<?> update(GameStatDaily entity, HttpServletRequest req) {
        // 刷新统计数据
        Date endDate = DateUtils.todayDate();
        Date startDate = DateUtils.addDays(endDate, -2);
        DateRange dateRange = PageQueryUtils.parseRange(req.getParameterMap(), "countDate", startDate, endDate);
        Date current = dateRange.getStart();

        List<GameStatDaily> list = new ArrayList<>();
        while (!current.after(dateRange.getEnd())) {
            list.add(service.getGameStatDaily(entity.getChannel(), entity.getServerId(), current));
            current = DateUtils.addDays(current, 1);
        }

        if (CollUtil.isNotEmpty(list)) {
            service.updateOrInsert(list);
        }
        return Result.ok("更新成功");
    }

    @AutoLog(value = "游戏日常统计-导出")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, GameStatDaily entity) {
        return super.exportXls(request, entity, GameStatDaily.class, "日常统计");
    }
}

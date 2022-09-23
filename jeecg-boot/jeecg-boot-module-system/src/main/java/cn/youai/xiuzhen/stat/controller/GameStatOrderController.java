package cn.youai.xiuzhen.stat.controller;

import cn.hutool.core.collection.CollUtil;
import cn.youai.basics.model.DateRange;
import cn.youai.server.utils.DateUtils;
import cn.youai.xiuzhen.game.constant.StatDayType;
import cn.youai.xiuzhen.game.constant.StatDuration;
import cn.youai.xiuzhen.game.entity.GameServerVO;
import cn.youai.xiuzhen.game.service.IGameChannelServerService;
import cn.youai.xiuzhen.stat.entity.GameStatOrder;
import cn.youai.xiuzhen.stat.service.IGameOrderStatService;
import cn.youai.xiuzhen.stat.service.ILogAccountService;
import cn.youai.xiuzhen.utils.PageQueryUtils;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.util.ExcelUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description game_stat_order
 * @date 2021-04-20
 */
@Slf4j
@RestController
@RequestMapping("game/stat/order")
public class GameStatOrderController {

    @Autowired
    private ILogAccountService logAccountService;

    @Autowired
    private IGameOrderStatService orderStatService;

    @Autowired
    private IGameChannelServerService channelServerService;

    /**
     * 分页列表查询
     *
     * @param entity   数据实体
     * @param pageNo   页码
     * @param pageSize 分页大小
     * @return {@linkplain Result}
     */
    @AutoLog(value = "充值统计-列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(GameStatOrder entity,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        Page<GameStatOrder> page = new Page<>(pageNo, pageSize);
        if (StringUtils.isEmpty(entity.getChannel())
                && (entity.getServerId() == null || entity.getServerId() < 0)) {
            return Result.ok(page);
        }

        // 如果指定 游戏服id，则清除渠道信息
        if (entity.getServerId() != null && entity.getServerId() > 0) {
            entity.setChannel(null);
        }

        IPage<GameStatOrder> pageList = pageList(page, entity, req);
        return Result.ok(pageList);
    }


    @AutoLog(value = "充值统计-导出")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest req, GameStatOrder entity) {
        Page<GameStatOrder> page = new Page<>(1, Integer.MAX_VALUE);
        IPage<GameStatOrder> pageList = pageList(page, entity, req);
        return ExcelUtils.exportXls(pageList, req.getParameter("selections"), GameStatOrder.class, "充值统计");
    }

    private IPage<GameStatOrder> pageList(Page<GameStatOrder> page, GameStatOrder entity, HttpServletRequest req) {
        // 刷新统计数据
        Date todayDate = DateUtils.todayDate();
        DateRange dateRange = PageQueryUtils.parseRange(req.getParameterMap(), "countDate");
        if (entity.getDayType() == null && dateRange.getStart() == null) {
            entity.setDayType(StatDayType.D7.getValue());
        }

        StatDayType dayType = StatDayType.DEFAULT;
        if (entity.getDayType() != null) {
            dayType = StatDayType.valueOf(entity.getDayType());
        }

        int days = dayType.getValue();
        if (days > 0) {
            dateRange = new DateRange(DateUtils.addDays(todayDate, -(days - 1)), todayDate);
        }

        // 按照年、月分组
        Map<String, List<Date>> monthsMap = new HashMap<>();
        Map<String, List<Date>> yearMap = new HashMap<>();

        Date current = dateRange.getStart();
        List<GameStatOrder> records = CollUtil.newArrayList();
        while (!current.after(dateRange.getEnd())) {
            groupDate(monthsMap, StatDuration.MONTH.getTimeFormat(), current);
            groupDate(yearMap, StatDuration.YEAR.getTimeFormat(), current);
            GameStatOrder statOrder = queryGameStatOrder(StatDuration.DAY, entity.getChannel(), current, current);
            records.add(statOrder);

            current = DateUtils.addDays(current, 1);
        }
        records.sort(Comparator.comparing(GameStatOrder::getStarDate, Comparator.reverseOrder()));

        // 增加按月、按年汇总
        records.addAll(queryGroupMap(StatDuration.MONTH, entity.getChannel(), monthsMap));
        records.addAll(queryGroupMap(StatDuration.YEAR, entity.getChannel(), yearMap));

        // 格式化统计标识
        records.forEach(t -> t.setStatTime(DateUtils.formatDate(t.getStarDate(), t.getDuration().getViewFormat())));
        return PageQueryUtils.makePage(records);
    }

    private void groupDate(Map<String, List<Date>> map, String pattern, Date current) {
        String key = DateUtils.formatDate(current, pattern);
        List<Date> dateList = map.computeIfAbsent(key, k -> new ArrayList<>());
        dateList.add(current);
    }

    private GameStatOrder queryGameStatOrder(StatDuration duration, String channel, Date start, Date end) {
        List<Integer> serverIds = logAccountService.selectRunningServerIdsByRange(start, end);
        List<GameServerVO> serverList = channelServerService.filterServerList(channel, serverIds);
        if (CollUtil.isEmpty(serverList)) {
            return GameStatOrder.zero(duration, start, end);
        } else {
            Set<Integer> idSet = serverList.stream().map(GameServerVO::getId).collect(Collectors.toSet());
            GameStatOrder statOrder = orderStatService.queryOrderStatByRange(new ArrayList<>(idSet), start, end).calc();
            statOrder.setDuration(duration).setStarDate(start).setEndDate(end);
            return statOrder;
        }
    }

    private GameStatOrder queryGroupGameStateOrder(StatDuration duration, String channel, List<Date> list) {
        list.sort(Comparator.comparing(Date::getTime));
        Date start = list.get(0);
        Date end = list.get(list.size() - 1);
        return queryGameStatOrder(duration, channel, start, end);
    }

    private List<GameStatOrder> queryGroupMap(StatDuration duration, String channel, Map<String, List<Date>> map) {
        List<GameStatOrder> list = new ArrayList<>();
        for (String s : map.keySet()) {
            List<Date> dateList = map.get(s);
            GameStatOrder statOrder = queryGroupGameStateOrder(duration, channel, dateList);
            list.add(statOrder);
        }
        return list;
    }

}

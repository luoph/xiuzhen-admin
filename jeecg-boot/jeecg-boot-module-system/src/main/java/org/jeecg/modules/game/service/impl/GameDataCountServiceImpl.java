package org.jeecg.modules.game.service.impl;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.youai.server.utils.DateUtils;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.game.constant.CoreStatisticType;
import org.jeecg.modules.game.constant.RoleType;
import org.jeecg.modules.game.entity.GameChannelServer;
import org.jeecg.modules.game.entity.GameStatDaily;
import org.jeecg.modules.game.entity.GameStatRemain;
import org.jeecg.modules.game.service.*;
import org.jeecg.modules.game.util.ParamValidUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.jeecg.modules.game.constant.CoreStatisticType.DAILY;

/**
 * @ClassName IGameDataCountServiceImpl
 * @Description 数据统计业务实现
 * @Author buliangliang
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2020-08-21 11:07
 */
@Service
@Slf4j
public class GameDataCountServiceImpl implements IGameDataCountService {

    @Autowired
    private IGameChannelServerService gameChannelServerService;
    @Autowired
    private IGameDataRemainService gameDataRemainService;

    @Autowired
    private IGameDayDataCountService dayDataCountService;

    @Autowired
    private IGameStatLtvService statLtvService;
    @Autowired
    private IGameStatRemainService statRemainService;
    @Autowired
    private IGameStatRemainDetailService statRemainDetailService;
    @Autowired
    private IGameStatLtvDetailService statLtvDetailService;

    @Override
    public List<GameStatDaily> queryDateRangeDataCount(int serverId, String rangeDateBegin, String rangeDateEnd, boolean isOpenDateCount) {
        Date dateBegin = DateUtils.parseDate(rangeDateBegin);
        Date dateEnd = DateUtils.parseDate(rangeDateEnd);
        // 数组第一个元素为开始统计的第一个日期
        int dateRangeBetween = ParamValidUtil.dateRangeBetween(dateBegin, dateEnd);
        List<GameStatDaily> list = new ArrayList<>(dateRangeBetween);
        for (int i = 0; i <= dateRangeBetween; i++) {
            String dateOnly = DateUtil.formatDate(DateUtils.addDays(dateBegin, i));
            GameStatDaily gameDataCount = dayDataCountService.gameDataCount(serverId, dateOnly);
            list.add(gameDataCount);
        }
        return list;
    }

    private String dailyCountKey(int serverId, String countDate) {
        return serverId + "_" + countDate;
    }

    @Override
    public void doJobDataCount() {
        doJobDataCount(DateUtils.todayDate(), DAILY);
        doJobDataCount(DateUtils.todayDate(), CoreStatisticType.REMAIN);
        doJobDataCount(DateUtils.todayDate(), CoreStatisticType.LTV);
    }

    @Override
    public void doJobDataCount(Date registerDate, CoreStatisticType type) {
        Wrapper<GameChannelServer> query = Wrappers.<GameChannelServer>lambdaQuery().eq(GameChannelServer::getDelFlag, 0).eq(GameChannelServer::getNoNeedCount, 0);
        List<GameChannelServer> list = gameChannelServerService.list(query);
        Map<Integer, GameChannelServer> serverMap = list.stream().collect(Collectors.toMap(GameChannelServer::getServerId, Function.identity(), (key1, key2) -> key2));

        int days = DateUtils.daysBetweenNatural(registerDate, DateUtils.now());
        if (days < 0) {
            return;
        }

        Date date = DateUtils.addDays(registerDate, -1);
        switch (type) {
            case DAILY:
                dayDataCountService.doJobDataCountToDaily(serverMap.keySet(), date);
                break;

            case REMAIN:
                statRemainService.doJobDataCountToRemain(RoleType.ALL, serverMap.keySet(), registerDate, days + 1, true);
                statRemainService.doJobDataCountToRemain(RoleType.PAID, serverMap.keySet(), registerDate, days + 1, true);
                break;

            case LTV:
                statLtvService.doJobDataCountToLtv(serverMap.keySet(), registerDate, days + 1, true);
                break;

            case REMAIN_DETAIL:
                statRemainDetailService.calcRemainDetailStat(RoleType.ALL, serverMap.keySet(), registerDate, days + 1, true);
                statRemainDetailService.calcRemainDetailStat(RoleType.PAID, serverMap.keySet(), registerDate, days + 1, true);
                break;

            default:
                break;
        }
    }

    @Override
    public List<GameStatRemain> queryDataRemainCount(int serverId, String rangeDateBegin, String rangeDateEnd, boolean isOpenDateCount) {
        return queryDataRemainCount(serverId, rangeDateBegin, rangeDateEnd, DateUtils.now(), isOpenDateCount);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<GameStatRemain> queryDataRemainCount(int serverId, String rangeDateBegin, String rangeDateEnd, Date statDate, boolean isOpenDateCount) {
        Date dateBegin = DateUtils.parseDate(rangeDateBegin);
        Date dateEnd = DateUtils.parseDate(rangeDateEnd);
        // 数组第一个元素为开始统计的第一个日期
        Date[] dates = ParamValidUtil.dateBegin(dateBegin, dateEnd);
        int dateRangeBetween = ParamValidUtil.dateRangeBetween(dateBegin, dateEnd);
        List<GameStatRemain> list = new ArrayList<>(dateRangeBetween);
        for (int i = 0; i <= dateRangeBetween; i++) {
            String dateOnly = DateUtils.formatDate(DateUtils.addDays(dates[0], i), DatePattern.NORM_DATE_PATTERN);
            GameStatRemain gameDataRemain = gameDataRemainService.getCountRemain(serverId, dateOnly, statDate);
            list.add(gameDataRemain);
        }
        return list;
    }

    @Override
    public Map<String, GameStatRemain> remainCountMap(boolean isReturnIndex) {
        List<GameStatRemain> dataCounts;
        if (isReturnIndex) {
            LambdaQueryWrapper<GameStatRemain> queryWrapper = Wrappers.lambdaQuery();
            queryWrapper.select(GameStatRemain::getChannel, GameStatRemain::getServerId, GameStatRemain::getCountDate);
            dataCounts = gameDataRemainService.list(queryWrapper);
        } else {
            dataCounts = gameDataRemainService.list();
        }
        Map<String, GameStatRemain> map = new HashMap<>(dataCounts.size());
        for (GameStatRemain remainCount : dataCounts) {
            String formatDate = DateUtil.formatDate(remainCount.getCountDate());
            String countKey = dailyCountKey(remainCount.getServerId(), formatDate);
            map.put(countKey, remainCount);
        }
        return map;
    }

    @Override
    public void doJobDataCountUpdateByType(CoreStatisticType type, Date current) {
        // TODO 处理ltv、留存每日更新任务
    }
}

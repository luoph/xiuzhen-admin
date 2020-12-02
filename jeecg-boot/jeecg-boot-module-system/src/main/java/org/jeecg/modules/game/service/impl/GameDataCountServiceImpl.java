package org.jeecg.modules.game.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DatePattern;
import cn.youai.xiuzhen.utils.BigDecimalUtil;
import cn.youai.xiuzhen.utils.DateUtils;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.game.entity.*;
import org.jeecg.modules.game.mapper.GameCountOngoingMapper;
import org.jeecg.modules.game.mapper.GameDataRemainMapper;
import org.jeecg.modules.game.mapper.GameDayDataCountMapper;
import org.jeecg.modules.game.mapper.GameLtvCountMapper;
import org.jeecg.modules.game.service.*;
import org.jeecg.modules.game.util.ParamValidUtil;
import org.jeecg.modules.game.util.ReflectUtils;
import org.jeecg.modules.player.service.ILogAccountService;
import org.jeecg.modules.player.service.IPayOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

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
    private IGameChannelService gameChannelService;
    @Autowired
    private IGameServerService gameServerService;
    @Autowired
    private IGameChannelServerService gameChannelServerService;
    @Autowired
    private IPayOrderService payOrderService;
    @Autowired
    private ILogAccountService logAccountService;
    @Autowired
    private IGameDataRemainService gameDataRemainService;
    @Autowired
    private IGameLtvCountService gameLtvCountService;
    @Resource
    private GameDayDataCountMapper gameDayDataCountMapper;
    @Resource
    private GameLtvCountMapper gameLtvCountMapper;
    @Resource
    private GameDataRemainMapper gameDataRemainMapper;
    @Autowired
    private IGameCountOngoingService gameCountOngoingService;
    @Resource
    private GameCountOngoingMapper gameCountOngoingMapper;
    @Autowired
    private IGameDayDataCountService gameDayDataCountService;

    @Value("${app.log.db.table}")
    private String logTable;

    /**
     * 统计时长30天
     */
    private static final int DAYS_BETWEEN = 30;

    /**
     * 连续统计字段前缀
     */
    private static final String FIELD = "c";

    /**
     * 留存间隔查询
     * 统计间隔+1 = 统计天数
     */
    private static final int[] REMAIN = new int[]{1, 2, 3, 4, 5, 6, 14, 29, 59, 89, 119, 179, 359};

    /**
     * ltv统计间隔
     * 统计间隔 = 统计天数
     */
    private static final int[] LTV = new int[]{1, 2, 3, 4, 5, 6, 7, 14, 21, 30, 60, 90, 120, 180, 360};

    @Override
    public List<GameStatDaily> queryDateRangeDataCount(GameChannel gameChannel, GameServer gameServer, String rangeDateBegin, String rangeDateEnd) {
        Map<String, GameStatDaily> map = dailyCountMap();
        List<GameStatDaily> list = new ArrayList<>();
        Date dateBegin = DateUtils.parseDate(rangeDateBegin);
        Date dateEnd = DateUtils.parseDate(rangeDateEnd);
        // 数组第一个元素为开始统计的第一个日期
        Date[] dates = ParamValidUtil.dateBegin(dateBegin, dateEnd);
        int dateRangeBetween = ParamValidUtil.dateRangeBetween(dateBegin, dateEnd);
        for (int i = 0; i <= dateRangeBetween; i++) {
            String dateOnly = DateUtils.formatDate(DateUtils.addDays(dates[0], i), DatePattern.NORM_DATE_PATTERN);
            String dailyCountKey = dailyCountKey(gameChannel.getSimpleName(), gameServer.getId(), dateOnly);
            GameStatDaily gameDayDataCount = map.get(dailyCountKey);
            if (gameDayDataCount != null) {
                continue;
            }
            GameStatDaily gameDataCount = gameDataCount(gameChannel, gameServer, dateOnly);
            list.add(gameDataCount);
            map.put(dailyCountKey, gameDataCount);
        }
        return list;
    }

    private Map<String, GameStatDaily> dailyCountMap() {
        List<GameStatDaily> dataCounts = gameDayDataCountService.list();
        Map<String, GameStatDaily> map = new HashMap<>(dataCounts.size());
        for (GameStatDaily dataCount : dataCounts) {
            String formatDate = DateUtils.formatDate(dataCount.getCountDate(), DatePattern.NORM_DATE_PATTERN);
            String countKey = dailyCountKey(dataCount.getChannel(), dataCount.getServerId(), formatDate);
            map.put(countKey, dataCount);
        }
        return map;
    }


    private String dailyCountKey(String channel, int serverId, String countDate) {
        return channel + "_" + serverId + "_" + countDate;
    }


    @Override
    public GameStatDaily gameDataCount(GameChannel gameChannel, GameServer gameServer, String date) {

        //当天付费总金额
        double sumPayAmount = payOrderService.sumPayAmount(gameChannel.getSimpleName(), gameServer.getId(), date);
        // 支付玩家数
        int countPay = payOrderService.countPayPlayer(gameChannel.getSimpleName(), gameServer.getId(), date);
        // 当天登录角色数
        int loginNum = logAccountService.loginRegisterPlayer(gameChannel.getSimpleName(), gameServer.getId(), date, 2);
        // 当天注册角色数
        int registerPlayer = logAccountService.loginRegisterPlayer(gameChannel.getSimpleName(), gameServer.getId(), date, 1);
        // 注册付费总金额
        double registerPayAmount = logAccountService.registerPayAmount(gameChannel.getSimpleName(), gameServer.getId(), date);
        // 注册付费玩家
        int registerPayPlayer = logAccountService.registerPayPlayer(gameChannel.getSimpleName(), gameServer.getId(), date);
        // 注册二次付费玩家
        int doublePayPlayer = logAccountService.doublePayRegisterPlayer(gameChannel.getSimpleName(), gameServer.getId(), date);


        return new GameStatDaily().setPayAmount(BigDecimalUtil.valueOf(sumPayAmount)).setLoginNum(loginNum)
                .setPayNum(countPay).setArpu(BigDecimalUtil.divideZero(sumPayAmount, loginNum, false))
                .setArppu(BigDecimalUtil.divideZero(sumPayAmount, countPay, false))
                .setPayRate(BigDecimalUtil.divideZero(countPay, loginNum, true))
                .setAddNum(registerPlayer).setAddPayNum(registerPayPlayer)
                .setAddPayAmount(BigDecimalUtil.valueOf(registerPayAmount))
                .setAddPayRate(BigDecimalUtil.divideZero(registerPayPlayer, registerPlayer, true))
                .setDoublePay(doublePayPlayer)
                .setDoublePayRate(BigDecimalUtil.divideZero(doublePayPlayer, registerPayPlayer, true))
                .setAddArpu(BigDecimalUtil.divideZero(registerPayAmount, registerPlayer, false))
                .setAddArppu(BigDecimalUtil.divideZero(registerPayAmount, registerPayPlayer, false))
                .setChannel(gameChannel.getSimpleName()).setServerId(gameServer.getId())
                .setCountDate(DateUtils.parseDate(date)).setCreateTime(DateUtils.now());
    }

    @Override
    public void doJobDataCount() {
        List<GameChannelServer> list = gameChannelServerService.list();
        list = list.stream().filter(gameChannelServer -> gameChannelServer.getDelFlag() == 0 && gameChannelServer.getNoNeedCount() == 0).collect(Collectors.toList());
        Date date = DateUtils.addDays(DateUtils.todayDate(), -1);
        String formatDate = DateUtils.formatDate(date, DatePattern.NORM_DATE_PATTERN);
        for (GameChannelServer gameChannelServer : list) {
            GameServer gameServer = gameServerService.getById(gameChannelServer.getServerId());
            GameChannel gameChannel = gameChannelService.getById(gameChannelServer.getChannelId());
            String f = DateUtils.formatDate(gameServer.getOpenTime(), DatePattern.NORM_DATETIME_PATTERN);
            List<GameStatDaily> gameDayDataCounts = queryDateRangeDataCount(gameChannel, gameServer, f, formatDate);
            if (CollUtil.isNotEmpty(gameDayDataCounts)) {
                gameDayDataCountMapper.updateOrInsert(gameDayDataCounts);
            }

            List<GameStatRemain> gameDataRemains = queryDataRemainCount(gameChannel, gameServer, f, formatDate);
            if (CollUtil.isNotEmpty(gameDataRemains)) {
                gameDataRemainMapper.updateOrInsert(gameDataRemains);
            }

            List<GameStatLtv> gameLtvCounts = queryDataLtvCount(gameChannel, gameServer, f, formatDate);
            if (CollUtil.isNotEmpty(gameLtvCounts)) {
                gameLtvCountMapper.updateOrInsert(gameLtvCounts);
            }
        }
        List<GameStatOngoing> gameCountOngoings = countOngoings();
        gameCountOngoingMapper.insertOrUpdateList(gameCountOngoings);
    }

    @Override
    public void doJobDataCountUpdate() {
        List<GameChannelServer> list = gameChannelServerService.list();
        list = list.stream().filter(gameChannelServer -> gameChannelServer.getDelFlag() == 0 && gameChannelServer.getNoNeedCount() == 0).collect(Collectors.toList());
        Date date = DateUtils.addDays(DateUtils.todayDate(), -1);
        String formatDate = DateUtils.formatDate(date, DatePattern.NORM_DATE_PATTERN);
        for (GameChannelServer gameChannelServer : list) {
            GameServer gameServer = gameServerService.getById(gameChannelServer.getServerId());
            GameChannel gameChannel = gameChannelService.getById(gameChannelServer.getChannelId());
            // 留存更新
            updateRemainTask(gameChannel, gameServer, formatDate);
            // ltv更新
            updateLtvTask(gameChannel, gameServer, formatDate);
        }
    }


    @Override
    public List<GameStatRemain> queryDataRemainCount(GameChannel gameChannel, GameServer gameServer, String rangeDateBegin, String rangeDateEnd) {
        Map<String, GameStatRemain> map = remainCountMap();
        List<GameStatRemain> list = new ArrayList<>();
        Date dateBegin = DateUtils.parseDate(rangeDateBegin);
        Date dateEnd = DateUtils.parseDate(rangeDateEnd);
        // 数组第一个元素为开始统计的第一个日期
        Date[] dates = ParamValidUtil.dateBegin(dateBegin, dateEnd);
        int dateRangeBetween = ParamValidUtil.dateRangeBetween(dateBegin, dateEnd);
        for (int i = 0; i <= dateRangeBetween; i++) {
            String dateOnly = DateUtils.formatDate(DateUtils.addDays(dates[0], i), DatePattern.NORM_DATE_PATTERN);
            String remainCountKey = dailyCountKey(gameChannel.getSimpleName(), gameServer.getId(), dateOnly);
            GameStatRemain dataRemain = map.get(remainCountKey);
            if (dataRemain != null) {
                continue;
            }
            GameStatRemain gameDataRemain = gameDataRemainService.getCountRemain(gameChannel.getSimpleName(), gameServer.getId(), dateOnly);
            list.add(gameDataRemain);
            map.put(remainCountKey, gameDataRemain);
        }
        return list;
    }

    private Map<String, GameStatRemain> remainCountMap() {
        List<GameStatRemain> dataCounts = gameDataRemainService.list();
        Map<String, GameStatRemain> map = new HashMap<>(dataCounts.size());
        for (GameStatRemain remainCount : dataCounts) {
            String formatDate = DateUtils.formatDate(remainCount.getCountDate(), DatePattern.NORM_DATE_PATTERN);
            String countKey = dailyCountKey(remainCount.getChannel(), remainCount.getServerId(), formatDate);
            map.put(countKey, remainCount);
        }
        return map;
    }


    @Override
    public List<GameStatLtv> queryDataLtvCount(GameChannel gameChannel, GameServer gameServer, String rangeDateBegin, String rangeDateEnd) {
        Map<String, GameStatLtv> map = ltvCountMap();
        List<GameStatLtv> list = new ArrayList<>();
        Date dateBegin = DateUtils.parseDate(rangeDateBegin);
        Date dateEnd = DateUtils.parseDate(rangeDateEnd);
        // 数组第一个元素为开始统计的第一个日期
        Date[] dates = ParamValidUtil.dateBegin(dateBegin, dateEnd);
        int dateRangeBetween = ParamValidUtil.dateRangeBetween(dateBegin, dateEnd);
        for (int i = 0; i <= dateRangeBetween; i++) {
            String dateOnly = DateUtils.formatDate(DateUtils.addDays(dates[0], i), DatePattern.NORM_DATE_PATTERN);
            String ltvCountKey = dailyCountKey(gameChannel.getSimpleName(), gameServer.getId(), dateOnly);
            GameStatLtv ltvCount = map.get(ltvCountKey);
            if (ltvCount != null) {
                continue;
            }
            GameStatLtv gameLtvCount = gameLtvCountService.getGameLtvCount(gameChannel.getSimpleName(), gameServer.getId(), dateOnly, logTable);
            list.add(gameLtvCount);
            map.put(ltvCountKey, gameLtvCount);
        }
        return list;
    }

    private Map<String, GameStatLtv> ltvCountMap() {
        List<GameStatLtv> dataCounts = gameLtvCountService.list();
        Map<String, GameStatLtv> map = new HashMap<>(dataCounts.size());
        for (GameStatLtv ltvCount : dataCounts) {
            String formatDate = DateUtils.formatDate(ltvCount.getCountDate(), DatePattern.NORM_DATE_PATTERN);
            String countKey = dailyCountKey(ltvCount.getChannel(), ltvCount.getServerId(), formatDate);
            map.put(countKey, ltvCount);
        }
        return map;
    }


    private int betweenNatural(GameServer gameServer, String countDate) {
        Date openTime = gameServer.getOpenTime();
        Date date = DateUtils.parseDate(countDate);
        if (openTime.after(date)) {
            return 0;
        }
        int betweenNatural = DateUtils.daysBetweenNatural(openTime, date);
        return Math.max(betweenNatural, 0);
    }


    @Override
    public void updateRemainTask(GameChannel gameChannel, GameServer gameServer, String countDate) {
        Map<String, GameStatRemain> map = remainCountMap();
        int betweenNatural = betweenNatural(gameServer, countDate);
        for (int i = 0; i <= betweenNatural; i++) {
            Date nextDate = DateUtils.addDays(gameServer.getOpenTime(), i);
            int leftDays = DateUtils.daysBetweenNatural(nextDate, DateUtils.parseDate(countDate));
            String formatDate = DateUtils.formatDate(nextDate, DatePattern.NORM_DATE_PATTERN);
            String remainCountKey = dailyCountKey(gameChannel.getSimpleName(), gameServer.getId(), formatDate);
            GameStatRemain gameRemainCount = map.get(remainCountKey);
            if (gameRemainCount == null || gameRemainCount.getD360Remain() != null) {
                continue;
            }

            for (int j = 1; j <= leftDays; j++) {
                int remain = gameDataRemainMapper.selectRemain(gameChannel.getSimpleName(), gameServer.getId(), DateUtils.formatDateTimeStr(nextDate), logTable, j);
                updateRemainCountField(gameRemainCount, j, remain);
            }
            map.put(remainCountKey, gameRemainCount);
        }
        List<GameStatRemain> remains = Lists.newArrayList(map.values());
        gameDataRemainService.updateBatchById(remains);
    }

    private void updateRemainCountField(GameStatRemain gameDataRemain, int j, int remain) {
        if (gameDataRemain != null) {
            if (j > 0 && j <= REMAIN[0]) {
                gameDataRemain.setD2Remain(BigDecimal.valueOf(remain));
            } else if (j > REMAIN[0] && j <= REMAIN[1]) {
                gameDataRemain.setD3Remain(BigDecimal.valueOf(remain));
            } else if (j > REMAIN[1] && j <= REMAIN[2]) {
                gameDataRemain.setD4Remain(BigDecimal.valueOf(remain));
            } else if (j > REMAIN[2] && j <= REMAIN[3]) {
                gameDataRemain.setD5Remain(BigDecimal.valueOf(remain));
            } else if (j > REMAIN[3] && j <= REMAIN[4]) {
                gameDataRemain.setD6Remain(BigDecimal.valueOf(remain));
            } else if (j > REMAIN[4] && j <= REMAIN[5]) {
                gameDataRemain.setD7Remain(BigDecimal.valueOf(remain));
            } else if (j > REMAIN[5] && j <= REMAIN[6]) {
                gameDataRemain.setD15Remain(BigDecimal.valueOf(remain));
            } else if (j > REMAIN[6] && j <= REMAIN[7]) {
                gameDataRemain.setD30Remain(BigDecimal.valueOf(remain));
            } else if (j > REMAIN[7] && j <= REMAIN[8]) {
                gameDataRemain.setD60Remain(BigDecimal.valueOf(remain));
            } else if (j > REMAIN[8] && j <= REMAIN[9]) {
                gameDataRemain.setD90Remain(BigDecimal.valueOf(remain));
            } else if (j > REMAIN[9] && j <= REMAIN[10]) {
                gameDataRemain.setD120Remain(BigDecimal.valueOf(remain));
            } else if (j > REMAIN[10] && j <= REMAIN[11]) {
                gameDataRemain.setD120Remain(BigDecimal.valueOf(remain));
            } else if (j > REMAIN[11] && j <= REMAIN[12]) {
                gameDataRemain.setD120Remain(BigDecimal.valueOf(remain));
            }
        }
    }

    @Override
    public void updateLtvTask(GameChannel gameChannel, GameServer gameServer, String countDate) {
        Map<String, GameStatLtv> map = ltvCountMap();
        int betweenNatural = betweenNatural(gameServer, countDate);
        for (int i = 0; i <= betweenNatural; i++) {
            Date nextDate = DateUtils.addDays(gameServer.getOpenTime(), i);
            int leftDays = DateUtils.daysBetweenNatural(nextDate, DateUtils.parseDate(countDate));
            String formatDate = DateUtils.formatDate(nextDate, DatePattern.NORM_DATE_PATTERN);
            String ltvCountKey = dailyCountKey(gameChannel.getSimpleName(), gameServer.getId(), formatDate);
            GameStatLtv gameLtvCount = map.get(ltvCountKey);
            if (gameLtvCount == null || gameLtvCount.getD360Amount() != null) {
                continue;
            }

            for (int j = 1; j <= leftDays; j++) {
                double remain = gameLtvCountMapper.selectLtv(gameChannel.getSimpleName(), gameServer.getId(), DateUtils.formatDateTimeStr(nextDate), j);
                updateLtvCountField(gameLtvCount, j, remain);
            }
            map.put(ltvCountKey, gameLtvCount);
        }
        // 批量更新
        List<GameStatLtv> ltvCounts = Lists.newArrayList(map.values());
        gameLtvCountService.updateBatchById(ltvCounts);
    }

    private void updateLtvCountField(GameStatLtv gameLtvCount, int j, double remain) {
        if (gameLtvCount != null) {
            if (j > 0 && j <= LTV[0]) {
                gameLtvCount.setD1Amount(BigDecimal.valueOf(remain));
            } else if (j > LTV[0] && j <= LTV[1]) {
                gameLtvCount.setD2Amount(BigDecimal.valueOf(remain));
            } else if (j > LTV[1] && j <= LTV[2]) {
                gameLtvCount.setD3Amount(BigDecimal.valueOf(remain));
            } else if (j > LTV[2] && j <= LTV[3]) {
                gameLtvCount.setD4Amount(BigDecimal.valueOf(remain));
            } else if (j > LTV[3] && j <= LTV[4]) {
                gameLtvCount.setD5Amount(BigDecimal.valueOf(remain));
            } else if (j > LTV[4] && j <= LTV[5]) {
                gameLtvCount.setD6Amount(BigDecimal.valueOf(remain));
            } else if (j > LTV[5] && j <= LTV[6]) {
                gameLtvCount.setD7Amount(BigDecimal.valueOf(remain));
            } else if (j > LTV[6] && j <= LTV[7]) {
                gameLtvCount.setD14Amount(BigDecimal.valueOf(remain));
            } else if (j > LTV[7] && j <= LTV[8]) {
                gameLtvCount.setD21Amount(BigDecimal.valueOf(remain));
            } else if (j > LTV[8] && j <= LTV[9]) {
                gameLtvCount.setD30Amount(BigDecimal.valueOf(remain));
            } else if (j > LTV[9] && j <= LTV[10]) {
                gameLtvCount.setD60Amount(BigDecimal.valueOf(remain));
            } else if (j > LTV[10] && j <= LTV[11]) {
                gameLtvCount.setD90Amount(BigDecimal.valueOf(remain));
            } else if (j > LTV[11] && j <= LTV[12]) {
                gameLtvCount.setD120Amount(BigDecimal.valueOf(remain));
            } else if (j > LTV[12] && j <= LTV[13]) {
                gameLtvCount.setD180Amount(BigDecimal.valueOf(remain));
            } else if (j > LTV[13] && j <= LTV[14]) {
                gameLtvCount.setD360Amount(BigDecimal.valueOf(remain));
            }
        }
    }

    @Override
    public List<GameStatOngoing> countOngoings() {
        List<GameChannelServer> list = gameChannelServerService.list();
        list = list.stream().filter(gameChannelServer -> gameChannelServer.getDelFlag() == 0
                && gameChannelServer.getNoNeedCount() == 0).collect(Collectors.toList());
        Date countDate = DateUtils.addDays(DateUtils.todayDate(), -1);
        Map<String, GameStatOngoing> ongoingMap = countOngoingMap();
        for (GameChannelServer gameChannelServer : list) {
            GameServer gameServer = gameServerService.getById(gameChannelServer.getServerId());
            GameChannel gameChannel = gameChannelService.getById(gameChannelServer.getChannelId());
            int betweenNatural = betweenNatural(gameServer, DateUtils.formatDate(countDate, DatePattern.NORM_DATETIME_PATTERN));
            if (betweenNatural <= 0) {
                continue;
            }
            int[] types = new int[]{1, 2}; // 1-留存 2-ltv
            for (int type : types) {
                countOngoingsByDays(gameChannel, gameServer, countDate, betweenNatural, type, ongoingMap);
            }
        }
        return Lists.newArrayList(ongoingMap.values());
    }

    private Map<String, GameStatOngoing> countOngoingMap() {
        Map<String, GameStatOngoing> map = new HashMap<>();
        List<GameStatOngoing> list = gameCountOngoingService.list();
        for (GameStatOngoing gameCountOngoing : list) {
            String mapKey = mapKey(gameCountOngoing);
            map.put(mapKey, gameCountOngoing);
        }
        return map;
    }

    private String mapKey(GameStatOngoing m) {
        return m.getChannel() + "_" + m.getServerId() + "_" + m.getType() + "_"
                + DateUtils.formatDate(m.getCountDate(), DatePattern.NORM_DATE_PATTERN);
    }

    private void countOngoingsByDays(GameChannel gameChannel, GameServer gameServer, Date countDate,
                                     int betweenNatural, int type, Map<String, GameStatOngoing> ongoingMap) {
        for (int i = 0; i <= betweenNatural; i++) {
            Date nextDate = DateUtils.addDays(gameServer.getOpenTime(), i);
            int leftDays = DateUtils.daysBetweenNatural(nextDate, countDate);
            GameStatOngoing keyObj = new GameStatOngoing().setChannel(gameChannel.getSimpleName())
                    .setServerId(gameServer.getId()).setCountDate(nextDate).setType(type);
            String mapKey = mapKey(keyObj);// 留存
            GameStatOngoing gameCountOngoing = ongoingMap.get(mapKey);
            if (gameCountOngoing == null) {
                // 插入新纪录
                int registerPlayer = logAccountService.loginRegisterPlayer(gameChannel.getSimpleName(), gameServer.getId(), DateUtils.formatDate(nextDate, DatePattern.NORM_DATE_PATTERN), 1);
                keyObj.setRegisterNum((long) registerPlayer);
                gameCountOngoing = keyObj;
            }
            if (gameCountOngoing.getC30() == null) {
                leftDays = Math.min(leftDays, DAYS_BETWEEN);
                // 更新统计
                for (int j = 0; j <= leftDays; j++) {
                    String countField = FIELD + j + 1;
                    BigDecimal getField = (BigDecimal) ReflectUtils.invokeGetField(gameCountOngoing, countField);
                    if (getField == null) {
                        double countValue;
                        // type 1-留存 2-ltv
                        if (type == 1) {
                            countValue = gameDataRemainMapper.selectRemain(gameChannel.getSimpleName(),
                                    gameServer.getId(), DateUtils.formatDateTimeStr(nextDate), logTable, j);
                        } else {
                            countValue = gameLtvCountMapper.selectLtv(gameChannel.getSimpleName(),
                                    gameServer.getId(), DateUtils.formatDateTimeStr(nextDate), j);
                        }
                        ReflectUtils.invokeSetField(gameCountOngoing, countField, countValue);

                        ongoingMap.put(mapKey, gameCountOngoing);
                    }
                }
            }
        }
    }

    @Override
    public List<GameStatOngoing> queryCountOnGoing(int type, GameChannel gameChannel, GameServer gameServer, String rangeDateBegin, String rangeDateEnd) {
        List<GameStatOngoing> list = new ArrayList<>();
        Date dateBegin = DateUtils.parseDate(rangeDateBegin);
        Date dateEnd = DateUtils.parseDate(rangeDateEnd);
        // 数组第一个元素为开始统计的第一个日期
        Date[] dates = ParamValidUtil.dateBegin(dateBegin, dateEnd);
        int dateRangeBetween = ParamValidUtil.dateRangeBetween(dateBegin, dateEnd);
        for (int i = 0; i <= dateRangeBetween; i++) {
            String dateOnly = DateUtils.formatDate(DateUtils.addDays(dates[0], i), DatePattern.NORM_DATE_PATTERN);
            int registerPlayer = logAccountService.loginRegisterPlayer(gameChannel.getSimpleName(), gameServer.getId(), dateOnly, 1);

            GameStatOngoing gameCountOngoing = new GameStatOngoing().setChannel(gameChannel.getSimpleName())
                    .setServerId(gameServer.getId()).setCountDate(DateUtils.parseDate(dateOnly))
                    .setRegisterNum((long) registerPlayer).setType(type).setC1(BigDecimal.valueOf(100));
            list.add(gameCountOngoing);
        }
        return list;
    }
}

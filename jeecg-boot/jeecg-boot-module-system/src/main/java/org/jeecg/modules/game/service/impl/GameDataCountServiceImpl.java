package org.jeecg.modules.game.service.impl;

import cn.hutool.core.date.DatePattern;
import cn.youai.xiuzhen.utils.BigDecimalUtil;
import cn.youai.xiuzhen.utils.DateUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.game.entity.*;
import org.jeecg.modules.game.mapper.GameDataRemainMapper;
import org.jeecg.modules.game.mapper.GameDayDataCountMapper;
import org.jeecg.modules.game.mapper.GameLtvCountMapper;
import org.jeecg.modules.game.service.*;
import org.jeecg.modules.game.util.ParamValidUtil;
import org.jeecg.modules.player.service.ILogAccountService;
import org.jeecg.modules.player.service.IPayOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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

    @Value("${app.log.db.table}")
    private String logTable;

    /**
     * 留存间隔查询
     * 统计间隔+1 = 统计天数
     */
    private static final int[] REMAIN = new int[]{1, 2, 3, 4, 5, 6, 14, 29, 59, 89, 119};

    /**
     * ltv统计间隔
     * 统计间隔 = 统计天数
     */
    private static final int[] LTV = new int[]{1, 2, 3, 4, 5, 6, 7, 14, 21, 30, 60, 90, 120};

    @Override
    public List<GameDayDataCount> queryDateRangeDataCount(GameChannel gameChannel, GameServer gameServer, String rangeDateBegin, String rangeDateEnd) {
        List<GameDayDataCount> list = new ArrayList<>();
        Date dateBegin = DateUtils.parseDate(rangeDateBegin);
        Date dateEnd = DateUtils.parseDate(rangeDateEnd);
        // 数组第一个元素为开始统计的第一个日期
        Date[] dates = ParamValidUtil.dateBegin(dateBegin, dateEnd);
        int dateRangeBetween = ParamValidUtil.dateRangeBetween(dateBegin, dateEnd);
        for (int i = 0; i <= dateRangeBetween; i++) {
            String dateOnly = DateUtils.formatDate(DateUtils.addDays(dates[0], i), DatePattern.NORM_DATE_PATTERN);
            GameDayDataCount gameDataCount = gameDataCount(gameChannel, gameServer, dateOnly);
            list.add(gameDataCount);
        }
        return list;
    }

    @Override
    public GameDayDataCount gameDataCount(GameChannel gameChannel, GameServer gameServer, String date) {

        //当天付费总金额
        double sumPayAmount = payOrderService.sumPayAmount(gameChannel.getSimpleName(), gameServer.getId(), date);
        // 支付玩家数
        int countPay = payOrderService.countPayPlayer(gameChannel.getSimpleName(), gameServer.getId(), date);
        // 当天登陆角色数
        int loginNum = logAccountService.loginRegisterPlayer(gameChannel.getSimpleName(), gameServer.getId(), date, 2);
        // 当天注册角色数
        int registerPlayer = logAccountService.loginRegisterPlayer(gameChannel.getSimpleName(), gameServer.getId(), date, 1);
        // 注册付费总金额
        double registerPayAmount = logAccountService.registerPayAmount(gameChannel.getSimpleName(), gameServer.getId(), date);
        // 注册付费玩家
        int registerPayPlayer = logAccountService.registerPayPlayer(gameChannel.getSimpleName(), gameServer.getId(), date);
        // 注册二次付费玩家
        int doublePayPlayer = logAccountService.doublePayRegisterPlayer(gameChannel.getSimpleName(), gameServer.getId(), date);


        return new GameDayDataCount().setPayAmount(BigDecimalUtil.valueOf(sumPayAmount)).setLoginNum(loginNum)
                .setPayNum(countPay).setArpu(BigDecimalUtil.divideZero(sumPayAmount, loginNum, false))
                .setArppu(BigDecimalUtil.divideZero(sumPayAmount, loginNum, false))
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
            List<GameDayDataCount> gameDayDataCounts = queryDateRangeDataCount(gameChannel, gameServer, formatDate, formatDate);
            gameDayDataCountMapper.updateOrInsert(gameDayDataCounts);

            List<GameDataRemain> gameDataRemains = queryDataRemainCount(gameChannel, gameServer, formatDate, formatDate);
            gameDataRemainMapper.updateOrInsert(gameDataRemains);

            List<GameLtvCount> gameLtvCounts = queryDataLtvCount(gameChannel, gameServer, formatDate, formatDate);
            gameLtvCountMapper.updateOrInsert(gameLtvCounts);
        }
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
    public List<GameDataRemain> queryDataRemainCount(GameChannel gameChannel, GameServer gameServer, String rangeDateBegin, String rangeDateEnd) {
        List<GameDataRemain> list = new ArrayList<>();
        Date dateBegin = DateUtils.parseDate(rangeDateBegin);
        Date dateEnd = DateUtils.parseDate(rangeDateEnd);
        // 数组第一个元素为开始统计的第一个日期
        Date[] dates = ParamValidUtil.dateBegin(dateBegin, dateEnd);
        int dateRangeBetween = ParamValidUtil.dateRangeBetween(dateBegin, dateEnd);
        for (int i = 0; i <= dateRangeBetween; i++) {
            String dateOnly = DateUtils.formatDate(DateUtils.addDays(dates[0], i), DatePattern.NORM_DATE_PATTERN);
            GameDataRemain gameDataRemain = gameDataRemainService.getCountRemain(gameChannel.getSimpleName(), gameServer.getId(), dateOnly);
            list.add(gameDataRemain);
        }
        return list;
    }

    @Override
    public List<GameLtvCount> queryDataLtvCount(GameChannel gameChannel, GameServer gameServer, String rangeDateBegin, String rangeDateEnd) {
        List<GameLtvCount> list = new ArrayList<>();

        Date dateBegin = DateUtils.parseDate(rangeDateBegin);
        Date dateEnd = DateUtils.parseDate(rangeDateEnd);
        // 数组第一个元素为开始统计的第一个日期
        Date[] dates = ParamValidUtil.dateBegin(dateBegin, dateEnd);
        int dateRangeBetween = ParamValidUtil.dateRangeBetween(dateBegin, dateEnd);
        for (int i = 0; i <= dateRangeBetween; i++) {
            String dateOnly = DateUtils.formatDate(DateUtils.addDays(dates[0], i), DatePattern.NORM_DATE_PATTERN);
            GameLtvCount gameLtvCount = gameLtvCountService.getGameLtvCount(gameChannel.getSimpleName(), gameServer.getId(), dateOnly, logTable);
            list.add(gameLtvCount);
        }
        return list;
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
        int betweenNatural = betweenNatural(gameServer, countDate);
        for (int i = 0; i <= betweenNatural; i++) {
            Date nextDate = DateUtils.addDays(gameServer.getOpenTime(), i);
            int leftDays = DateUtils.daysBetweenNatural(nextDate, DateUtils.parseDate(countDate));
            LambdaQueryWrapper<GameDataRemain> queryWrapper = Wrappers.<GameDataRemain>lambdaQuery().eq(GameDataRemain::getChannel, gameChannel.getSimpleName())
                    .eq(GameDataRemain::getCountDate, nextDate).eq(GameDataRemain::getServerId, gameServer.getId());
            GameDataRemain gameDataRemain = gameDataRemainService.getOne(queryWrapper);
            if (gameDataRemain != null && gameDataRemain.getD120Remain() != null) {
                continue;
            } else {
                gameDataRemain = new GameDataRemain().setChannel(gameChannel.getSimpleName()).setServerId(gameServer.getId()).setCountDate(nextDate);
            }
            for (int j = 1; j <= leftDays; j++) {
                int remain = gameDataRemainMapper.selectRemain(gameChannel.getSimpleName(), gameServer.getId(), DateUtils.formatDateTimeStr(nextDate), logTable, j);
                updateRemainCountField(gameDataRemain, j, remain);
            }
            gameDataRemainService.update(gameDataRemain, queryWrapper);
        }
    }

    private void updateRemainCountField(GameDataRemain gameDataRemain, int j, int remain) {
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
            }
        }
    }

    @Override
    public void updateLtvTask(GameChannel gameChannel, GameServer gameServer, String countDate) {
        int betweenNatural = betweenNatural(gameServer, countDate);
        for (int i = 0; i < betweenNatural; i++) {
            Date nextDate = DateUtils.addDays(gameServer.getOpenTime(), i);
            int leftDays = DateUtils.daysBetweenNatural(nextDate, DateUtils.parseDate(countDate));
            LambdaQueryWrapper<GameLtvCount> queryWrapper = Wrappers.<GameLtvCount>lambdaQuery().eq(GameLtvCount::getChannel, gameChannel.getSimpleName())
                    .eq(GameLtvCount::getCountDate, nextDate).eq(GameLtvCount::getServerId, gameServer.getId());
            GameLtvCount gameLtvCount = gameLtvCountService.getOne(queryWrapper);
            if (gameLtvCount != null && gameLtvCount.getD120Amount() != null) {
                continue;
            } else {
                gameLtvCount = new GameLtvCount().setChannel(gameChannel.getSimpleName()).setServerId(gameServer.getId()).setCountDate(nextDate);
            }
            for (int j = 1; j <= leftDays; j++) {
                double remain = gameLtvCountMapper.selectLtv(gameChannel.getSimpleName(), gameServer.getId(), DateUtils.formatDateTimeStr(nextDate), j);
                updateLtvCountField(gameLtvCount, j, remain);
            }
            gameLtvCountMapper.update(gameLtvCount, queryWrapper);
        }
    }

    private void updateLtvCountField(GameLtvCount gameLtvCount, int j, double remain) {
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
            }
        }
    }
}

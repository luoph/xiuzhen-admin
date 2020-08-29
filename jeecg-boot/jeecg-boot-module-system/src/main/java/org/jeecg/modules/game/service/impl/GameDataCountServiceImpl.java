package org.jeecg.modules.game.service.impl;

import cn.hutool.core.date.DatePattern;
import cn.youai.xiuzhen.utils.BigDecimalUtil;
import cn.youai.xiuzhen.utils.DateUtils;
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


    @Override
    public List<GameDayDataCount> queryDateRangeDataCount(int channelId, int serverId, String rangeDateBegin, String rangeDateEnd) {
        List<GameDayDataCount> list = new ArrayList<>();
        GameChannel gameChannel = gameChannelService.getById(channelId);
        if (gameChannel == null) {
            return list;
        }
        GameServer gameServer = gameServerService.getById(serverId);
        if (gameServer == null) {
            return list;
        }
        boolean channelWithServer = gameChannelServerService.isValidChannelWithServer(channelId, serverId);
        if (!channelWithServer) {
            return list;
        }
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

        // 当天付费率
        double payRate = loginNum > 0 ? BigDecimalUtil.div(countPay, loginNum, 2) : 0.00;
        // arpu
        double arpu = loginNum > 0 ? BigDecimalUtil.div(sumPayAmount, loginNum, 2) : 0.00;
        // arppu
        double arppu = loginNum > 0 ? BigDecimalUtil.div(sumPayAmount, loginNum, 2) : 0.00;

        // 当天注册角色数
        int registerPlayer = logAccountService.loginRegisterPlayer(gameChannel.getSimpleName(), gameServer.getId(), date, 1);
        // 注册付费总金额
        double registerPayAmount = logAccountService.registerPayAmount(gameChannel.getSimpleName(), gameServer.getId(), date);
        // 注册付费玩家
        int registerPayPlayer = logAccountService.registerPayPlayer(gameChannel.getSimpleName(), gameServer.getId(), date);
        // 注册二次付费玩家
        int doublePayPlayer = logAccountService.doublePayRegisterPlayer(gameChannel.getSimpleName(), gameServer.getId(), date);

        // 新增注册付费率
        double registerPayRate = registerPlayer > 0 ? BigDecimalUtil.div(registerPayPlayer, registerPlayer, 2) : 0.00;
        // 新增arpu
        double registerArpu = registerPlayer > 0 ? BigDecimalUtil.div(registerPayAmount, registerPlayer, 2) : 0.00;
        // 新增arppu
        double registerArppu = registerPayPlayer > 0 ? BigDecimalUtil.div(registerPayAmount, registerPayPlayer, 2) : 0.00;
        // 二次付费率
        double doublePayRate = registerPayPlayer > 0 ? BigDecimalUtil.div(doublePayPlayer, registerPayPlayer, 2) : 0.00;

        return new GameDayDataCount().setPayAmount(BigDecimal.valueOf(sumPayAmount)).setLoginNum(loginNum)
                .setPayNum(countPay).setArpu(BigDecimal.valueOf(arpu)).setArppu(BigDecimal.valueOf(arppu))
                .setPayRate(BigDecimal.valueOf(payRate)).setAddNum(registerPlayer).setAddPayNum(registerPayPlayer)
                .setAddPayAmount(BigDecimal.valueOf(registerPayAmount)).setAddPayRate(BigDecimal.valueOf(registerPayRate))
                .setDoublePay(doublePayPlayer).setDoublePayRate(BigDecimal.valueOf(doublePayRate))
                .setAddArpu(BigDecimal.valueOf(registerArpu)).setAddArppu(BigDecimal.valueOf(registerArppu))
                .setChannel(gameChannel.getSimpleName()).setServerId(gameServer.getId())
                .setCountDate(DateUtils.parseDate(date)).setCreateTime(DateUtils.now());
    }

    @Override
    public void doJobDataCount() {
        List<GameChannelServer> list = gameChannelServerService.list();
        list = list.stream().filter(gameChannelServer -> gameChannelServer.getDelFlag() == 0).collect(Collectors.toList());
        Date date = DateUtils.addDays(DateUtils.todayDate(), -1);
        String formatDate = DateUtils.formatDate(date, DatePattern.NORM_DATE_PATTERN);
        for (GameChannelServer gameChannelServer : list) {
            List<GameDayDataCount> gameDayDataCounts = queryDateRangeDataCount(Integer.valueOf(gameChannelServer.getChannelId()), gameChannelServer.getServerId(), formatDate, formatDate);
            gameDayDataCountMapper.updateOrInsert(gameDayDataCounts);
            List<GameDataRemain> gameDataRemains = queryDataRemainCount(Integer.valueOf(gameChannelServer.getChannelId()), gameChannelServer.getServerId(), formatDate, formatDate);
            gameDataRemainMapper.updateOrInsert(gameDataRemains);
            List<GameLtvCount> gameLtvCounts = queryDataLtvCount(Integer.valueOf(gameChannelServer.getChannelId()), gameChannelServer.getServerId(), formatDate, formatDate);
            gameLtvCountMapper.updateOrInsert(gameLtvCounts);
        }
    }

    @Override
    public List<GameDataRemain> queryDataRemainCount(int channelId, int serverId, String rangeDateBegin, String rangeDateEnd) {
        List<GameDataRemain> list = new ArrayList<>();
        GameChannel gameChannel = gameChannelService.getById(channelId);
        if (gameChannel == null) {
            return list;
        }
        GameServer gameServer = gameServerService.getById(serverId);
        if (gameServer == null) {
            return list;
        }
        boolean channelWithServer = gameChannelServerService.isValidChannelWithServer(channelId, serverId);
        if (!channelWithServer) {
            return list;
        }
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
    public List<GameLtvCount> queryDataLtvCount(int channelId, int serverId, String rangeDateBegin, String rangeDateEnd) {
        List<GameLtvCount> list = new ArrayList<>();
        GameChannel gameChannel = gameChannelService.getById(channelId);
        if (gameChannel == null) {
            return list;
        }
        GameServer gameServer = gameServerService.getById(serverId);
        if (gameServer == null) {
            return list;
        }
        boolean channelWithServer = gameChannelServerService.isValidChannelWithServer(channelId, serverId);
        if (!channelWithServer) {
            return list;
        }
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
}

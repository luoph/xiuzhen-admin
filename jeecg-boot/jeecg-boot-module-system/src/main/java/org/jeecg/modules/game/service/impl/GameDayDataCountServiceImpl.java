/*
 * create by mybatis-plus-generator  https://github.com/xiweile
 */
package org.jeecg.modules.game.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.youai.server.utils.DateUtils;
import cn.youai.xiuzhen.utils.BigDecimalUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.game.entity.GameServer;
import org.jeecg.modules.game.entity.GameStatDaily;
import org.jeecg.modules.game.mapper.GameDayDataCountMapper;
import org.jeecg.modules.game.service.IGameDayDataCountService;
import org.jeecg.modules.game.service.IGameServerService;
import org.jeecg.modules.game.util.ParamValidUtil;
import org.jeecg.modules.player.service.ILogAccountService;
import org.jeecg.modules.player.service.IPayOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 每日数据统计 服务实现类
 * </p>
 *
 * @author buliangliang
 * @since 2020-08-22
 */
@Service
public class GameDayDataCountServiceImpl extends ServiceImpl<GameDayDataCountMapper, GameStatDaily> implements IGameDayDataCountService {

    @Autowired
    private IPayOrderService payOrderService;

    @Autowired
    private ILogAccountService logAccountService;

    @Autowired
    private IGameServerService gameServerService;

    @Override
    public void calcDailyStat(Collection<Integer> serverIds, Date date) {
        String formatDate = DateUtil.formatDate(date);
        for (Integer serverId : serverIds) {
            GameServer gameServer = gameServerService.getById(serverId);
            if (gameServer.getOpenTime().getTime() <= date.getTime()) {
                continue;
            }

            String f = DateUtil.formatDate(gameServer.getOpenTime());
            List<GameStatDaily> gameDayDataCounts = queryDateRangeDataCount(gameServer.getId(), f, formatDate, false);
            if (CollUtil.isNotEmpty(gameDayDataCounts)) {
                getBaseMapper().updateOrInsert(gameDayDataCounts);
            }
        }
    }

    @Override
    public GameStatDaily gameDataCount(int serverId, String date) {
        // 当天付费总金额
        double sumPayAmount = payOrderService.sumPayAmount(serverId, date);
        // 支付玩家数
        int countPay = payOrderService.countPayPlayer(serverId, date);
        // 当天登录角色数
        int loginNum = logAccountService.loginRegisterPlayer(serverId, date, 2);
        // 当天注册角色数
        int registerPlayer = logAccountService.loginRegisterPlayer(serverId, date, 1);
        // 注册付费总金额
        double registerPayAmount = logAccountService.registerPayAmount(serverId, date);
        // 注册付费玩家
        int registerPayPlayer = logAccountService.registerPayPlayer(serverId, date);
        // 注册二次付费玩家
        int doublePayPlayer = logAccountService.doublePayRegisterPlayer(serverId, date);

        return new GameStatDaily().setPayAmount(BigDecimalUtil.valueOf(sumPayAmount))
                .setLoginNum(loginNum).setPayNum(countPay).setArpu(BigDecimalUtil.divideZero(sumPayAmount, loginNum, false))
                .setArppu(BigDecimalUtil.divideZero(sumPayAmount, countPay, false))
                .setPayRate(BigDecimalUtil.divideZero(countPay, loginNum, true))
                .setAddNum(registerPlayer).setAddPayNum(registerPayPlayer)
                .setAddPayAmount(BigDecimalUtil.valueOf(registerPayAmount))
                .setAddPayRate(BigDecimalUtil.divideZero(registerPayPlayer, registerPlayer, true))
                .setDoublePay(doublePayPlayer).setDoublePayRate(BigDecimalUtil.divideZero(doublePayPlayer, registerPayPlayer, true))
                .setAddArpu(BigDecimalUtil.divideZero(registerPayAmount, registerPlayer, false))
                .setAddArppu(BigDecimalUtil.divideZero(registerPayAmount, registerPayPlayer, false))
                .setServerId(serverId).setCountDate(DateUtils.parseDate(date)).setCreateTime(DateUtils.now());
    }


    private List<GameStatDaily> queryDateRangeDataCount(int serverId, String rangeDateBegin, String rangeDateEnd, boolean isOpenDateCount) {
        Date dateBegin = DateUtils.parseDate(rangeDateBegin);
        Date dateEnd = DateUtils.parseDate(rangeDateEnd);
        // 数组第一个元素为开始统计的第一个日期
        int dateRangeBetween = ParamValidUtil.dateRangeBetween(dateBegin, dateEnd);
        List<GameStatDaily> list = new ArrayList<>(dateRangeBetween);
        for (int i = 0; i <= dateRangeBetween; i++) {
            String dateOnly = DateUtil.formatDate(DateUtils.addDays(dateBegin, i));
            GameStatDaily gameDataCount = gameDataCount(serverId, dateOnly);
            list.add(gameDataCount);
        }
        return list;
    }

}

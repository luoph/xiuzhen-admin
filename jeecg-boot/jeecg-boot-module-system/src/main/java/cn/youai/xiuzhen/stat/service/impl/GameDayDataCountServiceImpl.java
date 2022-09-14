/*
 * create by mybatis-plus-generator  https://github.com/xiweile
 */
package cn.youai.xiuzhen.stat.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.youai.server.utils.DateUtils;
import cn.youai.xiuzhen.game.entity.GameServer;
import cn.youai.xiuzhen.game.service.IGameServerService;
import cn.youai.xiuzhen.stat.entity.GameStatDaily;
import cn.youai.xiuzhen.stat.mapper.GameDayDataCountMapper;
import cn.youai.xiuzhen.stat.service.IGameDayDataCountService;
import cn.youai.xiuzhen.stat.service.IGameOrderStatService;
import cn.youai.xiuzhen.stat.service.ILogAccountService;
import cn.youai.xiuzhen.utils.BigDecimalUtils;
import cn.youai.xiuzhen.utils.ParamUtils;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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
@DS("shardingSphere")
public class GameDayDataCountServiceImpl extends ServiceImpl<GameDayDataCountMapper, GameStatDaily> implements IGameDayDataCountService {

    @Autowired
    private IGameOrderStatService payOrderService;

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

        return new GameStatDaily().setPayAmount(BigDecimalUtils.valueOf(sumPayAmount))
                .setLoginNum(loginNum).setPayNum(countPay).setArpu(BigDecimalUtils.divideZero(sumPayAmount, loginNum, false))
                .setArppu(BigDecimalUtils.divideZero(sumPayAmount, countPay, false))
                .setPayRate(BigDecimalUtils.divideZero(countPay, loginNum, true))
                .setAddNum(registerPlayer).setAddPayNum(registerPayPlayer)
                .setAddPayAmount(BigDecimalUtils.valueOf(registerPayAmount))
                .setAddPayRate(BigDecimalUtils.divideZero(registerPayPlayer, registerPlayer, true))
                .setDoublePay(doublePayPlayer).setDoublePayRate(BigDecimalUtils.divideZero(doublePayPlayer, registerPayPlayer, true))
                .setAddArpu(BigDecimalUtils.divideZero(registerPayAmount, registerPlayer, false))
                .setAddArppu(BigDecimalUtils.divideZero(registerPayAmount, registerPayPlayer, false))
                .setServerId(serverId).setCountDate(DateUtils.parseDate(date)).setCreateTime(DateUtils.now());
    }


    private List<GameStatDaily> queryDateRangeDataCount(int serverId, String rangeDateBegin, String rangeDateEnd, boolean isOpenDateCount) {
        Date dateBegin = DateUtils.parseDate(rangeDateBegin);
        Date dateEnd = DateUtils.parseDate(rangeDateEnd);
        // 数组第一个元素为开始统计的第一个日期
        int dateRangeBetween = ParamUtils.dateRangeBetween(dateBegin, dateEnd);
        List<GameStatDaily> list = new ArrayList<>(dateRangeBetween);
        for (int i = 0; i <= dateRangeBetween; i++) {
            String dateOnly = DateUtil.formatDate(DateUtils.addDays(dateBegin, i));
            GameStatDaily gameDataCount = gameDataCount(serverId, dateOnly);
            list.add(gameDataCount);
        }
        return list;
    }

}

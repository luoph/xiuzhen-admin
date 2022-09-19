/*
 * create by mybatis-plus-generator  https://github.com/xiweile
 */
package cn.youai.xiuzhen.stat.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.youai.enums.AccountLogType;
import cn.youai.server.utils.DateUtils;
import cn.youai.xiuzhen.game.entity.GameServer;
import cn.youai.xiuzhen.game.service.IGameServerService;
import cn.youai.xiuzhen.stat.entity.GameStatDaily;
import cn.youai.xiuzhen.stat.mapper.GameStatDailyMapper;
import cn.youai.xiuzhen.stat.service.IGameOrderStatService;
import cn.youai.xiuzhen.stat.service.IGameStatDailyService;
import cn.youai.xiuzhen.stat.service.ILogAccountService;
import cn.youai.xiuzhen.utils.BigDecimalUtils;
import cn.youai.xiuzhen.utils.ParamUtils;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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
public class GameStatDailyServiceImpl extends ServiceImpl<GameStatDailyMapper, GameStatDaily> implements IGameStatDailyService {

    @Autowired
    private IGameOrderStatService payOrderService;

    @Autowired
    private ILogAccountService logAccountService;

    @Autowired
    private IGameServerService gameServerService;

    @Override
    public int updateOrInsert(List<GameStatDaily> list) {
        return getBaseMapper().updateOrInsert(list);
    }

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
    public GameStatDaily getGameStatDaily(int serverId, Date date) {
        // 当天付费总金额
        BigDecimal payAmount = payOrderService.serverPayAmount(serverId, date);
        // 支付玩家数
        int payPlayerNum = payOrderService.serverPayPlayerNum(serverId, date);
        // 当天登录角色数
        int loginNum = logAccountService.serverLoginRegisterPlayerNum(serverId, date, AccountLogType.LOGIN.getType());
        // 当天注册角色数
        int registerPlayer = logAccountService.serverLoginRegisterPlayerNum(serverId, date, AccountLogType.REGISTER.getType());
        // 注册付费总金额
        BigDecimal registerPayAmount = logAccountService.serverRegisterPayAmount(serverId, date);
        // 注册付费玩家
        int registerPayPlayer = logAccountService.serverRegisterPayPlayerNum(serverId, date);
        // 注册二次付费玩家
        int doublePayPlayer = logAccountService.serverDoublePayRegisterPlayer(serverId, date);
        // 唯一主键：`GAME_DAY_COUNT` (`channel`,`server_id`,`count_date`)
        return GameStatDaily.of(serverId, date, payAmount, payPlayerNum, loginNum, registerPlayer, registerPayAmount, registerPayPlayer, doublePayPlayer);
    }

    @Override
    public GameStatDaily getGameStatDaily(String channel, Date date) {
        // 当天付费总金额
        BigDecimal payAmount = payOrderService.channelPayAmount(channel, date);
        // 支付玩家数
        int payPlayerNum = payOrderService.channelPayPlayerNum(channel, date);
        // 当天登录角色数
        int loginNum = logAccountService.channelLoginRegisterPlayerNum(channel, date, AccountLogType.LOGIN.getType());
        // 当天注册角色数
        int registerPlayer = logAccountService.channelLoginRegisterPlayerNum(channel, date, AccountLogType.REGISTER.getType());
        // 注册付费总金额
        BigDecimal registerPayAmount = logAccountService.channelRegisterPayAmount(channel, date);
        // 注册付费玩家
        int registerPayPlayer = logAccountService.channelRegisterPayPlayerNum(channel, date);
        // 注册二次付费玩家
        int doublePayPlayer = logAccountService.channelDoublePayRegisterPlayer(channel, date);
        return GameStatDaily.of(channel, date, payAmount, payPlayerNum, loginNum, registerPlayer, registerPayAmount, registerPayPlayer, doublePayPlayer);
    }

    private List<GameStatDaily> queryDateRangeDataCount(int serverId, String rangeDateBegin, String rangeDateEnd, boolean isOpenDateCount) {
        Date dateBegin = DateUtils.parseDate(rangeDateBegin);
        Date dateEnd = DateUtils.parseDate(rangeDateEnd);
        // 数组第一个元素为开始统计的第一个日期
        int dateRangeBetween = ParamUtils.dateRangeBetween(dateBegin, dateEnd);
        List<GameStatDaily> list = new ArrayList<>(dateRangeBetween);
        for (int i = 0; i <= dateRangeBetween; i++) {
            GameStatDaily gameDataCount = getGameStatDaily(serverId, DateUtils.addDays(dateBegin, i));
            list.add(gameDataCount);
        }
        return list;
    }

}

package org.jeecg.modules.game.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.youai.server.utils.DateUtils;
import cn.youai.server.utils.QueryUtils;
import cn.youai.xiuzhen.utils.BigDecimalUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.game.constant.CoreStatisticType;
import org.jeecg.modules.game.entity.*;
import org.jeecg.modules.game.mapper.GameDayDataCountMapper;
import org.jeecg.modules.game.mapper.GameStatLtvMapper;
import org.jeecg.modules.game.mapper.GameStatRemainMapper;
import org.jeecg.modules.game.service.IGameChannelServerService;
import org.jeecg.modules.game.service.IGameDataCountService;
import org.jeecg.modules.game.service.IGameDataRemainService;
import org.jeecg.modules.game.service.IGameServerService;
import org.jeecg.modules.game.util.ParamValidUtil;
import org.jeecg.modules.player.service.ILogAccountService;
import org.jeecg.modules.player.service.IPayOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
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

    @Value("${app.log.db}")
    private String logDb;

    @Value("${app.log.db.table}")
    private String logTable;

    /**
     * 留存间隔查询
     */
    private static final int[] REMAIN = new int[]{2, 3, 4, 5, 6, 7, 15, 30, 60, 90, 120, 180, 360};
    /**
     * LTV统计间隔
     */
    private static final int[] LTV = new int[]{1, 2, 3, 4, 5, 6, 7, 14, 21, 30, 60, 90, 120, 180, 360};

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
    @Resource
    private GameDayDataCountMapper gameDayDataCountMapper;
    @Resource
    private GameStatLtvMapper gameLtvCountMapper;
    @Resource
    private GameStatRemainMapper gameStatRemainMapper;

    @Override
    public List<GameStatDaily> queryDateRangeDataCount(int serverId, String rangeDateBegin, String rangeDateEnd, boolean isOpenDateCount) {
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

    private String dailyCountKey(int serverId, String countDate) {
        return serverId + "_" + countDate;
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
                doJobDataCountToDaily(serverMap.keySet(), date);
                break;
            case REMAIN:
                doJobDataCountToRemain(serverMap.keySet(), registerDate, days + 1, true);
                break;
            case LTV:
                doJobDataCountToLtv(serverMap.keySet(), registerDate, days + 1, true);
                break;
            default:
                break;
        }
    }

    /**
     * 添加daily，每日统计新记录
     */
    private void doJobDataCountToDaily(Collection<Integer> serverIds, Date date) {
        String formatDate = DateUtil.formatDate(date);
        for (Integer serverId : serverIds) {
            GameServer gameServer = gameServerService.getById(serverId);
            if (gameServer.getOpenTime().getTime() <= date.getTime()) {
                continue;
            }

            String f = DateUtil.formatDate(gameServer.getOpenTime());
            List<GameStatDaily> gameDayDataCounts = queryDateRangeDataCount(gameServer.getId(), f, formatDate, false);
            if (CollUtil.isNotEmpty(gameDayDataCounts)) {
                gameDayDataCountMapper.updateOrInsert(gameDayDataCounts);
            }
        }
    }

    /**
     * 添加remain，每日统计新记录
     */
    private void doJobDataCountToRemain(Collection<Integer> serverIds, Date registerDate, int days, boolean updateAll) {
        String date = DateUtil.formatDate(registerDate);
        for (Integer serverId : serverIds) {
            GameServer gameServer = gameServerService.getById(serverId);
            if (gameServer == null || gameServer.getOpenTime().after(registerDate)) {
                continue;
            }

            LambdaQueryWrapper<GameStatRemain> query = Wrappers.<GameStatRemain>lambdaQuery()
                    .eq(GameStatRemain::getServerId, serverId)
                    .eq(GameStatRemain::getCountDate, registerDate);

            // 重新查询注册数量
            GameStatRemain updatedRemain = gameStatRemainMapper.getGameStatRemain(serverId, date);
            GameStatRemain gameStatRemain = gameStatRemainMapper.selectOne(QueryUtils.safeSelectOneQuery(query));
            if (gameStatRemain == null) {
                gameStatRemain = updatedRemain;
            }

            // 付费玩家数量
            gameStatRemain.setRegisterNum(updatedRemain.getRegisterNum());
            // 更新免费玩家数量
            gameStatRemain.setFreeNum(updatedRemain.getRegisterNum() - updatedRemain.getPayNum());

            // 免费、付费留存
            if (days == 2 || updateAll) {
                gameStatRemain.setPayRemain(gameStatRemainMapper.getPayRemain(serverId, date, logDb));
                gameStatRemain.setFreeRemain(gameStatRemainMapper.getFreeRemain(serverId, date, logDb));
            }

            if (updateAll) {
                // 更新全部字段
                for (int i : REMAIN) {
                    if (days < i) {
                        break;
                    }
                    calcRemainAmount(gameStatRemain, serverId, date, i);
                }
            }
            calcRemainAmount(gameStatRemain, serverId, date, days);

            if (gameStatRemain.getId() != null) {
                gameStatRemainMapper.updateById(gameStatRemain);
            } else {
                gameStatRemainMapper.insert(gameStatRemain);
            }
        }
    }

    /**
     * 添加ltv，每日统计新记录
     */
    private void doJobDataCountToLtv(Collection<Integer> serverIds, Date registerDate, int days, boolean updateAll) {
        String date = DateUtil.formatDate(registerDate);
        for (Integer serverId : serverIds) {
            GameServer gameServer = gameServerService.getById(serverId);
            if (gameServer == null || gameServer.getOpenTime().after(registerDate)) {
                continue;
            }

            LambdaQueryWrapper<GameStatLtv> query = Wrappers.<GameStatLtv>lambdaQuery()
                    .eq(GameStatLtv::getServerId, serverId)
                    .eq(GameStatLtv::getCountDate, registerDate);

            // 重新查询注册数量
            GameStatLtv updatedLtv = gameLtvCountMapper.getGameStatLtv(serverId, date);
            GameStatLtv gameStatLtv = gameLtvCountMapper.selectOne(QueryUtils.safeSelectOneQuery(query));
            if (gameStatLtv == null) {
                gameStatLtv = updatedLtv;
            } else {
                gameStatLtv.setRegisterNum(updatedLtv.getRegisterNum());
            }

            if (updateAll) {
                // 更新全部字段
                for (int i : LTV) {
                    if (days < i) {
                        break;
                    }
                    calcLtvAmount(gameStatLtv, serverId, date, i);
                }
            }
            calcLtvAmount(gameStatLtv, serverId, date, days);

            if (gameStatLtv.getId() != null) {
                gameLtvCountMapper.updateById(gameStatLtv);
            } else {
                gameLtvCountMapper.insert(gameStatLtv);
            }
        }
    }

    private void calcRemainAmount(GameStatRemain entity, int serverId, String registerDate, int days) {
        int registerNum = entity.getRegisterNum() != null ? entity.getRegisterNum() : 0;
        // 注册为0, 直接返回
        if (registerNum <= 0 || days <= 1 || !ArrayUtil.contains(REMAIN, days)) {
            return;
        }

        ServerRemain serverRemain = gameStatRemainMapper.selectRemain(serverId, registerDate, days, logDb);
        if (days <= 2) {
            entity.setD2Remain(serverRemain.getRemain());
        } else if (days <= 3) {
            entity.setD3Remain(serverRemain.getRemain());
        } else if (days <= 4) {
            entity.setD4Remain(serverRemain.getRemain());
        } else if (days <= 5) {
            entity.setD5Remain(serverRemain.getRemain());
        } else if (days <= 6) {
            entity.setD6Remain(serverRemain.getRemain());
        } else if (days <= 7) {
            entity.setD7Remain(serverRemain.getRemain());
        } else if (days <= 15) {
            entity.setD15Remain(serverRemain.getRemain());
        } else if (days <= 30) {
            entity.setD30Remain(serverRemain.getRemain());
        } else if (days <= 60) {
            entity.setD60Remain(serverRemain.getRemain());
        } else if (days <= 90) {
            entity.setD90Remain(serverRemain.getRemain());
        } else if (days <= 120) {
            entity.setD120Remain(serverRemain.getRemain());
        } else if (days <= 180) {
            entity.setD180Remain(serverRemain.getRemain());
        } else if (days <= 360) {
            entity.setD360Remain(serverRemain.getRemain());
        }
    }

    private void calcLtvAmount(GameStatLtv entity, int serverId, String registerDate, int days) {
        int registerNum = entity.getRegisterNum() != null ? entity.getRegisterNum() : 0;
        // 注册为0, 直接返回
        if (registerNum <= 0) {
            return;
        }

        ServerLtvAmount ltvAmount = gameLtvCountMapper.getLtvAmount(serverId, registerDate, days);
        if (days <= 1) {
            entity.setD1Amount(ltvAmount.getTotalAmount());
        } else if (days <= 2) {
            entity.setD2Amount(ltvAmount.getTotalAmount());
        } else if (days <= 3) {
            entity.setD3Amount(ltvAmount.getTotalAmount());
        } else if (days <= 4) {
            entity.setD4Amount(ltvAmount.getTotalAmount());
        } else if (days <= 5) {
            entity.setD5Amount(ltvAmount.getTotalAmount());
        } else if (days <= 6) {
            entity.setD6Amount(ltvAmount.getTotalAmount());
        } else if (days <= 7) {
            entity.setD7Amount(ltvAmount.getTotalAmount());
        } else if (days <= 14) {
            entity.setD14Amount(ltvAmount.getTotalAmount());
        } else if (days <= 21) {
            entity.setD21Amount(ltvAmount.getTotalAmount());
        } else if (days <= 30) {
            entity.setD30Amount(ltvAmount.getTotalAmount());
        } else if (days <= 60) {
            entity.setD60Amount(ltvAmount.getTotalAmount());
        } else if (days <= 90) {
            entity.setD90Amount(ltvAmount.getTotalAmount());
        } else if (days <= 120) {
            entity.setD120Amount(ltvAmount.getTotalAmount());
        } else if (days <= 180) {
            entity.setD180Amount(ltvAmount.getTotalAmount());
        } else if (days <= 360) {
            entity.setD360Amount(ltvAmount.getTotalAmount());
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

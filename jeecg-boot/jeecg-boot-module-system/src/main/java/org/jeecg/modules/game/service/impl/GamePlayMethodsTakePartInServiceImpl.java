package org.jeecg.modules.game.service.impl;

import cn.youai.server.component.ConfigManager;
import cn.youai.xiuzhen.config.GameConfig;
import cn.youai.xiuzhen.entity.pojo.ConfMedicine;
import cn.youai.xiuzhen.entity.pojo.ConfRefineEquip;
import cn.youai.xiuzhen.entity.pojo.PlayerLogType;
import cn.youai.xiuzhen.utils.BigDecimalUtil;
import cn.youai.xiuzhen.utils.DateUtils;
import com.googlecode.cqengine.query.Query;
import com.googlecode.cqengine.query.QueryFactory;
import com.googlecode.cqengine.query.logical.And;
import com.googlecode.cqengine.query.option.QueryOptions;
import org.jeecg.modules.game.entity.GamePlayMethodsTakePartInVO;
import org.jeecg.modules.game.entity.LogAccount;
import org.jeecg.modules.game.entity.LogPlayer;
import org.jeecg.modules.game.mapper.PlayMethodsTakePartInMapper;
import org.jeecg.modules.game.service.IGamePlayMethodsTakePartInService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author 胡立
 * @Description: GamePlayMethodsTakePartInServiceImpl
 * @date 2021/1/11 14:45
 */

@Service
public class GamePlayMethodsTakePartInServiceImpl implements IGamePlayMethodsTakePartInService {

    @Resource
    PlayMethodsTakePartInMapper playMethodsTakePartInMapper;
    @Value("${app.log.db.table_log_player}")
    String logPlayerTable;
    @Value("${app.log.db.table}")
    String logAccountTable;

    @Override
    public List<GamePlayMethodsTakePartInVO> playMethodsTakePartList(int fullTimes, int grade, String playMethodsType, Date createDateBegin, Date createDateEnd, int serverId) {

        List<GamePlayMethodsTakePartInVO> gamePlayMethodsTakePartInVOList = new ArrayList<>();
        // 查询时间范围内某玩法类型的玩家日志
        List<LogPlayer> playerLogList = playMethodsTakePartInMapper.conditionSelectPlayerLog(playMethodsType, createDateBegin, createDateEnd, logPlayerTable, serverId);

        // 查询炼丹、炼器
        boolean isRefineDanOrEquip = PlayerLogType.REFINE_DAN.getType() == Integer.parseInt(playMethodsType) || PlayerLogType.REFINE_EQUIP.getType() == Integer.parseInt(playMethodsType);
        // 过滤极品丹药或者仙器
        if (isRefineDanOrEquip) {
            playerLogList = filterRefineDanOrEquip(playerLogList, playMethodsType);
        }

        // 玩家日志日志以时间分组
        Map<Date, List<LogPlayer>> playerLogListMapCreateDate = playerLogList.stream().collect(Collectors.groupingBy(LogPlayer::getCreateDate));
        // 查询到达某等级用户的登录日志
        List<LogAccount> accountLogList = playMethodsTakePartInMapper.selectPlayLoginInfo(grade, logAccountTable, serverId);
        // 登录日志以时间分组
        Map<Date, List<LogAccount>> accountLogListCreateDate = accountLogList.stream().collect(Collectors.groupingBy(LogAccount::getCreateDate));


        for (Date s : playerLogListMapCreateDate.keySet()) {
            // 时间内玩家日志以用户id分组
            Map<Long, List<LogPlayer>> playerLogListMapCreateDatePlayerId = playerLogListMapCreateDate.get(s).stream().collect(Collectors.groupingBy(LogPlayer::getPlayerId));

            // 时间内登录日志以用户id分组
            Map<Long, List<LogAccount>> accountLogListCreateDatePlayerId;
            if (null == accountLogListCreateDate.get(s)) {
                continue;
            }
            accountLogListCreateDatePlayerId = accountLogListCreateDate.get(s).stream().collect(Collectors.groupingBy(LogAccount::getPlayerId));

            // 参与人数
            int takePlayerNum = playerLogListMapCreateDatePlayerId.size();
            // xx级登录人数
            int playerNum = accountLogListCreateDatePlayerId.size();
            Date yesterdayDate = DateUtils.addDays(s, -1);
            // 昨天参与该玩法的玩家
            List<LogPlayer> yesterdayDateData = playerLogListMapCreateDate.get(yesterdayDate);
            // 昨天参与该玩法的玩家 今天参与的人数
            int yesterdayHasPlayInTodaySum = 0;
            // 昨天参与该玩法的玩家 今天有登录的人数
            int yesterdayHasLoginInTodaySum = 0;
            if (null != yesterdayDateData) {
                // 昨天参与该玩法的玩家 以player_id分组
                Map<Long, List<LogPlayer>> yesterdayDateDataMapPlayerId = yesterdayDateData.stream().collect(Collectors.groupingBy(LogPlayer::getPlayerId));
                for (Long mapkey : yesterdayDateDataMapPlayerId.keySet()) {
                    if (null != playerLogListMapCreateDatePlayerId.get(mapkey)) {
                        yesterdayHasPlayInTodaySum++;
                    }
                }
                for (Long mapkey : yesterdayDateDataMapPlayerId.keySet()) {
                    if (null != accountLogListCreateDatePlayerId.get(mapkey)) {
                        yesterdayHasLoginInTodaySum++;
                    }
                }
            }

            // 设置满参与人数
            // 100以上的类型记录的是次数
            if (Integer.parseInt(playMethodsType) >= 100) {
                // 时间内玩家日志以用户id分组 且 过滤掉没有达到满次的数据
                Map<Long, List<LogPlayer>> playerLogListMapCreateDatePlayerIdFullTimes = new HashMap<>(16);
                for (Long mapKey : playerLogListMapCreateDatePlayerId.keySet()) {
                    if (playerLogListMapCreateDatePlayerId.get(mapKey).size() >= fullTimes) {
                        playerLogListMapCreateDatePlayerIdFullTimes.put(mapKey, playerLogListMapCreateDatePlayerId.get(mapKey));
                    }
                }
                gamePlayMethodsTakePartInVOList.add(new GamePlayMethodsTakePartInVO()
                        .setDate(s)
                        .setTakePlayInPlayerNum(takePlayerNum)
                        .setPlayerNum(playerNum)
                        .setTakePlayInRate(BigDecimalUtil.divide(takePlayerNum * 100, playerNum, 2))
                        .setAllTakePlayInPlayerNum(playerLogListMapCreateDatePlayerIdFullTimes.size())
                        .setAllTakePlayInRate(BigDecimalUtil.divide(playerLogListMapCreateDatePlayerIdFullTimes.size() * 100, playerNum, 2))
                        .setSecondGlanceRate(0 == yesterdayHasLoginInTodaySum ? BigDecimal.ZERO : BigDecimalUtil.divide(yesterdayHasPlayInTodaySum * 100, yesterdayHasLoginInTodaySum, 2)));
                // 100以下记录的是最大值
            } else {
                List<LogPlayer> playerOneDayPlayLogFullTimes = new ArrayList<>();
                for (Long mapKey : playerLogListMapCreateDatePlayerId.keySet()) {
                    List<LogPlayer> playerOneDayPlayLog = playerLogListMapCreateDatePlayerId.get(mapKey);
                    // 过滤出最大值 达到满次的数据
                    playerOneDayPlayLogFullTimes = playerOneDayPlayLog.stream().filter(logPlayer -> logPlayer.getValue() >= fullTimes).collect(Collectors.toList());
                }
                gamePlayMethodsTakePartInVOList.add(new GamePlayMethodsTakePartInVO()
                        .setDate(s)
                        .setTakePlayInPlayerNum(takePlayerNum)
                        .setPlayerNum(playerNum)
                        .setTakePlayInRate(BigDecimalUtil.divide(takePlayerNum * 100, playerNum, 2))
                        .setAllTakePlayInPlayerNum(playerOneDayPlayLogFullTimes.size())
                        .setAllTakePlayInRate(BigDecimalUtil.divide(playerOneDayPlayLogFullTimes.size() * 100, playerNum, 2))
                        .setSecondGlanceRate(0 == yesterdayHasLoginInTodaySum ? BigDecimal.ZERO : BigDecimalUtil.divide(yesterdayHasPlayInTodaySum * 100, yesterdayHasLoginInTodaySum, 2)));
            }
        }
        return gamePlayMethodsTakePartInVOList.stream().sorted((s1, s2) -> s2.getDate().compareTo(s1.getDate())).collect(Collectors.toList());
    }

    /**
     * 过滤炼丹或者炼器条件的日志集
     * 炼丹:极品丹药
     * 炼器:仙器以上
     */
    private List<LogPlayer> filterRefineDanOrEquip(List<LogPlayer> playerLogListMapCreateDatePlayerId, String playMethodsType) {
        // 丹药、炼器id集合
        List<Integer> ids = playerLogListMapCreateDatePlayerId.stream().filter(e -> e.getParam1() != null && e.getParam1() > 0)
                .map(LogPlayer::getParam1).collect(Collectors.toList());

        // 满足条件的id-Map
        Map<Integer, Integer> filterIdMap;

        // 丹药
        if (PlayerLogType.REFINE_DAN.getType() == Integer.parseInt(playMethodsType)) {
            QueryOptions queryOptions = QueryFactory.queryOptions(ConfMedicine.ITEM_ID);
            Query<ConfMedicine> in = QueryFactory.in(ConfMedicine.ITEM_ID, ids);
            // 极品丹药
            Query<ConfMedicine> equal = QueryFactory.equal(ConfMedicine.QUA, 4);
            And<ConfMedicine> and = QueryFactory.and(in, equal);
            List<ConfMedicine> confMedicines = ConfigManager.list(GameConfig.REFINE_MEDICINE, ConfMedicine.class, and, queryOptions);
            filterIdMap = confMedicines.stream().collect(Collectors.toMap(ConfMedicine::getItemId, ConfMedicine::getItemId, (k1, k2) -> k2));
        } else {
            QueryOptions queryOptions = QueryFactory.queryOptions(QueryFactory.orderBy(QueryFactory.ascending(ConfRefineEquip.ITEM_ID)));
            Query<ConfRefineEquip> in = QueryFactory.in(ConfRefineEquip.ITEM_ID, ids);
            // 仙器以上
            Query<ConfRefineEquip> greaterThan = QueryFactory.greaterThan(ConfRefineEquip.SUIT_TYPE, 2);
            And<ConfRefineEquip> and = QueryFactory.and(in, greaterThan);
            List<ConfRefineEquip> confRefineEquips = ConfigManager.list(GameConfig.REFINE_EQUIP, ConfRefineEquip.class, and, queryOptions);
            filterIdMap = confRefineEquips.stream().collect(Collectors.toMap(ConfRefineEquip::getItemId, ConfRefineEquip::getItemId, (k1, k2) -> k2));
        }

        return playerLogListMapCreateDatePlayerId.stream().filter(e -> filterIdMap.get(e.getParam1()) != null).collect(Collectors.toList());
    }


}
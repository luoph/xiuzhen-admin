package org.jeecg.modules.game.service.impl;

import cn.hutool.core.date.DatePattern;
import cn.youai.xiuzhen.utils.BigDecimalUtil;
import cn.youai.xiuzhen.utils.DateUtils;
import org.jeecg.modules.game.entity.GamePlayMethodsTakePartInVO;
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
    public List<GamePlayMethodsTakePartInVO> playMethodsTakePartList( int fullTimes, int grade, String playMethodsType, Date createDateBegin, Date createDateEnd, int serverId) {

        List<GamePlayMethodsTakePartInVO> gamePlayMethodsTakePartInVOList = new ArrayList<>();
        // 查询时间范围内某玩法类型的玩家日志
        List<Map<String, Object>> playerLogList = playMethodsTakePartInMapper.conditionSelectPlayerLog(playMethodsType, createDateBegin, createDateEnd, logPlayerTable, serverId);
        // 玩家日志日志以时间分组
        Map<String, List<Map<String, Object>>> playerLogListMapCreateDate = playerLogList.stream().collect(Collectors.groupingBy(map -> map.get("create_date").toString()));
        //查询到达某等级用户的登录日志
        List<Map<String, Object>> accountLogList = playMethodsTakePartInMapper.selectPlayLoginInfo(grade, logAccountTable, serverId);
        //登录日志以时间分组
        Map<String, List<Map<String, Object>>> accountLogListCreateDate = accountLogList.stream().collect(Collectors.groupingBy(map -> map.get("create_date").toString()));

        for (String s : playerLogListMapCreateDate.keySet()) {
            //时间内玩家日志以用户id分组
            Map<String ,List<Map<String, Object>>> playerLogListMapCreateDatePlayerId =  playerLogListMapCreateDate.get(s).stream().collect(Collectors.groupingBy(map -> map.get("player_id").toString()));

            //时间内登录日志以用户id分组
            Map<String, List<Map<String, Object>>> accountLogListCreateDatePlayerId;
            if (null == accountLogListCreateDate.get(s)) {
                continue;
            } else {
                accountLogListCreateDatePlayerId = accountLogListCreateDate.get(s).stream().collect(Collectors.groupingBy(map -> map.get("player_id").toString()));
            }
            GamePlayMethodsTakePartInVO  gamePlayMethodsTakePartInVO = new GamePlayMethodsTakePartInVO();
            //设置日期
            gamePlayMethodsTakePartInVO.setDate(DateUtils.parseDate(s));
            //设置参与人数
            int takePlayerNum = playerLogListMapCreateDatePlayerId.size();
            gamePlayMethodsTakePartInVO.setTakePlayInPlayerNum(takePlayerNum);
            //设置xx级登录人数
            int playerNum = accountLogListCreateDatePlayerId.size();
            gamePlayMethodsTakePartInVO.setPlayerNum(playerNum);
            //设置参与率
            gamePlayMethodsTakePartInVO.setTakePlayInRate(BigDecimalUtil.divide(takePlayerNum * 100, playerNum,2));
            //设置满参与人数
                //100以上的类型记录的是次数
            if ( Integer.parseInt(playMethodsType) >= 100 ) {
                //时间内玩家日志以用户id分组 且 过滤掉没有达到满次的数据
                Map<String ,List<Map<String, Object>>> playerLogListMapCreateDatePlayerIdFullTimes = new HashMap<>(16);
                for (String s1 : playerLogListMapCreateDatePlayerId.keySet()) {
                    if ( playerLogListMapCreateDatePlayerId.get(s1).size() >= fullTimes)  {
                        playerLogListMapCreateDatePlayerIdFullTimes.put(s1, playerLogListMapCreateDatePlayerId.get(s1));
                    }
                }
                gamePlayMethodsTakePartInVO.setAllTakePlayInPlayerNum(playerLogListMapCreateDatePlayerIdFullTimes.size());
                //100以下记录的是最大值
            }else{
                List<Map<String, Object>> playerOneDayPlayLogFullTimes = new ArrayList<>();
                for (String s1 : playerLogListMapCreateDatePlayerId.keySet()) {
                    List<Map<String, Object>> playerOneDayPlayLog = playerLogListMapCreateDatePlayerId.get(s1);
                    //过滤出最大值 达到满次的数据
                    playerOneDayPlayLogFullTimes = playerOneDayPlayLog.stream().filter(map -> Integer.parseInt(map.get("value").toString()) >= fullTimes).collect(Collectors.toList());
                }
                gamePlayMethodsTakePartInVO.setAllTakePlayInPlayerNum(playerOneDayPlayLogFullTimes.size());
            }
            //设置满参率
            gamePlayMethodsTakePartInVO.setAllTakePlayInRate(BigDecimalUtil.divide(gamePlayMethodsTakePartInVO.getAllTakePlayInPlayerNum() * 100, playerNum,2));
            //设置回头率（昨天有参与该玩法的玩家中，今天参与的人数/这些玩家今天有登录的人数）
            Date yesterdayDate = DateUtils.addDays(DateUtils.parseDate(s), -1);
              //昨天参与该玩法的玩家
            List<Map<String, Object>> yesterdayDateData = playerLogListMapCreateDate.get(DateUtils.formatDate(yesterdayDate, DatePattern.NORM_DATE_PATTERN));
            if (null == yesterdayDateData) {
                gamePlayMethodsTakePartInVO.setSecondGlanceRate(new BigDecimal(0));
                gamePlayMethodsTakePartInVOList.add(gamePlayMethodsTakePartInVO);
                continue;
            }
              //昨天参与该玩法的玩家 以player_id分组
            Map<String ,List<Map<String, Object>>> yesterdayDateDataMapPlayerId =  yesterdayDateData.stream().collect(Collectors.groupingBy(map -> map.get("player_id").toString()));
              //昨天参与该玩法的玩家 今天参与的人数
            int yesterdayHasPlayInTodaySum = 0;
            for (String s1 : yesterdayDateDataMapPlayerId.keySet()) {
                if (null != playerLogListMapCreateDatePlayerId.get(s1)) {
                    yesterdayHasPlayInTodaySum ++;
                }
            }
              //昨天参与该玩法的玩家 今天有登录的人数
            int yesterdayHasLoginInTodaySum = 0;
            for (String s1 : yesterdayDateDataMapPlayerId.keySet()) {
                if (null != accountLogListCreateDatePlayerId.get(s1)) {
                    yesterdayHasLoginInTodaySum ++;
                }
            }
            if (0 == yesterdayHasLoginInTodaySum) {
                gamePlayMethodsTakePartInVO.setSecondGlanceRate(new BigDecimal(0));
            } else {
                gamePlayMethodsTakePartInVO.setSecondGlanceRate(BigDecimalUtil.divide(yesterdayHasPlayInTodaySum * 100, yesterdayHasLoginInTodaySum,2));
            }
            gamePlayMethodsTakePartInVOList.add(gamePlayMethodsTakePartInVO);
        }
        return gamePlayMethodsTakePartInVOList.stream().sorted((s1, s2) -> s2.getDate().compareTo(s1.getDate())).collect(Collectors.toList());
    }

}
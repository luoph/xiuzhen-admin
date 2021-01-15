package org.jeecg.modules.game.service.impl;

import cn.youai.xiuzhen.entity.pojo.PlayerLogType;
import cn.youai.xiuzhen.utils.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.game.constant.PracticeExp;
import org.jeecg.modules.game.entity.GameRankListVO;
import org.jeecg.modules.game.entity.LogAccount;
import org.jeecg.modules.game.entity.LogPlayer;
import org.jeecg.modules.game.entity.PlayerLogTypeTimes;
import org.jeecg.modules.game.mapper.GameRankListMapper;
import org.jeecg.modules.game.service.IGameRankListService;
import org.jeecg.modules.game.util.ParamValidUtil;
import org.jeecg.modules.player.entity.GameRegisterInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author huli
 * @Description: GameRankListServiceImpl
 */

@Slf4j
@Service
public class GameRankListServiceImpl implements IGameRankListService {

    @Resource
    GameRankListMapper gameRankListMapper;

    @Value("${app.log.db.table}")
    String logAcountTable;

    /**
     * 查询排行榜列表
     *
     * @param logPlayerTable
     * @param gameRankListVO
     * @return
     */
    @Override
    public List<GameRankListVO> selectGameRankList(String logPlayerTable, GameRankListVO gameRankListVO) throws UnsupportedEncodingException {
        Date createTime = DateUtils.endTimeOfDate(gameRankListVO.getDate());
        if (null != gameRankListVO.getHour() && gameRankListVO.getHour() >= 0) {
            createTime = ParamValidUtil.getHourEnd(createTime, gameRankListVO.getHour() - 1);
        }
        // 查询操作日志
        List<LogPlayer> logPlayerList0 = gameRankListMapper.selectLogPlayer(logPlayerTable, gameRankListVO.getRankListType(), createTime, gameRankListVO.getServerId());
        Date finalCreateTime = createTime;
        List<LogPlayer> logPlayerList = logPlayerList0.stream().filter(logPlayer -> logPlayer.getCreateTime().before(finalCreateTime)).collect(Collectors.toList());
        Map<Long, List<LogPlayer>> logPlayerListMapPlayerId = logPlayerList.stream().collect(Collectors.groupingBy(LogPlayer::getPlayerId));
        // 查询注册信息
        List<GameRegisterInfo> gameRegisterInfoList = gameRankListMapper.selectRegisterInfo();
        Map<Long, List<GameRegisterInfo>> gameRegisterInfoListMapPlayerId = gameRegisterInfoList.stream().collect(Collectors.groupingBy(GameRegisterInfo::getPlayerId));
        // 查询登录等级信息
        List<LogAccount> logAccountList = gameRankListMapper.selectLogAccount(logAcountTable);
        Map<Long, List<LogAccount>> logAccountListMapPlayerId = logAccountList.stream().collect(Collectors.groupingBy(LogAccount::getPlayerId));

        List<PlayerLogTypeTimes> playerLogTypeTimesList = new ArrayList<>();
        // 获取用户玩法次数或数值
        for (Long playerId : logPlayerListMapPlayerId.keySet()) {
            List<LogPlayer> onePlayerLogPlayer = logPlayerListMapPlayerId.get(playerId);
            PlayerLogTypeTimes playerLogTypeTimes = new PlayerLogTypeTimes()
                    .setPlayerId(playerId)
                    .setBehaviorType(gameRankListVO.getRankListType());
            getBehaviorTimes(playerLogTypeTimes, onePlayerLogPlayer);
            playerLogTypeTimesList.add(playerLogTypeTimes);
        }
        // 用户次数排序
        List<PlayerLogTypeTimes> playerLogTypeTimesListSort = playerLogTypeTimesList.stream().sorted((s1, s2) -> (int) (s2.getValues() - s1.getValues())).collect(Collectors.toList());
        List<GameRankListVO> gameRankListVOList = new ArrayList<>();
        for (int i = 0; i < playerLogTypeTimesListSort.size(); i++) {
            System.out.println(i);
            System.out.println(null != logAccountListMapPlayerId.get(playerLogTypeTimesListSort.get(i).getPlayerId()));
            long playerGrade = 0;
            if (null != logAccountListMapPlayerId.get(playerLogTypeTimesListSort.get(i).getPlayerId())) {
                playerGrade = logAccountListMapPlayerId.get(playerLogTypeTimesListSort.get(i).getPlayerId()).stream().map(LogAccount::getValue).max(Comparator.comparing(Long::longValue)).orElse(0L);
            }
            gameRankListVOList.add(new GameRankListVO()
                    .setRanking(i + 1)
                    .setPlayerId(playerLogTypeTimesListSort.get(i).getPlayerId())
                    .setPlayerName(null == gameRegisterInfoListMapPlayerId.get(playerLogTypeTimesListSort.get(i).getPlayerId()) ? "未注册用户" : gameRegisterInfoListMapPlayerId.get(playerLogTypeTimesListSort.get(i).getPlayerId()).get(0).getName())
                    .setRealm(new PracticeExp().getLevelToNameMap().get(playerGrade))
                    .setRankValues(playerLogTypeTimesListSort.get(i).getValues()));
        }
        return gameRankListVOList;
    }

    /**
     * 获取数据处理
     *
     * @param playerLogTypeTimes 需要查询的行为
     * @param logPlayerList      某用户的行为日志
     */
    private void getBehaviorTimes(PlayerLogTypeTimes playerLogTypeTimes, List<LogPlayer> logPlayerList) {
        if (PlayerLogType.isCount(playerLogTypeTimes.getBehaviorType())) {
            playerLogTypeTimes.setValues((long) logPlayerList.size());
        } else {
            long max = logPlayerList.stream().map(LogPlayer::getValue).max(Comparator.comparing(Long::longValue)).orElse(0L);
            playerLogTypeTimes.setValues(max);
        }
    }

}
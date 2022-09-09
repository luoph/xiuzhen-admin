package org.jeecg.modules.game.service.impl;

import cn.youai.server.utils.DateUtils;
import com.baomidou.dynamic.datasource.annotation.DS;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.game.constant.PracticeExp;
import org.jeecg.modules.game.entity.GameRankListVO;
import org.jeecg.modules.game.entity.LogAccount;
import org.jeecg.modules.game.entity.LogPlayer;
import org.jeecg.modules.game.mapper.GameRankListMapper;
import org.jeecg.modules.game.service.IGameRankListService;
import org.jeecg.modules.game.util.ParamValidUtil;
import org.jeecg.modules.player.entity.GameRegisterInfo;
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
@DS("shardingSphere")
public class GameRankListServiceImpl implements IGameRankListService {

    @Resource
    GameRankListMapper gameRankListMapper;

    /**
     * 查询排行榜列表
     *
     * @param gameRankListVO
     * @return
     */
    @Override
    public List<GameRankListVO> selectGameRankList(GameRankListVO gameRankListVO) throws UnsupportedEncodingException {
        Date createTime = DateUtils.endTimeOfDate(gameRankListVO.getDate());
        if (null != gameRankListVO.getHour() && gameRankListVO.getHour() >= 0) {
            createTime = ParamValidUtil.getHourEnd(createTime, gameRankListVO.getHour() - 1);
        }
        // 查询操作日志
        List<LogPlayer> logPlayerValueList;
        if (100 > gameRankListVO.getRankListType()) {
            logPlayerValueList = gameRankListMapper.selectSum(gameRankListVO.getRankListType(), createTime, gameRankListVO.getServerId());
        } else {
            logPlayerValueList = gameRankListMapper.selectCount(gameRankListVO.getRankListType(), createTime, gameRankListVO.getServerId());
        }
        // 查询注册信息
        List<GameRegisterInfo> gameRegisterInfoList = gameRankListMapper.selectRegisterInfo();
        Map<Long, List<GameRegisterInfo>> gameRegisterInfoListMapPlayerId = gameRegisterInfoList.stream().collect(Collectors.groupingBy(GameRegisterInfo::getPlayerId));
        // 查询登录等级信息
        List<LogAccount> logAccountList = gameRankListMapper.selectLogAccount();
        Map<Long, List<LogAccount>> logAccountListMapPlayerId = logAccountList.stream().collect(Collectors.groupingBy(LogAccount::getPlayerId));

        List<GameRankListVO> gameRankListVOList = new ArrayList<>();
        for (int i = 0; i < logPlayerValueList.size(); i++) {
            long playerGrade = logAccountListMapPlayerId.get(logPlayerValueList.get(i).getPlayerId()).stream().map(LogAccount::getValue).max(Comparator.comparing(Long::longValue)).orElse(0L);
            gameRankListVOList.add(new GameRankListVO()
                    .setRanking(i + 1)
                    .setPlayerId(logPlayerValueList.get(i).getPlayerId())
                    .setPlayerName(null == gameRegisterInfoListMapPlayerId.get(logPlayerValueList.get(i).getPlayerId()) ? "未注册用户" : gameRegisterInfoListMapPlayerId.get(logPlayerValueList.get(i).getPlayerId()).get(0).getName())
                    .setRealm(new PracticeExp().getLevelToNameMap().get(playerGrade))
                    .setRankValues(logPlayerValueList.get(i).getValue()));
        }
        return gameRankListVOList;
    }

}
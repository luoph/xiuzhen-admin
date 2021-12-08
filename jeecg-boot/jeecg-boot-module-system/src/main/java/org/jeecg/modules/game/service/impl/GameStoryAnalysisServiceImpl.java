package org.jeecg.modules.game.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.youai.xiuzhen.entity.pojo.ConfMainStory;
import cn.youai.xiuzhen.utils.BigDecimalUtil;
import cn.youai.server.utils.DateUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.common.util.CollectionPageHelp;
import org.jeecg.database.DataSourceHelper;
import org.jeecg.modules.game.entity.GameStoryAnalysis;
import org.jeecg.modules.game.entity.GameStoryAnalysisVO;
import org.jeecg.modules.game.mapper.GameStoryAnalysisMapper;
import org.jeecg.modules.game.service.IGameStoryAnalysisService;
import org.jeecg.modules.player.service.ILogAccountService;
import org.jeecg.modules.utils.GameConfigUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 剧情分析
 * @date 2021-04-12
 */
@Service
public class GameStoryAnalysisServiceImpl extends ServiceImpl<GameStoryAnalysisMapper, GameStoryAnalysis> implements IGameStoryAnalysisService {

    @Autowired
    private ILogAccountService logAccountService;

    @Override
    public IPage<GameStoryAnalysisVO> queryGameStoryAnalysisList(GameStoryAnalysisVO gameStoryAnalysis, int pageSize, int pageNo) {
        List<GameStoryAnalysis> list = null;
        try {
            DataSourceHelper.useServerDatabase(gameStoryAnalysis.getServerId());
            list = playerReachLevel(gameStoryAnalysis);
        } catch (Exception e) {
            log.error("通过服务器id:" + gameStoryAnalysis.getServerId() + ",切换数据源异常", e);
            return null;
        } finally {
            DataSourceHelper.useDefaultDatabase();
        }

        if (CollUtil.isEmpty(list)) {
            return null;
        }

        //各玩家最大到达关卡Map
        Map<Long, Integer> maxMinorLevelByPlayerMap = list.stream().collect(Collectors.toMap(GameStoryAnalysis::getPlayerId, GameStoryAnalysis::getMinorLevel));

        List<Integer> minorLevelList = list.stream().map(GameStoryAnalysis::getMinorLevel).distinct().collect(Collectors.toList());

        if (CollUtil.isNotEmpty(minorLevelList)) {
            Collections.sort(minorLevelList);
            List<Integer> levels = CollectionPageHelp.pageBySubList(minorLevelList, pageSize, pageNo);
            if (CollectionUtil.isEmpty(levels)) {
                return null;
            }
            List<ConfMainStory> confMainStories = GameConfigUtils.getConfMainStoryList(levels);
            if (CollectionUtil.isEmpty(confMainStories)) {
                return null;
            }

            List<GameStoryAnalysisVO> result = genGameStoryAnalysisList(gameStoryAnalysis, confMainStories, maxMinorLevelByPlayerMap);
            IPage<GameStoryAnalysisVO> iPage = new Page<>(pageNo, pageSize, minorLevelList.size());
            iPage.setRecords(result);
            return iPage;
        }
        return null;
    }

    /**
     * 计算剧情分布列表各字段逻辑
     *
     * @param gameStoryAnalysis          查询条件
     * @param confMainStories            关卡
     * @param stayingOnPlayerMaxLevelMap 各玩家停留的关卡Map
     * @return
     */
    private List<GameStoryAnalysisVO> genGameStoryAnalysisList(GameStoryAnalysisVO gameStoryAnalysis,
                                                               List<ConfMainStory> confMainStories,
                                                               Map<Long, Integer> stayingOnPlayerMaxLevelMap) {

        // 当天活跃用户
        List<Long> playerIdsByLoginDate = logAccountService.getPlayerIdsByLoginDate(gameStoryAnalysis.getServerId(),
                gameStoryAnalysis.getAnalysisDate());
        // 3天内没登陆用户
        List<Long> playerIdsByNoLoginRangeDate = logAccountService.getPlayerIdsByNoLoginRangeDate(
                gameStoryAnalysis.getServerId(), gameStoryAnalysis.getAnalysisDate(), 3);

        // 统计停留活跃人数
        Map<Integer, Integer> countLiveMap = new HashMap<>(playerIdsByLoginDate.size());
        for (Long playerId : playerIdsByLoginDate) {
            Integer minorLeve = stayingOnPlayerMaxLevelMap.get(playerId);
            if (minorLeve != null) {
                Integer num = countLiveMap.get(minorLeve);
                if (num != null) {
                    num += 1;
                } else {
                    num = 1;
                }
                countLiveMap.put(minorLeve, num);
            }
        }
        // 总停留活跃人数
        long totalStayLiveNum = countLiveMap.values().stream().collect(Collectors.summarizingInt(x -> x.intValue())).getSum();

        // 统计停留流失人数
        Map<Integer, Integer> countLeaveMap = new HashMap<>(playerIdsByNoLoginRangeDate.size());
        for (Long playerId : playerIdsByNoLoginRangeDate) {
            Integer minorLeve = stayingOnPlayerMaxLevelMap.get(playerId);
            if (minorLeve != null) {
                Integer num = countLeaveMap.get(minorLeve);
                if (num != null) {
                    num += 1;
                } else {
                    num = 1;
                }
                countLeaveMap.put(minorLeve, num);
            }
        }
        // 总停留流失人数
        long totalStayLeaveNum = countLeaveMap.values().stream().collect(Collectors.summarizingInt(x -> x.intValue())).getSum();

        List<GameStoryAnalysisVO> result = new ArrayList<>(confMainStories.size());

        for (ConfMainStory confMainStory : confMainStories) {
            GameStoryAnalysisVO gameStoryAnalysisVO = new GameStoryAnalysisVO(confMainStory.getLevel(), confMainStory.getChapterNum());
            // 关卡名称
            gameStoryAnalysisVO.setStoryCheckpoint(confMainStory.getChapterNum());
            // 停留活跃人数
            gameStoryAnalysisVO.setStayLiveNum(countLiveMap.get(confMainStory.getLevel()) != null ? countLiveMap.get(confMainStory.getLevel()) : 0);
            // 停留流失人数
            gameStoryAnalysisVO.setStayLeaveNum(countLeaveMap.get(confMainStory.getLevel()) != null ? countLeaveMap.get(confMainStory.getLevel()) : 0);
            // 总达成人数
            gameStoryAnalysisVO.setTotalArriveNum(computeTotalConcludeNum(confMainStory.getLevel(), stayingOnPlayerMaxLevelMap));
            // 总停留人数
            gameStoryAnalysisVO.setTotalStayNum(computeTotalRemainNum(confMainStory.getLevel(), stayingOnPlayerMaxLevelMap));

            result.add(gameStoryAnalysisVO);
        }

        for (GameStoryAnalysisVO vo : result) {
            // 活跃占比
            if (totalStayLiveNum != 0 && vo.getStayLiveNum() != null) {
                vo.setLiveRate(BigDecimalUtil.dividePercent(BigDecimalUtil.divide((double) vo.getStayLiveNum(), totalStayLiveNum, 4).doubleValue()).toString());
            } else {
                vo.setLiveRate("0");
            }
            // 流失占比
            if (totalStayLeaveNum != 0 && vo.getStayLeaveNum() != null) {
                vo.setLeaveRate(BigDecimalUtil.dividePercent(BigDecimalUtil.divide((double) vo.getStayLeaveNum(), totalStayLeaveNum, 4).doubleValue()).toString());
            } else {
                vo.setLeaveRate("0");
            }
            // 关卡滞留率
            if (vo.getTotalArriveNum() != null && vo.getTotalArriveNum() != 0 && vo.getTotalStayNum() != null) {
                vo.setCheckpointStayRate(BigDecimalUtil.dividePercent(BigDecimalUtil.divide((double) vo.getTotalStayNum(), (double) vo.getTotalArriveNum(), 4).doubleValue()).toString());
            } else {
                vo.setCheckpointStayRate("0");
            }
        }

        return result;
    }

    /**
     * 指定日期内，各玩家到达关卡的情况
     *
     * @param gameStoryAnalysis 指定日期
     * @return
     */
    private List<GameStoryAnalysis> playerReachLevel(GameStoryAnalysisVO gameStoryAnalysis) {
        QueryWrapper<GameStoryAnalysis> query = Wrappers.query();
        query.select("player_id", "max(minor_level) as minor_level");
        query.le("create_time", DateUtils.endTimeOfDate(gameStoryAnalysis.getAnalysisDate()));
        query.groupBy("player_id");
        return this.list(query);
    }

    /**
     * 计算总达成人数
     *
     * @param level                      关卡
     * @param stayingOnPlayerMaxLevelMap 玩家停留的最大关卡
     * @return 总达成人数
     */
    private int computeTotalConcludeNum(int level, Map<Long, Integer> stayingOnPlayerMaxLevelMap) {
        int totalConcludeNum = 0;

        for (Map.Entry<Long, Integer> entry : stayingOnPlayerMaxLevelMap.entrySet()) {
            if (entry.getValue() != null && entry.getValue() >= level) {
                totalConcludeNum++;
            }
        }
        return totalConcludeNum;
    }

    /**
     * 计算总停留人数
     *
     * @param level                      关卡
     * @param stayingOnPlayerMaxLevelMap 玩家停留的最大关卡
     * @return 总停留人数
     */
    private int computeTotalRemainNum(int level, Map<Long, Integer> stayingOnPlayerMaxLevelMap) {
        int totalRemainNum = 0;

        for (Map.Entry<Long, Integer> entry : stayingOnPlayerMaxLevelMap.entrySet()) {
            if (entry.getValue() != null && entry.getValue().equals(level)) {
                totalRemainNum++;
            }
        }
        return totalRemainNum;
    }
}

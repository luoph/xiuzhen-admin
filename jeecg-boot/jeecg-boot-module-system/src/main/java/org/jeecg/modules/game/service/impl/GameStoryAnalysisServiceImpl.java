package org.jeecg.modules.game.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.youai.xiuzhen.common.data.ConfigDataEnum;
import cn.youai.xiuzhen.common.data.ConfigDataService;
import cn.youai.xiuzhen.entity.pojo.ConfMainStory;
import cn.youai.xiuzhen.utils.DateUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.googlecode.cqengine.query.Query;
import com.googlecode.cqengine.query.QueryFactory;
import com.googlecode.cqengine.query.option.QueryOptions;
import org.jeecg.common.util.CollectionPageHelp;
import org.jeecg.database.DataSourceHelper;
import org.jeecg.modules.game.entity.GameStoryAnalysis;
import org.jeecg.modules.game.entity.GameStoryAnalysisVO;
import org.jeecg.modules.game.mapper.GameStoryAnalysisMapper;
import org.jeecg.modules.game.service.IGameStoryAnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.DecimalFormat;
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

	@Resource
	private GameStoryAnalysisMapper gameStoryAnalysisMapper;

	@Autowired
	private ConfigDataService configDataService;

	@Override
	public IPage<GameStoryAnalysisVO> queryGameStoryAnalysisList(GameStoryAnalysisVO gameStoryAnalysis, int pageSize, int pageNo) {

		List<GameStoryAnalysis> datalist = null;
		List<GameStoryAnalysisVO> list = null;
		List<GameStoryAnalysisVO> resultPage = null;
		List<Integer> levelIds = null;

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(gameStoryAnalysis.getAnalysisDate());
		calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) - 3);

		try {
			DataSourceHelper.useServerDatabase(gameStoryAnalysis.getServerId());
			list = gameStoryAnalysisMapper.queryGameStoryAnalysis(DateUtils.startTimeOfDate(gameStoryAnalysis.getAnalysisDate()),
					DateUtils.endTimeOfDate(gameStoryAnalysis.getAnalysisDate()), calendar.getTime());
			resultPage = CollectionPageHelp.pageBySubList(list, pageSize, pageNo);
			levelIds = resultPage.stream().map(GameStoryAnalysisVO::getMinorLevel).collect(Collectors.toList());

			LambdaQueryWrapper<GameStoryAnalysis> queryWrapper = Wrappers.lambdaQuery();
			queryWrapper.select(GameStoryAnalysis::getPlayerId, GameStoryAnalysis::getMinorLevel)
					.in(GameStoryAnalysis::getMinorLevel, levelIds);
			datalist = this.list(queryWrapper);
		} catch (Exception e) {
			log.error("通过服务器id:" + gameStoryAnalysis.getServerId() + ",切换数据源异常", e);
		} finally {
			DataSourceHelper.useDefaultDatabase();
		}

		if (!CollUtil.isEmpty(datalist)) {
			List<ConfMainStory> confMainStories = queryConfMainStory(levelIds);
			if (CollectionUtil.isEmpty(confMainStories)) {
				return null;
			}

			List<GameStoryAnalysisVO> result = genGameStoryAnalysisList(confMainStories, resultPage, datalist);
			IPage<GameStoryAnalysisVO> iPage = new Page<>(pageNo, pageSize, list.size());
			iPage.setRecords(result);
			return iPage;
		}
		return null;
	}

	/**
	 * 计算剧情分布列表各字段逻辑
	 *
	 * @param confMainStories 分页后的关卡list
	 * @param resultPage      GameStoryAnalysisVOList
	 * @param datalist        数据库结果集
	 */
	private List<GameStoryAnalysisVO> genGameStoryAnalysisList(List<ConfMainStory> confMainStories, List<GameStoryAnalysisVO> resultPage, List<GameStoryAnalysis> datalist) {
		// 总停留活跃人数
		int totalStayLiveNum = 0;

		// 总停留流失人数
		int totalStayLeaveNum = 0;

		HashMap<Integer, GameStoryAnalysisVO> gameStoryAnalysisVOMapByLevel = new HashMap<>();
		for (GameStoryAnalysisVO gameStoryAnalysisVO : resultPage) {
			gameStoryAnalysisVOMapByLevel.put(gameStoryAnalysisVO.getMinorLevel(), gameStoryAnalysisVO);
			totalStayLiveNum += gameStoryAnalysisVO.getStayLiveNum();
			totalStayLeaveNum += gameStoryAnalysisVO.getStayLeaveNum();
		}

		List<GameStoryAnalysisVO> result = new ArrayList<>(confMainStories.size());

		// 总停留人数Map<玩家，最大关卡>
		Map<Long, Integer> stayingOnPlayerMaxLevelMap = getStayingOnPlayerMaxLevelMap(datalist);


		for (ConfMainStory confMainStory : confMainStories) {
			GameStoryAnalysisVO gameStoryAnalysisVO = gameStoryAnalysisVOMapByLevel.get(confMainStory.getLevel());
			if (gameStoryAnalysisVO != null) { // 有数据
				// 关卡名称
				gameStoryAnalysisVO.setStoryCheckpoint(confMainStory.getChapterNum());
				// 总达成人数
				gameStoryAnalysisVO.setTotalArriveNum(computeTotalConcludeNum(confMainStory.getLevel(), stayingOnPlayerMaxLevelMap));
				// 总停留人数
				gameStoryAnalysisVO.setTotalStayNum(computeTotalRemainNum(confMainStory.getLevel(), stayingOnPlayerMaxLevelMap));

				totalStayLiveNum += gameStoryAnalysisVO.getStayLiveNum();
				totalStayLeaveNum += gameStoryAnalysisVO.getStayLeaveNum();
			} else {
				gameStoryAnalysisVO = new GameStoryAnalysisVO(confMainStory.getLevel(), confMainStory.getChapterNum());
			}
			result.add(gameStoryAnalysisVO);
		}

		DecimalFormat df = new DecimalFormat("##0.00");
		for (GameStoryAnalysisVO vo : resultPage) {
			// 活跃占比
			if (totalStayLiveNum != 0 && vo.getStayLiveNum() != null && vo.getStayLiveNum() != 0) {
				vo.setLiveRate(df.format((vo.getStayLiveNum() / (double) totalStayLiveNum) * 100));
			}
			// 流失占比
			if (totalStayLeaveNum != 0 && vo.getStayLeaveNum() != null && vo.getStayLeaveNum() != 0) {
				vo.setLeaveRate(df.format((vo.getStayLeaveNum() / (double) totalStayLeaveNum) * 100));
			}
			// 关卡滞留率
			if (vo.getTotalArriveNum() != null && vo.getTotalArriveNum() != 0) {
				vo.setCheckpointStayRate(df.format((vo.getTotalStayNum() / (double) vo.getTotalArriveNum()) * 100));
			}
		}

		return result;
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

	/**
	 * 玩家停留的最大关卡
	 *
	 * @param datalist 关卡信息
	 * @return stayingOnPlayerMaxLevelMap<玩家ID ， max关卡ID>
	 */
	private Map<Long, Integer> getStayingOnPlayerMaxLevelMap(List<GameStoryAnalysis> datalist) {
		Map<Long, Integer> stayingOnPlayerMaxLevelMap = new HashMap(datalist.size());
		datalist.forEach(e -> {
			Integer level = stayingOnPlayerMaxLevelMap.get(e.getPlayerId());
			if (level != null) {
				if (e.getMinorLevel() > level) {
					stayingOnPlayerMaxLevelMap.put(e.getPlayerId(), e.getMinorLevel());
				}
			} else {
				stayingOnPlayerMaxLevelMap.put(e.getPlayerId(), e.getMinorLevel());
			}
		});

		return stayingOnPlayerMaxLevelMap;
	}

	@Override
	public List<ConfMainStory> queryConfMainStory(List<Integer> levelIds) {
		QueryOptions queryOptions = QueryFactory.queryOptions(QueryFactory.orderBy(QueryFactory.ascending(ConfMainStory.LEVEL)));
		Query<ConfMainStory> in = QueryFactory.in(ConfMainStory.LEVEL, levelIds);
		return configDataService.selectList(ConfigDataEnum.MAIN_STORY, ConfMainStory.class, in, queryOptions);
	}
}

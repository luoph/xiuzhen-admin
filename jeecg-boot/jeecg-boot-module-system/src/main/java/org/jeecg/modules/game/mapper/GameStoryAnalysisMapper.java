package org.jeecg.modules.game.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.game.entity.GameStoryAnalysis;
import org.jeecg.modules.game.entity.GameStoryAnalysisVO;

import java.util.Date;
import java.util.List;

/**
 * @author lijunchi
 * @version V1.0
 * @description main_story_level
 * @date 2021-04-09
 */
public interface GameStoryAnalysisMapper extends BaseMapper<GameStoryAnalysis> {

	/**
	 * 返回 minorLevel 关卡id
	 * stayLiveNum 停留活跃人数
	 * stayLeaveNum 停留流失人数
	 *
	 * @param startDate             开始日期
	 * @param endDate               结束日期
	 * @param daysAgoDate           3天前的日期
	 * @return
	 */
	List<GameStoryAnalysisVO> queryGameStoryAnalysis(@Param("startDate") Date startDate,
													 @Param("endDate") Date endDate,
													 @Param("3DaysAgoDate") Date daysAgoDate);
}

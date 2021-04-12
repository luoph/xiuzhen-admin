package org.jeecg.modules.game.service;


import cn.youai.xiuzhen.entity.pojo.ConfMainStory;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.game.entity.GameStoryAnalysis;
import org.jeecg.modules.game.entity.GameStoryAnalysisVO;

import java.util.List;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 剧情分析
 * @date 2021-03-26
 */
public interface IGameStoryAnalysisService extends IService<GameStoryAnalysis> {

	/**
	 * 查询剧情列表
	 *
	 * @param gameStoryAnalysis
	 * @param pageSize
	 * @param pageNo
	 * @return
	 */
	IPage<GameStoryAnalysisVO> queryGameStoryAnalysisList(GameStoryAnalysisVO gameStoryAnalysis, int pageSize, int pageNo);

	/**
	 * 查询主剧情关卡
	 *
	 * @param levelIds
	 * @return
	 */
	List<ConfMainStory> queryConfMainStory(List<Integer> levelIds);

}

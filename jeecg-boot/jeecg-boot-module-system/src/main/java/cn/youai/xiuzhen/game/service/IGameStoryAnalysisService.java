package cn.youai.xiuzhen.game.service;


import cn.youai.xiuzhen.game.entity.GameStoryAnalysis;
import cn.youai.xiuzhen.game.entity.GameStoryAnalysisVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

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
}

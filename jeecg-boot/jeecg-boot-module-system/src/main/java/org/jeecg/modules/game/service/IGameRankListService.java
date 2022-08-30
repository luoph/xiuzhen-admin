package org.jeecg.modules.game.service;

import org.jeecg.modules.game.entity.GameRankListVO;

import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * @author huli
 * @Description: IGameRankListService
 */

public interface IGameRankListService {

    /**
     * 查询排行榜列表
     *
     * @param logPlayerTable
     * @param gameRankListVO
     * @return
     */
    List<GameRankListVO> selectGameRankList(String logPlayerTable,
                                            GameRankListVO gameRankListVO) throws UnsupportedEncodingException;
}
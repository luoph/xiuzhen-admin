package cn.youai.xiuzhen.stat.service;

import cn.youai.xiuzhen.stat.entity.GameRankListVO;

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
     * @param gameRankListVO
     * @return
     */
    List<GameRankListVO> selectGameRankList(GameRankListVO gameRankListVO) throws UnsupportedEncodingException;
}
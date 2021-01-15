package org.jeecg.modules.game.service;

import org.apache.ibatis.annotations.*;
import org.jeecg.modules.game.entity.GameRankListVO;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author huli
 * @Description: IGameRankListService
 */

public interface IGameRankListService {

    /**
     * 查询排行榜列表
     * @param logPlayerTable
     * @param gameRankListVO
     * @return
     */
    List<GameRankListVO> selectGameRankList(String logPlayerTable,
                                            GameRankListVO gameRankListVO) throws UnsupportedEncodingException;
}
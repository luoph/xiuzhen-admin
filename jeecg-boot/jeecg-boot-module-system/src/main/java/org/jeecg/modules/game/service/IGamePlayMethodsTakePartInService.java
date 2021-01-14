package org.jeecg.modules.game.service;

import org.jeecg.modules.game.entity.GamePlayMethodsTakePartInVO;

import java.util.Date;
import java.util.List;

/**
 * @author huli
 * @Description: IGamePlayMethodsTakePartInService
 * @date 2021/1/11 14:45
 */

public interface IGamePlayMethodsTakePartInService {
    /**
     * 玩法参与查询
     * @param times 玩法对应满次数
     * @param grade 玩法对应等级
     * @param playMethodsType 玩法
     * @param createDateBegin 开始时间
     * @param createDateEnd 结束时间
     * @param serverId 服务器id
     * @return List<GamePlayMethodsTakePartInVO>
     */
    List<GamePlayMethodsTakePartInVO> playMethodsTakePartList(int times, int grade, String playMethodsType,
                                                              Date createDateBegin, Date createDateEnd, int serverId);
}
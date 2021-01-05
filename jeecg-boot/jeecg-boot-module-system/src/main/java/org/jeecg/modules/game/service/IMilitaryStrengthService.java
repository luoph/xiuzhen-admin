package org.jeecg.modules.game.service;

import org.apache.ibatis.annotations.*;
import org.jeecg.modules.game.entity.MilitaryStrengthVO;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author huli
 * @Description: IMilitaryStrengthService
 * @date 2021/1/4 11:53
 */

public interface IMilitaryStrengthService {
    /**
     * 获取渡劫战力列表
     * @return
     */
    List<MilitaryStrengthVO> getMilitaryStrengVoDujieList(int serverId, String createDateBegin, String createDateEnd, String channel);
    /**
     * 获取所有战力变化列表
     */
    List<MilitaryStrengthVO> getMilitaryStrengVoAllList(String userName, int serverId, String createDateBegin, String createDateEnd, String channel);
}
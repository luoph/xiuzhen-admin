/*
 * create by mybatis-plus-generator  https://github.com/xiweile
 */
package org.jeecg.modules.game.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.game.constant.RoleType;
import org.jeecg.modules.game.entity.GameStatRemainDetail;

import java.util.Collection;
import java.util.Date;

/**
 * <p>
 * 留存统计 服务类
 * </p>
 *
 * @author buliangliang
 * @since 2021-12-22
 */
public interface IGameStatRemainDetailService extends IService<GameStatRemainDetail> {

    void calcRemainDetailStat(RoleType roleType, Collection<Integer> serverIds,
                              Date registerDate, int days, boolean updateAll);
}

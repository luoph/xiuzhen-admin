/*
 * create by mybatis-plus-generator  https://github.com/xiweile
 */
package org.jeecg.modules.game.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.game.constant.RoleType;
import org.jeecg.modules.game.entity.GameStatRemain;

import java.util.Collection;
import java.util.Date;

/**
 * <p>
 * 留存统计
 * </p>
 *
 * @author luopeihuan
 * @since 2020-08-26
 */
public interface IGameStatRemainService extends IService<GameStatRemain> {

    void calcRemainStat(RoleType roleType, Collection<Integer> serverIds,
                        Date registerDate, int days, boolean updateAll);

}

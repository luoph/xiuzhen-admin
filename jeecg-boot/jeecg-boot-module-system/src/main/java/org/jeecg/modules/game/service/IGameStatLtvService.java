/*
 * create by mybatis-plus-generator  https://github.com/xiweile
 */
package org.jeecg.modules.game.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.game.entity.GameStatLtv;

import java.util.Collection;
import java.util.Date;

/**
 * <p>
 * LTV统计 服务类
 * </p>
 *
 * @author buliangliang
 * @since 2020-08-26
 */
public interface IGameStatLtvService extends IService<GameStatLtv> {

    void calcLtvStat(Collection<Integer> serverIds, Date registerDate, int days, boolean updateAll);

}

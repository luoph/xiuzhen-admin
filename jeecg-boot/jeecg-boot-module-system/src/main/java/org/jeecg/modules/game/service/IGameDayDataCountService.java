/*
 * create by mybatis-plus-generator  https://github.com/xiweile
 */
package org.jeecg.modules.game.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.game.entity.GameStatDaily;

import java.util.Collection;
import java.util.Date;

/**
 * <p>
 * 每日数据统计 服务类
 * </p>
 *
 * @author buliangliang
 * @since 2020-08-22
 */
public interface IGameDayDataCountService extends IService<GameStatDaily> {

    void calcDailyStat(Collection<Integer> serverIds, Date date);

    GameStatDaily gameDataCount(int serverId, String date);
}

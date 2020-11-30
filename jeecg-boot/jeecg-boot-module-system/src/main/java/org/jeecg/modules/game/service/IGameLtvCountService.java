/*
 * create by mybatis-plus-generator  https://github.com/xiweile
 */
package org.jeecg.modules.game.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.game.entity.GameStatLtvCount;

/**
 * <p>
 * LTV统计 服务类
 * </p>
 *
 * @author buliangliang
 * @since 2020-08-26
 */
public interface IGameLtvCountService extends IService<GameStatLtvCount> {
    /**
     * 留存统计
     *
     * @param page
     * @param channelId
     * @param serverId
     * @param rangeDateBegin
     * @param rangeDateEnd
     * @return
     */
    IPage<GameStatLtvCount> selectList(Page<GameStatLtvCount> page, int channelId, int serverId, String rangeDateBegin, String rangeDateEnd);

    /**
     * 统计留存
     *
     * @param channel
     * @param serverId
     * @param date
     * @param logTable
     * @return
     */
    GameStatLtvCount getGameLtvCount(String channel, int serverId, String date, String logTable);
}

/*
 * create by mybatis-plus-generator  https://github.com/xiweile
 */
package org.jeecg.modules.game.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.game.entity.GameStatRemain;

import java.util.Date;

/**
 * <p>
 * 留存统计 服务类
 * </p>
 *
 * @author buliangliang
 * @since 2020-08-26
 */
public interface IGameDataRemainService extends IService<GameStatRemain> {
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
    IPage<GameStatRemain> selectList(Page<GameStatRemain> page, int channelId, int serverId, String rangeDateBegin, String rangeDateEnd);

    /**
     * 留存统计
     *
     * @param channel
     * @param serverId
     * @param date
     * @param statDate 指定统计日期
     * @return
     */
    GameStatRemain getCountRemain(String channel, int serverId, String date, Date statDate);
}

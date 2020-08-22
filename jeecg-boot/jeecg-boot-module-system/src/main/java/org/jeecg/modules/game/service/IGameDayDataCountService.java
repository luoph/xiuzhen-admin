/*
 * create by mybatis-plus-generator  https://github.com/xiweile
 */
package org.jeecg.modules.game.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.modules.game.entity.GameDayDataCount;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 每日数据统计 服务类
 * </p>
 *
 * @author buliangliang
 * @since 2020-08-22
 */
public interface IGameDayDataCountService extends IService<GameDayDataCount> {
    /**
     * 分页查询
     *
     * @param page
     * @param channelId
     * @param serverId
     * @param rangeDateBegin
     * @param rangeDateEnd
     * @return
     */
    IPage<GameDayDataCount> selectList(Page<GameDayDataCount> page, int channelId, int serverId, String rangeDateBegin, String rangeDateEnd);
}

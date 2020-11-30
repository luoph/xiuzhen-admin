/*
 * create by mybatis-plus-generator  https://github.com/xiweile
 */
package org.jeecg.modules.game.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.game.entity.GameStatCountOngoing;

/**
 * <p>
 * 30天留存和ltv统计 服务类
 * </p>
 *
 * @author buliangliang
 * @since 2020-09-12
 */
public interface IGameCountOngoingService extends IService<GameStatCountOngoing> {
    /**
     * 查询三十天统计
     *
     * @param page
     * @param channelId
     * @param serverId
     * @param rangeDateBegin
     * @param rangeDateEnd
     * @param type           1-留存 2-ltv
     * @return
     */
    IPage<GameStatCountOngoing> selectList(Page<GameStatCountOngoing> page, int channelId, int serverId, int type, String rangeDateBegin, String rangeDateEnd);
}

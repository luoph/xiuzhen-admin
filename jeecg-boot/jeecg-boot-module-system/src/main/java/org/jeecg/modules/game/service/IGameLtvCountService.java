/*
 * create by mybatis-plus-generator  https://github.com/xiweile
 */
package org.jeecg.modules.game.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.game.entity.GameStatLtv;

import java.util.Date;

/**
 * <p>
 * LTV统计 服务类
 * </p>
 *
 * @author buliangliang
 * @since 2020-08-26
 */
public interface IGameLtvCountService extends IService<GameStatLtv> {
    /**
     * 留存统计
     */
    IPage<GameStatLtv> selectList(Page<GameStatLtv> page, int serverId, String rangeDateBegin, String rangeDateEnd);

    /**
     * 统计留存
     */
    GameStatLtv getGameLtvCount(int serverId, String date, Date statDate, String logTable);
}

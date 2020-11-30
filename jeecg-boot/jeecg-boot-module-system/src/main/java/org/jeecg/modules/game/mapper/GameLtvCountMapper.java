/*
 * create by mybatis-plus-generator  https://github.com/xiweile
 */
package org.jeecg.modules.game.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.game.entity.GameStatLtv;

import java.util.List;

/**
 * <p>
 * LTV统计 Mapper 接口
 * </p>
 *
 * @author buliangliang
 * @since 2020-08-26
 */
public interface GameLtvCountMapper extends BaseMapper<GameStatLtv> {
    /**
     * 统计ltv
     *
     * @param channel
     * @param serverId
     * @param date
     * @param logTable
     * @return
     */
    GameStatLtv getGameLtvCount(@Param("channel") String channel, @Param("serverId") int serverId, @Param("date") String date, @Param("logTable") String logTable);

    /**
     * 插入或更新
     *
     * @param list
     * @return
     */
    int updateOrInsert(List<GameStatLtv> list);

    /**
     * 查询留存
     *
     * @param channel
     * @param serverId
     * @param date
     * @param leftDays
     * @return
     */
    double selectLtv(@Param("channel") String channel, @Param("serverId") int serverId, @Param("date") String date, @Param("leftDays") int leftDays);
}

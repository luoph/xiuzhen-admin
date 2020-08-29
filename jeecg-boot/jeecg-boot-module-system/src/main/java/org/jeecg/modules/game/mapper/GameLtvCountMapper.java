/*
 * create by mybatis-plus-generator  https://github.com/xiweile
 */
package org.jeecg.modules.game.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.game.entity.GameLtvCount;

import java.util.List;

/**
 * <p>
 * LTV统计 Mapper 接口
 * </p>
 *
 * @author buliangliang
 * @since 2020-08-26
 */
public interface GameLtvCountMapper extends BaseMapper<GameLtvCount> {
    /**
     * 统计ltv
     *
     * @param channel
     * @param serverId
     * @param date
     * @param logTable
     * @return
     */
    GameLtvCount getGameLtvCount(@Param("channel") String channel, @Param("serverId") int serverId, @Param("date") String date, @Param("logTable") String logTable);

    int updateOrInsert(List<GameLtvCount> list);
}

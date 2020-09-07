/*
 * create by mybatis-plus-generator  https://github.com/xiweile
 */
package org.jeecg.modules.game.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.game.entity.GameDataRemain;

import java.util.List;

/**
 * <p>
 * 留存统计 Mapper 接口
 * </p>
 *
 * @author buliangliang
 * @since 2020-08-26
 */
public interface GameDataRemainMapper extends BaseMapper<GameDataRemain> {
    /**
     * 留存统计
     *
     * @param channel
     * @param serverId
     * @param date
     * @param logTable
     * @return
     */
    GameDataRemain gameRemainCount(@Param("channel") String channel, @Param("serverId") int serverId, @Param("date") String date, @Param("logTable") String logTable);


    /**
     * 批量插入更新
     *
     * @param list
     * @return
     */
    int updateOrInsert(List<GameDataRemain> list);

    /**
     * 从开服起统计留存
     *
     * @param channel
     * @param serverId
     * @param date
     * @param logTable
     * @param leftDay
     * @return
     */
    int selectRemain(@Param("channel") String channel, @Param("serverId") int serverId, @Param("date") String date, @Param("logTable") String logTable, @Param("leftDay") int leftDay);

}

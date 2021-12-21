/*
 * create by mybatis-plus-generator  https://github.com/xiweile
 */
package org.jeecg.modules.game.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.game.entity.GameStatRemain;
import org.jeecg.modules.game.entity.ServerRemain;

/**
 * <p>
 * LTV统计 Mapper 接口
 * </p>
 *
 * @author buliangliang
 * @since 2020-08-26
 */
public interface GameStatRemainMapper extends BaseMapper<GameStatRemain> {
    /**
     * 统计ltv
     */
    GameStatRemain getGameStatRemain(@Param("serverId") int serverId,
                                     @Param("registerDate") String registerDate);

    /**
     * 付费次留
     */
    Integer getPayRemain(@Param("serverId") int serverId,
                         @Param("registerDate") String registerDate,
                         @Param("logDb") String logDb);

    /**
     * 免费次留
     */
    Integer getFreeRemain(@Param("serverId") int serverId,
                          @Param("registerDate") String registerDate,
                          @Param("logDb") String logDb);

    /**
     * 查询留存
     */
    ServerRemain selectRemain(@Param("serverId") int serverId,
                              @Param("registerDate") String registerDate,
                              @Param("days") int days,
                              @Param("logDb") String logDb);

}

/*
 * create by mybatis-plus-generator  https://github.com/xiweile
 */
package org.jeecg.modules.game.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.game.entity.GameStatRemainDetail;
import org.jeecg.modules.game.entity.ServerRemain;

/**
 * <p>
 * 留存统计 Mapper 接口
 * </p>
 *
 * @author buliangliang
 * @since 2021-12-22
 */
public interface GameStatRemainDetailMapper extends BaseMapper<GameStatRemainDetail> {

    /**
     * 统计ltv
     */
    GameStatRemainDetail getGameStatRemainDetail(@Param("roleType") int roleType,
                                                 @Param("serverId") int serverId,
                                                 @Param("registerDate") String registerDate);

    /**
     * 统计ltv
     */
    GameStatRemainDetail getPayGameStatRemainDetail(@Param("roleType") int roleType,
                                                    @Param("serverId") int serverId,
                                                    @Param("registerDate") String registerDate);

    /**
     * 查询留存
     */
    ServerRemain selectRemain(@Param("serverId") int serverId,
                              @Param("registerDate") String registerDate,
                              @Param("days") int days,
                              @Param("logDb") String logDb);

    /**
     * 付费留存
     */
    ServerRemain selectPayRemain(@Param("serverId") int serverId,
                                 @Param("registerDate") String registerDate,
                                 @Param("days") int days,
                                 @Param("logDb") String logDb);

    /**
     * 免费留存
     */
    ServerRemain selectFreeRemain(@Param("serverId") int serverId,
                                  @Param("registerDate") String registerDate,
                                  @Param("days") int days,
                                  @Param("logDb") String logDb);
}

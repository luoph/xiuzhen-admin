/*
 * create by mybatis-plus-generator  https://github.com/xiweile
 */
package cn.youai.xiuzhen.stat.mapper;

import cn.youai.xiuzhen.stat.entity.GameStatRemain;
import cn.youai.xiuzhen.stat.entity.ServerRemain;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

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
    GameStatRemain getGameStatRemain(@Param("roleType") int roleType,
                                     @Param("serverId") int serverId,
                                     @Param("registerDate") String registerDate);

    /**
     * 付费次留
     */
    Integer getPayRemain(@Param("serverId") int serverId, @Param("registerDate") String registerDate);

    /**
     * 免费次留
     */
    Integer getFreeRemain(@Param("serverId") int serverId, @Param("registerDate") String registerDate);

    /**
     * 查询留存
     */
    ServerRemain selectRemain(@Param("serverId") int serverId, @Param("registerDate") String registerDate, @Param("days") int days);

    /**
     * 付费留存
     */
    ServerRemain selectPayRemain(@Param("serverId") int serverId, @Param("registerDate") String registerDate, @Param("days") int days);

}

/*
 * create by mybatis-plus-generator  https://github.com/xiweile
 */
package cn.youai.xiuzhen.stat.mapper;

import cn.youai.xiuzhen.stat.entity.GameStatRemain;
import cn.youai.xiuzhen.stat.entity.ServerRemain;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

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
     * 留存统计
     */
    GameStatRemain queryGameStatRemain(@Param("serverId") int serverId,
                                       @Param("roleType") int roleType,
                                       @Param("registerDate") Date registerDate);

    /**
     * 统计ltv
     */
    GameStatRemain getGameStatRemain(@Param("roleType") int roleType,
                                     @Param("serverId") int serverId,
                                     @Param("registerDate") Date registerDate);

    /**
     * 付费次留
     */
    int getPayRemain(@Param("serverId") int serverId, @Param("registerDate") Date registerDate);

    /**
     * 免费次留
     */
    int getFreeRemain(@Param("serverId") int serverId, @Param("registerDate") Date registerDate);

    /**
     * 查询留存
     */
    ServerRemain selectRemain(@Param("serverId") int serverId, @Param("registerDate") Date registerDate, @Param("days") int days);

    /**
     * 付费留存
     */
    ServerRemain selectPayRemain(@Param("serverId") int serverId, @Param("registerDate") Date registerDate, @Param("days") int days);

}

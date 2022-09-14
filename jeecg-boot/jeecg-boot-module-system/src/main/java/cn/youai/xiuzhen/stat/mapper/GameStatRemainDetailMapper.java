/*
 * create by mybatis-plus-generator  https://github.com/xiweile
 */
package cn.youai.xiuzhen.stat.mapper;

import cn.youai.xiuzhen.stat.entity.GameStatRemainDetail;
import cn.youai.xiuzhen.stat.entity.ServerRemain;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

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
     * 留存
     */
    GameStatRemainDetail getStatRemainDetail(@Param("roleType") int roleType,
                                             @Param("serverId") int serverId,
                                             @Param("registerDate") String registerDate);

    /**
     * 付费留存
     */
    GameStatRemainDetail getPayStatRemainDetail(@Param("roleType") int roleType,
                                                @Param("serverId") int serverId,
                                                @Param("registerDate") String registerDate);

    /**
     * 免费留存
     */
    GameStatRemainDetail getFreeStatRemainDetail(@Param("roleType") int roleType,
                                                 @Param("serverId") int serverId,
                                                 @Param("registerDate") String registerDate);

    /**
     * 查询留存
     */
    ServerRemain selectRemain(@Param("serverId") int serverId,
                              @Param("registerDate") String registerDate,
                              @Param("days") int days);

    /**
     * 付费留存
     */
    ServerRemain selectPayRemain(@Param("serverId") int serverId,
                                 @Param("registerDate") String registerDate,
                                 @Param("days") int days);

    /**
     * 免费留存
     */
    ServerRemain selectFreeRemain(@Param("serverId") int serverId,
                                  @Param("registerDate") String registerDate,
                                  @Param("days") int days);
}

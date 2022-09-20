/*
 * create by mybatis-plus-generator  https://github.com/xiweile
 */
package cn.youai.xiuzhen.stat.mapper;

import cn.youai.xiuzhen.stat.entity.GameStatRemainDetail;
import cn.youai.xiuzhen.stat.entity.ServerRemain;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

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
    GameStatRemainDetail queryServerRemain(@Param("serverId") int serverId,
                                           @Param("roleType") int roleType,
                                           @Param("registerDate") Date registerDate);

    /**
     * 留存
     */
    GameStatRemainDetail queryChannelRemain(@Param("channel") String channel,
                                            @Param("roleType") int roleType,
                                            @Param("registerDate") Date registerDate);

    /**
     * 付费留存
     */
    GameStatRemainDetail queryServerPayRemain(@Param("serverId") int serverId,
                                              @Param("roleType") int roleType,
                                              @Param("registerDate") Date registerDate);

    GameStatRemainDetail queryChannelPayRemain(@Param("channel") String channel,
                                               @Param("roleType") int roleType,
                                               @Param("registerDate") Date registerDate);

    /**
     * 免费留存
     */
    GameStatRemainDetail queryServerFreeRemain(@Param("serverId") int serverId,
                                               @Param("roleType") int roleType,
                                               @Param("registerDate") Date registerDate);

    GameStatRemainDetail queryChannelFreeRemain(@Param("channel") String channel,
                                                @Param("roleType") int roleType,
                                                @Param("registerDate") Date registerDate);

    /**
     * 查询留存
     */
    ServerRemain selectServerRemain(@Param("serverId") int serverId,
                                    @Param("registerDate") Date registerDate,
                                    @Param("days") int days);

    ServerRemain selectChannelRemain(@Param("channel") String channel,
                                     @Param("registerDate") Date registerDate,
                                     @Param("days") int days);

    /**
     * 付费留存
     */
    ServerRemain selectServerPayRemain(@Param("serverId") int serverId,
                                       @Param("registerDate") Date registerDate,
                                       @Param("days") int days);

    ServerRemain selectChannelPayRemain(@Param("channel") String channel,
                                        @Param("registerDate") Date registerDate,
                                        @Param("days") int days);

    /**
     * 免费留存
     */
    ServerRemain selectServerFreeRemain(@Param("serverId") int serverId,
                                        @Param("registerDate") Date registerDate,
                                        @Param("days") int days);

    ServerRemain selectChannelFreeRemain(@Param("channel") String channel,
                                         @Param("registerDate") Date registerDate,
                                         @Param("days") int days);
}

/*
 * create by mybatis-plus-generator  https://github.com/xiweile
 */
package cn.youai.xiuzhen.stat.mapper;

import cn.youai.xiuzhen.stat.entity.GameStatLtvDetail;
import cn.youai.xiuzhen.stat.entity.ServerLtvAmount;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

/**
 * <p>
 * LTV统计 Mapper 接口
 * </p>
 *
 * @author buliangliang
 * @since 2021-12-22
 */
public interface GameStatLtvDetailMapper extends BaseMapper<GameStatLtvDetail> {

    /**
     * 统计ltv
     */
    GameStatLtvDetail queryServerLtvDetail(@Param("serverId") int serverId,
                                           @Param("registerDate") Date registerDate);

    GameStatLtvDetail queryChannelLtvDetail(@Param("channel") String channel,
                                            @Param("registerDate") Date registerDate);

    /**
     * 查询LTV充值金额
     *
     * @param serverId     服务器ID
     * @param days         天数
     * @param registerDate 注册日期
     * @return 充值金额
     */
    ServerLtvAmount queryServerLtvAmount(@Param("serverId") int serverId,
                                         @Param("registerDate") Date registerDate,
                                         @Param("days") int days);

    ServerLtvAmount queryChannelLtvAmount(@Param("channel") String channel,
                                         @Param("registerDate") Date registerDate,
                                         @Param("days") int days);

}

/*
 * create by mybatis-plus-generator  https://github.com/xiweile
 */
package org.jeecg.modules.game.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.game.entity.GameStatLtvDetail;
import org.jeecg.modules.game.entity.ServerLtvAmount;

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
    GameStatLtvDetail getGameStatLtvDetail(@Param("serverId") int serverId, @Param("registerDate") String registerDate);

    /**
     * 查询LTV充值金额
     *
     * @param serverId     服务器ID
     * @param days         天数
     * @param registerDate 注册日期
     * @return 充值金额
     */
    ServerLtvAmount getLtvAmount(@Param("serverId") int serverId,
                                 @Param("registerDate") String registerDate,
                                 @Param("days") int days);

}

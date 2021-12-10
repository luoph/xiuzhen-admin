/*
 * create by mybatis-plus-generator  https://github.com/xiweile
 */
package org.jeecg.modules.game.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.game.entity.GameStatLtv;
import org.jeecg.modules.game.entity.ServerLtvAmount;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * LTV统计 Mapper 接口
 * </p>
 *
 * @author buliangliang
 * @since 2020-08-26
 */
public interface GameLtvCountMapper extends BaseMapper<GameStatLtv> {
    /**
     * 统计ltv
     */
    GameStatLtv getGameStatLtv(@Param("serverId") int serverId,
                               @Param("registerDate") Date registerDate);

    /**
     * 插入或更新
     */
    int updateOrInsert(List<GameStatLtv> list);

    /**
     * 查询留存
     * 优化-需要手动处理日期
     *
     * @param serverId          服务器
     * @param startRegisterDate 当天0点注册时间
     * @param endRegisterDate   当天24点注册时间
     * @param targetDate        统计时间
     * @param logTable          logTable
     * @return 金额
     */
    double selectLtv(@Param("serverId") int serverId,
                     @Param("startRegisterDate") String startRegisterDate,
                     @Param("endRegisterDate") String endRegisterDate,
                     @Param("targetDate") String targetDate,
                     @Param("logTable") String logTable);


    /**
     * 查询LTV充值金额
     *
     * @param serverId     服务器ID
     * @param days         天数
     * @param registerDate 注册日期
     * @return 充值金额
     */
    ServerLtvAmount getLtvAmount(@Param("serverId") int serverId,
                                 @Param("registerDate") Date registerDate,
                                 @Param("days") int days);
}

/*
 * create by mybatis-plus-generator  https://github.com/xiweile
 */
package cn.youai.xiuzhen.stat.mapper;

import cn.youai.basics.model.DateRange;
import cn.youai.server.model.RangeValue;
import cn.youai.xiuzhen.game.entity.GamePlayer;
import cn.youai.xiuzhen.stat.entity.PlayerBehavior;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 玩家表 Mapper 接口
 * </p>
 *
 * @author buliangliang
 * @since 2021-12-07
 */
public interface GamePlayerMapper extends BaseMapper<GamePlayer> {
    GamePlayer queryPlayer(@Param("playerId") Long playerId);

    List<GamePlayer> queryPlayerList(@Param("playerIds") Collection<Long> playerIds);

    List<PlayerBehavior> queryPlayerBehavior(@Param("rangeDateBeginTime") Date rangeDateBeginTime,
                                             @Param("rangeDateEndTime") Date rangeDateEndTime,
                                             @Param("playerId") Long playerId,
                                             @Param("logTable") String logTable);

    IPage<GamePlayer> queryList(Page<GamePlayer> page,
                                @Param("entity") GamePlayer entity,
                                @Param("levelRange") RangeValue<BigDecimal> levelRange,
                                @Param("combatPowerRange") RangeValue<BigDecimal> combatPowerRange,
                                @Param("createDateRange") DateRange createDateRange);

}

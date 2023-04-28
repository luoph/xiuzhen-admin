/*
 * create by mybatis-plus-generator  https://github.com/xiweile
 */
package cn.youai.xiuzhen.stat.mapper;

import cn.youai.basics.model.DateRange;
import cn.youai.server.model.RangeValue;
import cn.youai.xiuzhen.game.entity.GamePlayer;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.system.entity.SysUser;

import java.math.BigDecimal;
import java.util.Collection;
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
    GamePlayer queryPlayer(@Param("playerId") Long playerId, @Param("configAuth") String configAuth);

    List<GamePlayer> queryPlayerList(@Param("playerIds") Collection<Long> playerIds);

    List<GamePlayer> selectPlayerListByUser(@Param("user") SysUser user, @Param("playerIds") String playerIds);

    IPage<GamePlayer> queryList(Page<GamePlayer> page,
                                @Param("entity") GamePlayer entity,
                                @Param("levelRange") RangeValue<BigDecimal> levelRange,
                                @Param("combatPowerRange") RangeValue<BigDecimal> combatPowerRange,
                                @Param("dateRange") DateRange dateRange,
                                @Param("configAuth") String configAuth);
}

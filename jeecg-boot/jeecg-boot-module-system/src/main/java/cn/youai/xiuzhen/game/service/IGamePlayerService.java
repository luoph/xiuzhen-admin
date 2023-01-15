/*
 * create by mybatis-plus-generator  https://github.com/xiweile
 */
package cn.youai.xiuzhen.game.service;

import cn.youai.basics.model.DateRange;
import cn.youai.server.model.RangeValue;
import cn.youai.xiuzhen.game.entity.GamePlayer;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 玩家表 服务类
 * </p>
 *
 * @author buliangliang
 * @since 2021-12-07
 */
public interface IGamePlayerService extends IService<GamePlayer> {

    GamePlayer queryPlayer(long playerId);

    List<GamePlayer> queryPlayerList(Collection<Long> playerIds);

    Map<Integer, List<GamePlayer>> groupPlayerByServerId(Collection<Long> playerIds);

    IPage<GamePlayer> queryList(Page<GamePlayer> page,
                                GamePlayer entity,
                                RangeValue<BigDecimal> levelRange,
                                RangeValue<BigDecimal> combatPowerRange,
                                DateRange createDateRange);
}

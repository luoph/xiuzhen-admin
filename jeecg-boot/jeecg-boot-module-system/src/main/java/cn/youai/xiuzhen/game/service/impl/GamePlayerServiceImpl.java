/*
 * create by mybatis-plus-generator  https://github.com/xiweile
 */
package cn.youai.xiuzhen.game.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.youai.basics.model.DateRange;
import cn.youai.server.model.RangeValue;
import cn.youai.xiuzhen.game.entity.GamePlayer;
import cn.youai.xiuzhen.game.service.IGamePlayerService;
import cn.youai.xiuzhen.stat.mapper.GamePlayerMapper;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.SysUserCacheInfo;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 玩家表 服务实现类
 * </p>
 *
 * @author buliangliang
 * @since 2021-12-07
 */
@Service
@DS("master")
public class GamePlayerServiceImpl extends ServiceImpl<GamePlayerMapper, GamePlayer> implements IGamePlayerService {

    @Override
    public GamePlayer queryPlayer(long playerId) {
        String configAuth = QueryGenerator.getAllConfigAuth();
        return getBaseMapper().queryPlayer(playerId, configAuth);
    }

    @Override
    public List<GamePlayer> selectPlayerListByUser(SysUserCacheInfo user, String playerIds) {
        return getBaseMapper().selectPlayerListByUser(user, playerIds);
    }

    @Override
    public List<GamePlayer> queryPlayerList(Collection<Long> playerIds) {
        if (CollUtil.isEmpty(playerIds)) {
            return new ArrayList<>();
        }
        return getBaseMapper().queryPlayerList(playerIds);
    }

    @Override
    public Map<Integer, List<GamePlayer>> groupPlayerByServerId(Collection<Long> playerIds) {
        List<GamePlayer> playerList = queryPlayerList(playerIds);
        return playerList.stream().collect(Collectors.groupingBy(GamePlayer::getServerId, HashMap::new, Collectors.toCollection(ArrayList::new)));
    }

    @Override
    public IPage<GamePlayer> queryList(Page<GamePlayer> page,
                                       GamePlayer entity,
                                       RangeValue<BigDecimal> levelRange,
                                       RangeValue<BigDecimal> combatPowerRange,
                                       DateRange createDateRange) {
        String configAuth = QueryGenerator.getAllConfigAuth();
        return getBaseMapper().queryList(page, entity, levelRange, combatPowerRange, createDateRange, configAuth);
    }
}

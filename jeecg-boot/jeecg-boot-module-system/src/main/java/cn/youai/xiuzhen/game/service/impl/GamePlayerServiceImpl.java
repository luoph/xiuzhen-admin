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
    public GamePlayer selectPlayer(long playerId) {
        return getBaseMapper().selectPlayer(playerId);
    }

    @Override
    public List<GamePlayer> selectPlayerList(Collection<Long> playerIds) {
        if (CollUtil.isEmpty(playerIds)) {
            return new ArrayList<>();
        }
        return getBaseMapper().selectPlayerList(playerIds);
    }

    @Override
    public Map<Integer, List<GamePlayer>> groupPlayerByServerId(Collection<Long> playerIds) {
        List<GamePlayer> playerList = selectPlayerList(playerIds);
        return playerList.stream().collect(Collectors.groupingBy(GamePlayer::getServerId, HashMap::new, Collectors.toCollection(ArrayList::new)));
    }

    @Override
    public IPage<GamePlayer> selectList(Page<GamePlayer> page, GamePlayer entity, RangeValue<BigDecimal> levelRange, RangeValue<BigDecimal> combatPowerRange, DateRange createDateRange) {
        return getBaseMapper().selectList(page, entity, levelRange, combatPowerRange, createDateRange);
    }
}

/*
 * create by mybatis-plus-generator  https://github.com/xiweile
 */
package cn.youai.xiuzhen.game.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.youai.entities.GamePlayer;
import cn.youai.xiuzhen.game.service.IGamePlayerService;
import cn.youai.xiuzhen.stat.mapper.GamePlayerMapper;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

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
    public Map<Long, String> getPlayerNicknameMap(Collection<Long> playerIds) {
        if (CollUtil.isEmpty(playerIds)) {
            return new HashMap<>();
        }
        LambdaQueryWrapper<GamePlayer> query = Wrappers.lambdaQuery();
        query.select(GamePlayer::getPlayerId, GamePlayer::getNickname);
        query.in(GamePlayer::getPlayerId, playerIds);
        query.groupBy(GamePlayer::getPlayerId);

        List<GamePlayer> list = list(query);
        return CollectionUtil.isNotEmpty(list) ?
                list.stream().collect(Collectors.toMap(GamePlayer::getPlayerId, GamePlayer::getNickname, (k, v) -> v))
                : new HashMap<>(list.size());
    }

    @Override
    public List<GamePlayer> selectPlayerByName(String nickname) {
        return getBaseMapper().selectPlayerByName(nickname);
    }

    @Override
    public GamePlayer getPlayer(long playerId) {
        return getOne(Wrappers.<GamePlayer>lambdaQuery().eq(GamePlayer::getPlayerId, playerId));
    }

    @Override
    public List<GamePlayer> getPlayerList(Collection<Long> playerIds) {
        if (CollUtil.isEmpty(playerIds)) {
            return new ArrayList<>();
        }
        return list(Wrappers.<GamePlayer>lambdaQuery().in(GamePlayer::getPlayerId, playerIds).orderByAsc(GamePlayer::getPlayerId));
    }

    @Override
    public Map<Integer, List<GamePlayer>> groupPlayerByServerId(Collection<Long> playerIds) {
        List<GamePlayer> playerList = getPlayerList(playerIds);
        return playerList.stream().collect(Collectors.groupingBy(GamePlayer::getServerId, HashMap::new, Collectors.toCollection(ArrayList::new)));
    }
}

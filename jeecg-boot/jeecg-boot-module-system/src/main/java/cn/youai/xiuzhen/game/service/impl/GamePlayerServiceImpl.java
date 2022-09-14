/*
 * create by mybatis-plus-generator  https://github.com/xiweile
 */
package cn.youai.xiuzhen.game.service.impl;

import cn.youai.entities.GamePlayer;
import cn.youai.xiuzhen.game.service.IGamePlayerService;
import cn.youai.xiuzhen.stat.mapper.GamePlayerMapper;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

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
    public GamePlayer getPlayer(long playerId) {
        return getOne(Wrappers.<GamePlayer>lambdaQuery().eq(GamePlayer::getPlayerId, playerId));
    }

    @Override
    public List<GamePlayer> getPlayerList(List<Long> playerIds) {
        return list(Wrappers.<GamePlayer>lambdaQuery().in(GamePlayer::getPlayerId, playerIds).orderByAsc(GamePlayer::getPlayerId));
    }
}

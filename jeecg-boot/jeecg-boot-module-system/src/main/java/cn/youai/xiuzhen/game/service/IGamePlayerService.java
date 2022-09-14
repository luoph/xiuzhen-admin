/*
 * create by mybatis-plus-generator  https://github.com/xiweile
 */
package cn.youai.xiuzhen.game.service;

import cn.youai.entities.GamePlayer;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 玩家表 服务类
 * </p>
 *
 * @author buliangliang
 * @since 2021-12-07
 */
public interface IGamePlayerService extends IService<GamePlayer> {

    GamePlayer getPlayer(long playerId);

    List<GamePlayer> getPlayerList(List<Long> playerIds);

}

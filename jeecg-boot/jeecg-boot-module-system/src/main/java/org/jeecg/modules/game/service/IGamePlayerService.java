/*
 * create by mybatis-plus-generator  https://github.com/xiweile
 */
package org.jeecg.modules.game.service;

import cn.youai.entities.GamePlayer;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.player.entity.PlayerBehavior;

import java.util.Date;
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

    /**
     * 查询玩家行为
     */
    List<PlayerBehavior> queryPlayerBehavior(Date rangeDateBegin, Date rangeDateEnd, String nickname, Long playerId, int days, int serverId);

}

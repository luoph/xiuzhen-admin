/*
 * create by mybatis-plus-generator  https://github.com/xiweile
 */
package cn.youai.xiuzhen.stat.service;

import cn.youai.xiuzhen.game.entity.GamePlayer;
import cn.youai.xiuzhen.stat.entity.PlayerBehavior;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 玩家统计
 * </p>
 *
 * @author buliangliang
 * @since 2021-12-07
 */
public interface IPlayerStatService extends IService<GamePlayer> {

    /**
     * 查询玩家行为
     */
    List<PlayerBehavior> queryPlayerBehavior(Date rangeDateBegin, Date rangeDateEnd, String nickname, Long playerId, int days, int serverId);

}

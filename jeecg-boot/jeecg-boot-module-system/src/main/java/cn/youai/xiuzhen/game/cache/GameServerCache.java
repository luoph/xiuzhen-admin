package cn.youai.xiuzhen.game.cache;

import cn.youai.basics.cache.BaseCache;
import cn.youai.basics.cache.CacheFactory;
import cn.youai.server.springboot.utils.SpringContextUtils;
import cn.youai.xiuzhen.game.entity.GameServer;
import cn.youai.xiuzhen.game.service.IGameServerService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * GameServerCache
 *
 * @author ocean
 * @since 202212051226
 */
public final class GameServerCache extends BaseCache<Integer, GameServer> {

    private static class CacheHolder {
        private static final GameServerCache INSTANCE = new GameServerCache();
    }

    public static GameServerCache getInstance() {
        return GameServerCache.CacheHolder.INSTANCE;
    }

    private GameServerCache() {
        super(CacheFactory.NEVER_EXPIRE);
    }

    @Override
    public GameServer without(Integer serverId) {
        return SpringContextUtils.getBean(IGameServerService.class).getOne(Wrappers.<GameServer>lambdaQuery().eq(GameServer::getId, serverId));
    }

    public void loadAll() {
        SpringContextUtils.getBean(IGameServerService.class).list().forEach(e -> put(e.getId(), e));
    }

    public Set<Integer> getStopServerRefundServerIds() {
        return values().stream().filter(e -> e.getStopServerRefund() == 1).map(GameServer::getId).collect(Collectors.toSet());
    }
}

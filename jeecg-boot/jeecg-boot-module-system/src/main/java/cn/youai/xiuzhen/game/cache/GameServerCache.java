package cn.youai.xiuzhen.game.cache;

import cn.hutool.core.collection.CollUtil;
import cn.youai.basics.cache.BaseCache;
import cn.youai.basics.cache.CacheFactory;
import cn.youai.xiuzhen.game.entity.GameServer;
import cn.youai.xiuzhen.game.service.IGameServerService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.jeecg.common.util.SpringContextUtils;

import java.util.List;
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
        IGameServerService gameServerService = SpringContextUtils.getBean(IGameServerService.class);
        GameServer gameServer = gameServerService.getOne(Wrappers.<GameServer>lambdaQuery().eq(GameServer::getId, serverId));
        if (null != gameServer) {
            gameServer.parseDate();
            gameServerService.setChannelSimpleNameList(CollUtil.newArrayList(gameServer));
        }
        return gameServer;
    }

    public void loadAll() {
        IGameServerService gameServerService = SpringContextUtils.getBean(IGameServerService.class);
        List<GameServer> gameServerList = gameServerService.list();
        gameServerService.setChannelSimpleNameList(gameServerList);
        gameServerList.forEach(e -> {
            e.parseDate();
            put(e.getId(), e);
        });
    }

    public Set<Integer> getStopServerRefundServerIds() {
        return values().stream().filter(e -> e.getStopServerRefund() == 1).map(GameServer::getId).collect(Collectors.toSet());
    }
}

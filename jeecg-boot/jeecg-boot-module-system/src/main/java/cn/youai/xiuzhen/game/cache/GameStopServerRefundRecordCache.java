package cn.youai.xiuzhen.game.cache;

import cn.hutool.core.collection.CollUtil;
import cn.youai.basics.cache.BaseCache;
import cn.youai.basics.cache.CacheFactory;
import cn.youai.server.springboot.utils.SpringContextUtils;
import cn.youai.xiuzhen.game.entity.GameStopServerRefundRecord;
import cn.youai.xiuzhen.game.service.IGameStopServerRefundRecordService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 删档返还记录
 *
 * @author ocean
 * @since 202212051226
 */
public final class GameStopServerRefundRecordCache extends BaseCache<Integer, List<GameStopServerRefundRecord>> {

    private static class CacheHolder {
        private static final GameStopServerRefundRecordCache INSTANCE = new GameStopServerRefundRecordCache();
    }

    public static GameStopServerRefundRecordCache getInstance() {
        return GameStopServerRefundRecordCache.CacheHolder.INSTANCE;
    }

    private GameStopServerRefundRecordCache() {
        super(CacheFactory.NEVER_EXPIRE);
    }

    @Override
    public List<GameStopServerRefundRecord> without(Integer sourceServerId) {
        return SpringContextUtils.getBean(IGameStopServerRefundRecordService.class).list(Wrappers.<GameStopServerRefundRecord>lambdaQuery()
                .eq(GameStopServerRefundRecord::getSourceServerId, sourceServerId));
    }

    public void loadAll() {
        put(SpringContextUtils.getBean(IGameStopServerRefundRecordService.class).list());
    }

    public void put(List<GameStopServerRefundRecord> list) {
        if (CollUtil.isNotEmpty(list)) {
            list.forEach(e -> get(e.getSourceServerId()).add(e));
        }
    }

    public Set<Long> getRecordPlayerIds(Set<Integer> gameServerIds) {
        if (CollUtil.isEmpty(gameServerIds)) {
            return Collections.emptySet();
        }

        Set<Long> recordPlayerIds = new HashSet<>();
        gameServerIds.forEach(gameServerId -> recordPlayerIds.addAll(get(gameServerId).stream().map(GameStopServerRefundRecord::getSourcePlayerId).collect(Collectors.toSet())));
        return recordPlayerIds;
    }
}

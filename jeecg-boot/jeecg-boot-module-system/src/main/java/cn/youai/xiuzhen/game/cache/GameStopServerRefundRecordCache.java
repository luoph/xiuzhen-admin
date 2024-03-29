package cn.youai.xiuzhen.game.cache;

import cn.hutool.core.collection.CollUtil;
import cn.youai.basics.cache.BaseCache;
import cn.youai.basics.cache.CacheFactory;
import cn.youai.xiuzhen.game.entity.GameStopServerRefundRecord;
import cn.youai.xiuzhen.game.service.IGameStopServerRefundRecordService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.jeecg.common.util.SpringContextUtils;

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

    public Set<Long> getRecordPlayerIds(Set<Integer> serverIds) {
        if (CollUtil.isEmpty(serverIds)) {
            return Collections.emptySet();
        }

        Set<Long> recordPlayerIds = new HashSet<>();
        serverIds.forEach(t -> recordPlayerIds.addAll(get(t).stream().map(GameStopServerRefundRecord::getSourcePlayerId).collect(Collectors.toSet())));
        return recordPlayerIds;
    }
}

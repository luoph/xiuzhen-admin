package cn.youai.xiuzhen.game.cache;

import cn.youai.basics.cache.BaseCache;
import cn.youai.basics.cache.CacheFactory;
import cn.youai.xiuzhen.game.entity.GameSetting;
import cn.youai.xiuzhen.game.service.IGameSettingService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.jeecg.common.util.SpringContextUtils;

/**
 * GameSettingCache
 *
 * @author ocean
 * @since 202305301053
 */
public final class GameSettingCache extends BaseCache<String, GameSetting> {

    private static class CacheHolder {
        private static final GameSettingCache INSTANCE = new GameSettingCache();
    }

    public static GameSettingCache getInstance() {
        return GameSettingCache.CacheHolder.INSTANCE;
    }

    private GameSettingCache() {
        super(CacheFactory.NEVER_EXPIRE);
    }

    @Override
    public GameSetting without(String key) {
        return SpringContextUtils.getBean(IGameSettingService.class).getOne(Wrappers.<GameSetting>lambdaQuery().eq(GameSetting::getDictKey, key));
    }

    public void loadAll() {
        SpringContextUtils.getBean(IGameSettingService.class).list().forEach(e -> put(e.getDictKey(), e));
    }
}

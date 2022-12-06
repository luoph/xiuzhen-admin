package cn.youai.xiuzhen.game.cache;

/**
 * CacheServiceManager
 *
 * @author ocean
 * @since 202212061542
 */
public final class CacheServiceManager {

    public static void loadAll() {
        GameServerCache.getInstance().loadAll();
        GameStopServerRefundRecordCache.getInstance().loadAll();
    }
}

package cn.youai.xiuzhen.utils;

import com.googlecode.concurrentlinkedhashmap.ConcurrentLinkedHashMap;

/**
 * 缓存构建工具类
 *
 * @author luopeihuan
 */
public final class CacheUtils {

    private CacheUtils() {
    }


    /**
     * 构建缓存
     *
     * @param capacity 缓存大小
     * @return {@linkplain ConcurrentLinkedHashMap}
     */
    public static <K, V> ConcurrentLinkedHashMap<K, V> buildCache(long capacity) {
        ConcurrentLinkedHashMap.Builder<K, V> builder = new ConcurrentLinkedHashMap.Builder<>();
        return builder.maximumWeightedCapacity(capacity).build();
    }


}

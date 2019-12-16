package org.jeecg.common.datasource;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * 动态数据源路由
 *
 * @author : 披荆斩棘
 * @date : 2017/9/6
 */
public class DynamicMultipleDataSource extends AbstractRoutingDataSource {

    private static final ThreadLocal<String> DATA_SOURCE_KEY = new ThreadLocal<>();

    public static void setDataSourceKey(String dataSource) {
        DATA_SOURCE_KEY.set(dataSource);
    }

    public static void clear() {
        DATA_SOURCE_KEY.remove();
    }

    public static String get() {
        return DATA_SOURCE_KEY.get();
    }

    @Override
    protected Object determineCurrentLookupKey() {
        final String lookupKey = DATA_SOURCE_KEY.get();
        clear();
        return lookupKey;
    }


}

package org.jeecg.common.datasource;

/**
 * 切换数据源
 *
 * @author : luopeihuan
 * @date :2019-12-05
 */
public class DataSourceSwitch {

    /**
     * 设置数据源key
     */
    public static void switchDataSource(String dataSource) {
        DynamicMultipleDataSource.setDataSourceKey(dataSource);
    }

    /**
     * 重置未默认数据源
     */
    public static void resetDataSource() {
        DynamicMultipleDataSource.setDataSourceKey(DataSourceKey.DEFAULT_DATA_SOURCE_KEY);
    }

}

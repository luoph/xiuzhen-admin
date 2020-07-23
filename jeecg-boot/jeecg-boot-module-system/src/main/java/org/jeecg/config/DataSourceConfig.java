package org.jeecg.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.datasource.DataSourceKey;
import org.jeecg.common.datasource.DynamicMultipleDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author : 披荆斩棘
 * @date : 2017/9/6
 */
@Slf4j
@Configuration
public class DataSourceConfig {

    public static final String DRIVER_NAME = "com.mysql.jdbc.Driver";
    private static final Map<Object, Object> DATA_SOURCE_MAP = new ConcurrentHashMap<>();

    /**
     * 动态数据源配置
     *
     * @return
     */
    @Bean
    public DynamicMultipleDataSource multipleDataSource(@Qualifier(DataSourceKey.DEFAULT_DATA_SOURCE_KEY) DataSource dataSource) {
        DynamicMultipleDataSource dynamicMultipleDataSource = new DynamicMultipleDataSource();
        DATA_SOURCE_MAP.put(DataSourceKey.DEFAULT_DATA_SOURCE_KEY, dataSource);
        dynamicMultipleDataSource.setTargetDataSources(DATA_SOURCE_MAP);
        dynamicMultipleDataSource.setDefaultTargetDataSource(dataSource);
        return dynamicMultipleDataSource;
    }

    /**
     * 添加新的数据源
     *
     * @param key
     * @param dataSource
     */
    public static void addDataSource(String key, DataSource dataSource) {
        DATA_SOURCE_MAP.put(key, dataSource);
    }

    /**
     * 获取数据源
     *
     * @param key
     * @return
     */
    public static DataSource getDataSource(String key) {
        return (DataSource) DATA_SOURCE_MAP.get(key);
    }

    @Primary
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.druid.default")
    public DataSource dataSourceDefault() {
        DataSource defaultDataSource = DruidDataSourceBuilder.create().build();
        addDataSource(DataSourceKey.DEFAULT_DATA_SOURCE_KEY, defaultDataSource);
        return defaultDataSource;
    }

    /**
     * 创建数据库
     *
     * @param url
     * @param username
     * @param password
     * @return
     */
    public static DataSource createDataSource(String url, String username, String password) {
        Map<String, String> properties = new HashMap<>(4);
        properties.put(DruidDataSourceFactory.PROP_URL, url);
        properties.put(DruidDataSourceFactory.PROP_USERNAME, username);
        properties.put(DruidDataSourceFactory.PROP_PASSWORD, password);
        properties.put(DruidDataSourceFactory.PROP_DRIVERCLASSNAME, DRIVER_NAME);
        try {
            DruidDataSource dataSource = (DruidDataSource) DruidDataSourceFactory.createDataSource(properties);
            dataSource.setBreakAfterAcquireFailure(true);
            dataSource.setRemoveAbandoned(false);
            dataSource.setRemoveAbandonedTimeout(600);
            dataSource.setLogAbandoned(true);
            dataSource.setBreakAfterAcquireFailure(true);
            dataSource.setTimeBetweenConnectErrorMillis(60);
            dataSource.setConnectionErrorRetryAttempts(10);
            dataSource.setMaxWait(3000);
            // 这行代码很重要，如果不加不会立即建立数据库连接，也就无法检测连接是否正确
            dataSource.getConnection();
            return dataSource;
        } catch (Exception e) {
            log.error("createDataSource error, url:" + url, e);
            throw new RuntimeException(e);
        }
    }
}




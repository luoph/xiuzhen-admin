package cn.youai.xiuzhen.config;

import cn.youai.server.web.datasource.DynamicMultipleDataSource;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.baomidou.dynamic.datasource.DynamicRoutingDataSource;
import com.baomidou.dynamic.datasource.provider.AbstractDataSourceProvider;
import com.baomidou.dynamic.datasource.provider.DynamicDataSourceProvider;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DataSourceProperty;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DynamicDataSourceAutoConfiguration;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DynamicDataSourceProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;

import javax.annotation.Resource;
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
@AutoConfigureBefore({DynamicDataSourceAutoConfiguration.class, SpringBootConfiguration.class})
public class DataSourceConfig {
    /**
     * 多数据源key : 默认数据源
     **/
    public static final String DEFAULT_DATA_SOURCE_KEY = "dataSourceDefault";
    public static final String SERVER_DATA_SOURCE_KEY = "dataSourceServer:";

    public static final String DRIVER_NAME = "com.mysql.jdbc.Driver";
    private static final Map<Object, Object> DATA_SOURCE_MAP = new ConcurrentHashMap<>();

    @Resource
    private DynamicDataSourceProperties properties;

    @Lazy
    @Resource(name = "shardingSphereDataSource")
    private DataSource shardingSphereDataSource;

    /**
     * 将动态数据源设置为首选的
     * 当spring存在多个数据源时, 自动注入的是首选的对象
     * 设置为主要的数据源之后，就可以支持shardingjdbc原生的配置方式了
     */
    @Primary
    @Bean
    public DataSource dataSource() {
        DynamicRoutingDataSource dataSource = new DynamicRoutingDataSource();
        dataSource.setPrimary(properties.getPrimary());
        dataSource.setStrict(properties.getStrict());
        dataSource.setStrategy(properties.getStrategy());
        dataSource.setP6spy(properties.getP6spy());
        dataSource.setSeata(properties.getSeata());
        addDataSource(DEFAULT_DATA_SOURCE_KEY, dataSource);
        return dataSource;
    }

    @Bean
    public DynamicDataSourceProvider dynamicDataSourceProvider() {
        Map<String, DataSourceProperty> datasourceMap = properties.getDatasource();
        return new AbstractDataSourceProvider() {
            @Override
            public Map<String, DataSource> loadDataSources() {
                Map<String, DataSource> dataSourceMap = createDataSourceMap(datasourceMap);
                dataSourceMap.put("shardingSphere", shardingSphereDataSource);
                return dataSourceMap;
            }
        };
    }

    /**
     * 动态数据源配置
     */
    @Bean
    public DynamicMultipleDataSource multipleDataSource(DataSource dataSource) {
        DynamicMultipleDataSource dynamicMultipleDataSource = new DynamicMultipleDataSource();
        dynamicMultipleDataSource.setTargetDataSources(DATA_SOURCE_MAP);
        dynamicMultipleDataSource.setDefaultTargetDataSource(dataSource);
        addDataSource(DEFAULT_DATA_SOURCE_KEY, dataSource);
        return dynamicMultipleDataSource;
    }

    /**
     * 添加新的数据源
     */
    public static void addDataSource(String key, DataSource dataSource) {
        DATA_SOURCE_MAP.put(key, dataSource);
    }

    /**
     * 获取数据源
     */
    public static DataSource getDataSource(String key) {
        return (DataSource) DATA_SOURCE_MAP.get(key);
    }

    /**
     * 创建数据库
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




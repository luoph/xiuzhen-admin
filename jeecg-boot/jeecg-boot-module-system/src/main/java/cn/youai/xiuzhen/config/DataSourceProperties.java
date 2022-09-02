package cn.youai.xiuzhen.config;

import com.alibaba.druid.pool.DruidAbstractDataSource;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 多数据源属性
 *
 * @author hunji
 * @since 1.0.0
 */
@Slf4j
@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = DataSourceProperties.PREFIX)
public class DataSourceProperties {
    public static final String PREFIX = "spring.datasource.druid";

    private String dbType;
    private String driverClassName;
    private String url;
    private String username;
    private String password;

    /**
     * Druid默认参数
     */
    private int initialSize = DruidAbstractDataSource.DEFAULT_INITIAL_SIZE;
    private int maxActive = DruidAbstractDataSource.DEFAULT_MAX_ACTIVE_SIZE;
    private int minIdle = DruidAbstractDataSource.DEFAULT_MIN_IDLE;
    private int maxIdle = DruidAbstractDataSource.DEFAULT_MAX_IDLE;
    private long maxWait = DruidAbstractDataSource.DEFAULT_MAX_WAIT;

    private long timeBetweenEvictionRunsMillis = DruidAbstractDataSource.DEFAULT_TIME_BETWEEN_EVICTION_RUNS_MILLIS;
    private long minEvictableIdleTimeMillis = DruidAbstractDataSource.DEFAULT_MIN_EVICTABLE_IDLE_TIME_MILLIS;
    private long maxEvictableIdleTimeMillis = DruidAbstractDataSource.DEFAULT_MAX_EVICTABLE_IDLE_TIME_MILLIS;
    private long keepAliveBetweenTimeMillis = DruidAbstractDataSource.DEFAULT_TIME_BETWEEN_EVICTION_RUNS_MILLIS * 2;

    private String validationQuery = DruidAbstractDataSource.DEFAULT_VALIDATION_QUERY;
    private int validationQueryTimeout = -1;

    private boolean testOnBorrow = DruidAbstractDataSource.DEFAULT_TEST_ON_BORROW;
    private boolean testOnReturn = DruidAbstractDataSource.DEFAULT_TEST_ON_RETURN;
    private boolean testWhileIdle = DruidAbstractDataSource.DEFAULT_WHILE_IDLE;

    private int queryTimeout;
    private int maxWaitThreadCount = -1;
    private boolean poolPreparedStatements = false;
    private boolean sharePreparedStatements = false;
    private int maxOpenPreparedStatements = -1;
    private boolean breakAfterAcquireFailure = false;
    private boolean removeAbandoned = false;
    private boolean logAbandoned;
    private int removeAbandonedTimeoutMillis = 300 * 1000;
    private int connectionErrorRetryAttempts = 1;
    private long timeBetweenConnectErrorMillis = DruidAbstractDataSource.DEFAULT_TIME_BETWEEN_CONNECT_ERROR_MILLIS;
    private int maxPoolPreparedStatementPerConnectionSize = 10;
    private String filters = "stat,wall";
}

package org.jeecg.config;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import org.jeecg.common.datasource.DataSourceKey;
import org.jeecg.common.datasource.DynamicMultipleDataSource;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author : 披荆斩棘
 * @date : 2017/9/6
 */
@Configuration
public class DataSourceConfig {

    /**
     * 动态数据源配置
     *
     * @return
     */
    @Bean
    public DynamicMultipleDataSource multipleDataSource(@Qualifier(DataSourceKey.DEFAULT_DATA_SOURCE_KEY) DataSource dataSource) {
        DynamicMultipleDataSource dynamicMultipleDataSource = new DynamicMultipleDataSource();
        Map<Object, Object> targetDataSources = new HashMap<>();
        targetDataSources.put(DataSourceKey.DEFAULT_DATA_SOURCE_KEY, dataSource);
        dynamicMultipleDataSource.setTargetDataSources(targetDataSources);
        dynamicMultipleDataSource.setDefaultTargetDataSource(dataSource);
        return dynamicMultipleDataSource;
    }

    @Primary
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.druid.default")
    public DataSource dataSourceDefault() {
        return DruidDataSourceBuilder.create().build();
    }
}




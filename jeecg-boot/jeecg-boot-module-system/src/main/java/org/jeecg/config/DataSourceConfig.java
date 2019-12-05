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
@MapperScan(basePackages = "org.jeecg.**.mapper")
@Configuration
public class DataSourceConfig {

//    private static final String MAPPER_PACKAGE = "org.jeecg.**.mapper";
//    private static final String MAPPER_LOCATION = "classpath*:/mappers/**/*Mapper.xml";

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

//    /**
//     * 相当于顶部的：
//     * {@code @MapperScan("com.baomidou.springboot.mapper*")}
//     * 这里可以扩展，比如使用配置文件来配置扫描Mapper的路径
//     */
//    @Bean
//    public MapperScannerConfigurer mapperScannerConfigurer() {
//        MapperScannerConfigurer scannerConfigurer = new MapperScannerConfigurer();
//        //可以通过环境变量获取你的mapper路径,这样mapper扫描可以通过配置文件配置了
//        scannerConfigurer.setBasePackage(MAPPER_PACKAGE);
//        return scannerConfigurer;
//    }
//
//    /**
//     * 自定义配置{@link MybatisSqlSessionFactoryBean}
//     * 使用mp-boot-starter 完全可以去掉这些配置，使用yml配置方式, 这里只做示范
//     */
//    @Bean("sqlSessionFactory")
//    @Primary
//    public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
//        MybatisSqlSessionFactoryBean sqlSessionFactory = new MybatisSqlSessionFactoryBean();
//        sqlSessionFactory.setDataSource(dataSource);
//
//        Resource[] resources = PATTERN_RESOLVER.getResources(MAPPER_LOCATION);
//        sqlSessionFactory.setMapperLocations(resources);
//
//        PaginationInterceptor pagination = new PaginationInterceptor();
//        sqlSessionFactory.setPlugins(new Interceptor[]{
//                pagination,
//                new PerformanceInterceptor()
//        });
//
//        GlobalConfig globalConfig = new GlobalConfig();
//        GlobalConfig.DbConfig dbConfig = new GlobalConfig.DbConfig();
//        dbConfig.setIdType(IdType.UUID).setTableUnderline(true);
//        globalConfig.setDbConfig(dbConfig);
//        globalConfig.setBanner(false);
//
//        sqlSessionFactory.setGlobalConfig(globalConfig);
//        return sqlSessionFactory.getObject();
//    }

//    @Bean
//    public SqlSessionFactory sqlSessionFactory(DynamicMultipleDataSource dynamicMultipleDataSource) throws
//            Exception {
//        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
//        sqlSessionFactoryBean.setDataSource(dynamicMultipleDataSource);
//        return sqlSessionFactoryBean.getObject();
//    }
}




package org.jeecg.database;

import cn.youai.basics.utils.ObjectReference;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.SqlSessionFactory;
import org.jeecg.common.datasource.DataSourceKey;
import org.jeecg.common.datasource.DataSourceSwitch;
import org.jeecg.common.util.SpringWebContextUtils;
import org.jeecg.config.DataSourceConfig;
import org.jeecg.modules.game.entity.GameServer;
import org.jeecg.modules.game.mapper.GameServerMapper;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.lang.reflect.Field;

/**
 * @author luopeihuan
 * @version 1.0
 * @date 2019-12-05.
 */
@Slf4j
@Component
public class DataSourceHelper implements InitializingBean {

    private static final String JDBC_URL_PATTERN = "jdbc:mysql://%s:%d/%s?zeroDateTimeBehavior=convertToNull&characterEncoding=utf-8&allowMultiQueries=true";

    private static final ObjectReference<DataSourceHelper> REFERENCE = new ObjectReference<>();

    @Autowired
    private GameServerMapper gameServerMapper;

    @Override
    public void afterPropertiesSet() {
        REFERENCE.set(this);
    }

    public static DataSourceHelper getInstance() {
        return REFERENCE.get();
    }

    private GameServer getGameServerById(Integer serverId) {
        return gameServerMapper.selectById(serverId);
    }

    private void loadDataSourceByServerId(Integer serverId) {
        DataSource dataSource = DataSourceConfig.getDataSource(DataSourceKey.SERVER_DATA_SOURCE_KEY + serverId);
        if (dataSource == null) {
            GameServer gameServer = getGameServerById(serverId);
            if (gameServer != null) {
                String jdbcUrl = String.format(JDBC_URL_PATTERN, gameServer.getDbHost(), gameServer.getDbPort(), gameServer.getDbName());
                dataSource = DataSourceConfig.createDataSource(jdbcUrl, gameServer.getDbUser(), gameServer.getDbPassword());
                // 添加到数据源池
                DataSourceConfig.addDataSource(DataSourceKey.SERVER_DATA_SOURCE_KEY + serverId, dataSource);
            }
        }
    }

    /**
     * 切换数据源
     *
     * @param serverId 游戏服id
     */
    public static void useServerDatabase(Integer serverId) {
        getInstance().loadDataSourceByServerId(serverId);
        switchDataSource(DataSourceKey.SERVER_DATA_SOURCE_KEY + serverId);
    }

    public static void useDatabase(String dataSource) {
        switchDataSource(dataSource);
    }

    private static void switchDataSource(String key) {
        DataSource dataSource = DataSourceConfig.getDataSource(key);
        if (dataSource != null) {
            // 修改MyBatis的数据源
            try {
                SqlSessionFactory sqlSessionFactory = SpringWebContextUtils.getBean(SqlSessionFactory.class);
                Environment environment = sqlSessionFactory.getConfiguration().getEnvironment();
                Field dataSourceField = environment.getClass().getDeclaredField("dataSource");
                dataSourceField.setAccessible(true);
                // 修改mybatis的数据源
                dataSourceField.set(environment, dataSource);
                DataSourceSwitch.switchDataSource(key);
            } catch (Exception e) {
                log.error("useServerDatabase error, key:" + key, e);
            }
        }
    }

    /**
     * 切换到默认数据源
     */
    public static void useDefaultDatabase() {
        switchDataSource(DataSourceKey.DEFAULT_DATA_SOURCE_KEY);
    }
}

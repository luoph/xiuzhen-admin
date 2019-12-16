package org.jeecg.database;

import cn.youai.commons.utils.ObjectReference;
import org.jeecg.common.datasource.DataSourceKey;
import org.jeecg.common.datasource.DataSourceSwitch;
import org.jeecg.config.DataSourceConfig;
import org.jeecg.modules.game.entity.GameServer;
import org.jeecg.modules.game.mapper.GameServerMapper;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author luopeihuan
 * @version 1.0
 * @date 2019-12-05.
 */
@Component
public class DataSourceHelper implements InitializingBean {

    private static final ObjectReference<DataSourceHelper> REFERENCE = new ObjectReference<>();
    private final Map<Integer, DataSource> dataSourceMap = new ConcurrentHashMap<>();

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
        DataSource dataSource = dataSourceMap.get(serverId);
        if (dataSource == null) {
            GameServer gameServer = getGameServerById(serverId);
            if (gameServer != null) {
                dataSource = DataSourceConfig.createDataSource(gameServer.getDbHost(), gameServer.getDbName(), gameServer.getDbPassword());
                dataSourceMap.put(serverId, dataSource);

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
        DataSourceSwitch.switchDataSource(DataSourceKey.SERVER_DATA_SOURCE_KEY + serverId);
    }

    /**
     * 切换到默认数据源
     */
    public static void useDefaultDatabase() {
        DataSourceSwitch.resetDataSource();
    }
}

package cn.youai.xiuzhen.core.database;

import cn.youai.basics.utils.ObjectReference;
import cn.youai.xiuzhen.game.entity.GameServer;
import cn.youai.xiuzhen.game.mapper.GameServerMapper;
import com.mongodb.ConnectionString;
import lombok.extern.slf4j.Slf4j;
import org.ehcache.impl.internal.concurrent.ConcurrentHashMap;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author luopeihuan
 * @version 1.0
 * @date 2019-12-05.
 */
@Slf4j
@Component
public class MongoDataSourceHelper implements InitializingBean {

    private static final ObjectReference<MongoDataSourceHelper> REFERENCE = new ObjectReference<>();

    private static final String MONGO_URI_PATTERN = "mongodb://%s:%s@%s?authSource=admin&readPreference=primary&ssl=false&directConnection=true";

    @Autowired
    private GameServerMapper gameServerMapper;

    private final Map<Integer, MongoTemplate> templateMap = new ConcurrentHashMap<>();

    @Value("${app.mongo.user}")
    private String user;

    @Value("${app.mongo.password}")
    private String password;

    @Override
    public void afterPropertiesSet() {
        REFERENCE.set(this);
    }

    public void reload() {
        templateMap.clear();
    }

    public static MongoDataSourceHelper getInstance() {
        return REFERENCE.get();
    }

    private GameServer selectGameServerById(int serverId) {
        GameServer gameServer = gameServerMapper.selectById(serverId);
        if (gameServer != null && gameServer.getPid() != null) {
            return gameServerMapper.selectById(gameServer.getPid());
        }
        return gameServer;
    }

    public MongoTemplate getMongoTemplate(int serverId) {
        MongoTemplate mongoTemplate = templateMap.get(serverId);
        if (mongoTemplate == null) {
            GameServer gameServer = selectGameServerById(serverId);
            if (gameServer == null) {
                return null;
            }

            String mongodbUrl = String.format(MONGO_URI_PATTERN, user, password, gameServer.getMongodb());
            ConnectionString connectionString = new ConnectionString(mongodbUrl);
            mongoTemplate = new MongoTemplate(new SimpleMongoClientDatabaseFactory(connectionString));
            templateMap.put(serverId, mongoTemplate);
        }
        return mongoTemplate;
    }
}

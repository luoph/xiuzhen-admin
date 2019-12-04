package org.jeecg.modules.monitor.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.monitor.domain.RedisInfo;
import org.jeecg.modules.monitor.service.RedisService;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * Redis 监控信息获取
 *
 * @Author MrBird
 */
@Service("redisService")
@Slf4j
public class RedisServiceImpl implements RedisService {

    @Resource
    private RedisConnectionFactory redisConnectionFactory;

    /**
     * Redis详细信息
     */
    @Override
    public List<RedisInfo> getRedisInfo() {
        Properties info = redisConnectionFactory.getConnection().info();
        List<RedisInfo> infoList = new ArrayList<>();
        RedisInfo redisInfo = null;
        for (Map.Entry<Object, Object> entry : info.entrySet()) {
            redisInfo = new RedisInfo();
            redisInfo.setKey(oConvertUtils.getString(entry.getKey()));
            redisInfo.setValue(oConvertUtils.getString(entry.getValue()));
            infoList.add(redisInfo);
        }
        return infoList;
    }

    @Override
    public Map<String, Object> getKeysSize() {
        Long dbSize = redisConnectionFactory.getConnection().dbSize();
        Map<String, Object> map = new HashMap<>();
        map.put("create_time", System.currentTimeMillis());
        map.put("dbSize", dbSize);

        log.info("--getKeysSize--: " + map.toString());
        return map;
    }

    @Override
    public Map<String, Object> getMemoryInfo() {
        Map<String, Object> map = null;
        Properties info = redisConnectionFactory.getConnection().info();
        for (Map.Entry<Object, Object> entry : info.entrySet()) {
            String key = oConvertUtils.getString(entry.getKey());
            if ("used_memory".equals(key)) {
                map = new HashMap<>();
                map.put("used_memory", entry.getValue());
                map.put("create_time", System.currentTimeMillis());
            }
        }
        log.info("--getMemoryInfo--: " + map);
        return map;
    }
}

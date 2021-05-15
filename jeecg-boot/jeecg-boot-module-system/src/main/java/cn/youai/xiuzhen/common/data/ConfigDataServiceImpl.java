package cn.youai.xiuzhen.common.data;

import cn.youai.server.model.ReadonlyRoleAttr;
import cn.youai.server.utils.CQEngineUtils;
import cn.youai.xiuzhen.entity.common.ConfigData;
import cn.youai.xiuzhen.utils.CacheUtils;
import cn.youai.xiuzhen.utils.ConfigUtils;
import cn.youai.xiuzhen.utils.ConvertUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.googlecode.concurrentlinkedhashmap.ConcurrentLinkedHashMap;
import com.googlecode.cqengine.IndexedCollection;
import com.googlecode.cqengine.query.Query;
import com.googlecode.cqengine.query.QueryFactory;
import com.googlecode.cqengine.query.option.QueryOptions;
import com.googlecode.cqengine.resultset.ResultSet;
import lombok.extern.slf4j.Slf4j;
import net.dreamlu.mica.core.utils.ReflectUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.ReflectionUtils;

import javax.annotation.PostConstruct;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 配置数据管理类
 *
 * @author luopeihuan
 */
@Slf4j
@Service
public class ConfigDataServiceImpl implements ConfigDataService {

    private static final String CONFIG_FOLDER = "json/";

    private static final String INDEXED_COLLECTION = "indexedCollection";

    /**
     * 配置数据
     */
    @SuppressWarnings("rawtypes")
    private final ConcurrentLinkedHashMap<String, List> configDataMap = CacheUtils.buildCache(100000);

    /**
     * CQEngine 使用的 indexedCollection Map
     */
    private final Map<String, Object> indexedCollectionMap = new ConcurrentHashMap<>();

    /**
     * 解析文件
     */
    @Override
    @PostConstruct
    @SuppressWarnings({"unchecked", "rawtypes"})
    public void load() {
        for (ConfigDataEnum value : ConfigDataEnum.values()) {
            ClassPathResource classPathResource = new ClassPathResource(CONFIG_FOLDER + value.getTableName() + ".json");
            if (!classPathResource.exists()) {
                log.warn("[Config] {}.json not exists", value.getTableName());
                continue;
            }

            try {
                byte[] fileData = FileCopyUtils.copyToByteArray(classPathResource.getInputStream());
                // 通过文件名获取 moduleCode
                Class entityClass = value.getEntityClass();
                if (entityClass != null) {
                    List dataList = readJson(fileData, entityClass);
                    log.debug("[Config] load [{}] filename:[{}] class:[{}] size:{}", value, value.getTableName(), entityClass.getSimpleName(), dataList.size());
                    // 存入缓存
                    saveCachedData(value.getTableName(), dataList, entityClass);
                }

            } catch (Exception e) {
                log.error("[Config] load " + value.getTableName() + ".json, error", e);
            }
        }
    }

    private static <T> List<T> readJson(byte[] fileData, Class<T> clazz) {
        return readJson(new String(fileData, StandardCharsets.UTF_8), clazz);
    }

    private static <T> List<T> readJson(String fileContent, Class<T> clazz) {
        List<T> list = new ArrayList<>();
        JSONArray jsonArray = JSON.parseArray(fileContent);
        for (Object object : jsonArray) {
            JSONObject json = (JSONObject) object;
            // 验证有 json array 的字段，例如 Reward.rewardMoney
            checkJsonArrayKey(json);
            JSONObject configJson = JSON.parseObject(json.toJSONString());
            // 初始化包装类型为0值
            ConvertUtils.initialPrimitiveKey(configJson, clazz);
            T data = JSON.parseObject(configJson.toJSONString(), clazz);
            if (data instanceof ConfigData) {
                ((ConfigData) data).onload(configJson);

                // 加载后标为只读
                if (data instanceof ReadonlyRoleAttr) {
                    ((ReadonlyRoleAttr) data).setReadOnly(true);
                }
            }

            list.add(data);
        }
        return list;
    }

    private static void checkJsonArrayKey(JSONObject json) {
        for (Map.Entry<String, Object> entry : json.entrySet()) {
            Object value = entry.getValue();
            if (value instanceof String) {
                String valueString = (String) value;
                boolean isJsonArray = ConfigUtils.isJSONArray(valueString);
                if (isJsonArray) {
                    try {
                        JSONArray jsonArray = JSON.parseArray(valueString);
                        json.put(entry.getKey(), jsonArray);
                    } catch (Exception e) {
                        log.warn("[Config] checkJsonArrayKey exception, json:{}", json);
                        json.put(entry.getKey(), valueString);
                    }
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    private <T> List<T> getCachedData(String tableName) {
        if (StringUtils.isEmpty(tableName)) {
            return null;
        }
        return (List<T>) configDataMap.get(tableName);
    }

    @Override
    public <T> List<T> getCachedData(ConfigDataEnum dataEnum, Class<T> clazz) {
        return getCachedData(dataEnum.getTableName());
    }

    private <T> void saveCachedData(String tableName, List<T> objectList, Class<T> clazz) {
        if (StringUtils.isNotBlank(tableName)) {
            // 处理 CQEngine 的索引集合
            setupIndexedCollection(tableName, clazz, objectList);
            configDataMap.put(tableName, objectList);
        } else {
            if (log.isDebugEnabled()) {
                log.debug("[Config] data model config error");
            }
            throw new RuntimeException("配置数据表错误!");
        }
    }

    @Override
    public <T> T selectOne(ConfigDataEnum dataEnum, Class<T> clazz, Query<T> query) {
        return selectOne(dataEnum.getTableName(), query, null);
    }

    @Override
    public <T> T selectOne(ConfigDataEnum dataEnum, Class<T> clazz, Query<T> query, boolean showError) {
        return selectOne(dataEnum.getTableName(), query, null, showError);
    }

    @Override
    public <T> T selectOne(ConfigDataEnum dataEnum, Class<T> clazz, Query<T> query, QueryOptions queryOptions) {
        return selectOne(dataEnum.getTableName(), query, queryOptions);
    }

    @Override
    public <T> T selectOne(ConfigDataEnum dataEnum, Class<T> clazz, Query<T> query, QueryOptions queryOptions, boolean showError) {
        return selectOne(dataEnum.getTableName(), query, queryOptions, showError);
    }

    @Override
    public <T> T selectOne(ConfigDataEnum dataEnum, Class<T> clazz, QueryOptions queryOptions) {
        return selectOne(dataEnum.getTableName(), QueryFactory.all(clazz), queryOptions);
    }

    @Override
    public <T> T selectOne(ConfigDataEnum dataEnum, Class<T> clazz, QueryOptions queryOptions, boolean showError) {
        return selectOne(dataEnum.getTableName(), QueryFactory.all(clazz), queryOptions, showError);
    }

    private <T> T selectOne(String tableName, Query<T> query, QueryOptions queryOptions) {
        return selectOne(tableName, query, queryOptions, false);
    }

    private <T> T selectOne(String tableName, Query<T> query, QueryOptions queryOptions, boolean showError) {
        T target = null;
        try {
            ResultSet<T> resultSet = retrieve(tableName, query, queryOptions);
            if (resultSet != null) {
                Iterator<T> iterator = resultSet.iterator();
                if (iterator.hasNext()) {
                    target = iterator.next();
                }
            }
        } catch (Exception e) {
            log.error("[Config] selectOne error, tableName:" + tableName + ", query:" + query + ", queryOptions:" + queryOptions, e);
        }

        if (target == null && showError) {
            log.error("selectOne error, table:" + tableName + ", query:" + query + ", options:" + queryOptions, new RuntimeException(tableName + " config error"));
        }
        return target;
    }

    @Override
    public <T> List<T> selectList(ConfigDataEnum dataEnum, Class<T> clazz, Query<T> query) {
        return selectList(dataEnum, clazz, query, false);
    }

    @Override
    public <T> List<T> selectList(ConfigDataEnum dataEnum, Class<T> clazz, Query<T> query, boolean showError) {
        return selectList(dataEnum.getTableName(), query, null, showError);
    }

    @Override
    public <T> List<T> selectList(ConfigDataEnum dataEnum, Class<T> clazz, Query<T> query, QueryOptions queryOptions) {
        return selectList(dataEnum, clazz, query, queryOptions, false);
    }

    @Override
    public <T> List<T> selectList(ConfigDataEnum dataEnum, Class<T> clazz, Query<T> query, QueryOptions queryOptions, boolean showError) {
        return selectList(dataEnum.getTableName(), query, queryOptions, showError);
    }

    @Override
    public <T> List<T> selectList(ConfigDataEnum dataEnum, Class<T> clazz, QueryOptions queryOptions) {
        return selectList(dataEnum.getTableName(), clazz, queryOptions, false);
    }

    @Override
    public <T> List<T> selectList(ConfigDataEnum dataEnum, Class<T> clazz, QueryOptions queryOptions, boolean showError) {
        return selectList(dataEnum.getTableName(), clazz, queryOptions, showError);
    }

    @Override
    public <T> T selectOneRandom(ConfigDataEnum dataEnum, Class<T> clazz) {
        List<T> list = getCachedData(dataEnum.getTableName());
        return ConfigUtils.random(list);
    }

    private <T> List<T> selectList(String tableName, Query<T> query, QueryOptions queryOptions, boolean showError) {
        List<T> resultList = new ArrayList<>();
        try {
            ResultSet<T> resultSet = retrieve(tableName, query, queryOptions);
            if (resultSet != null) {
                resultSet.stream().forEach(resultList::add);
            }
            return resultList;
        } catch (Exception e) {
            log.error("[Config] selectList error, tableName:" + tableName + ", query:" + query + ", queryOptions:" + queryOptions, e);
        }

        if (resultList.isEmpty() && showError) {
            log.error("selectList error, table:" + tableName + ", query:" + query + ", options:" + queryOptions, new RuntimeException(tableName + " config error"));
        }
        return resultList;
    }

    private <T> List<T> selectList(String tableName, Class<T> clazz, QueryOptions queryOptions, boolean showError) {
        List<T> resultList = new ArrayList<>();
        ResultSet<T> resultSet = retrieve(tableName, QueryFactory.all(clazz), queryOptions);
        if (resultSet != null && resultSet.isNotEmpty()) {
            resultSet.stream().forEach(resultList::add);
        }

        if (resultList.isEmpty() && showError) {
            log.error("selectList error, table:" + tableName + ", options:" + queryOptions, new RuntimeException(tableName + " config error"));
        }
        return resultList;
    }

    @SuppressWarnings("unchecked")
    private <T> ResultSet<T> retrieve(String tableName, Query<T> query, QueryOptions queryOptions) {
        try {
            IndexedCollection<T> indexedCollection = (IndexedCollection<T>) indexedCollectionMap.get(tableName);
            return CQEngineUtils.retrieve(indexedCollection, query, queryOptions);
        } catch (Exception e) {
            log.error("[Config] retrieve error, tableName:" + tableName + ", query:" + query + ", queryOptions:" + queryOptions, e);
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    private <T> void setupIndexedCollection(String tableName, Class<T> clazz, List<T> list) {
        Method indexedCollectionMethod = ReflectionUtils.findMethod(clazz, INDEXED_COLLECTION);
        if (indexedCollectionMethod != null) {
            IndexedCollection<T> indexedCollection = (IndexedCollection<T>) ReflectUtil.invokeMethod(indexedCollectionMethod, clazz);
            if (indexedCollection != null && list != null && !list.isEmpty()) {
                indexedCollection.addAll(list);
                indexedCollectionMap.put(tableName, indexedCollection);
            }
        }
    }

}

package cn.youai.xiuzhen.common.data;

import cn.youai.xiuzhen.entity.common.ConfigData;
import cn.youai.xiuzhen.utils.CQEngineUtils;
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
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.ReflectionUtils;

import javax.annotation.PostConstruct;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
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
     * LRU MAP Builder
     */
    private static final ConcurrentLinkedHashMap.Builder<String, List<Object>> BUILDER = new ConcurrentLinkedHashMap.Builder<>();

    /**
     * 配置数据
     */
    private ConcurrentLinkedHashMap<String, List<Object>> configDataMap = BUILDER.maximumWeightedCapacity(10000000).build();

    /**
     * CQEngine 使用的 indexedCollection Map
     */
    private Map<String, IndexedCollection<Object>> indexedCollectionMap = new ConcurrentHashMap<>();

    /**
     * 解析文件
     */
    @Override
    @PostConstruct
    @SuppressWarnings("unchecked")
    public void load() {
        for (ConfigDataEnum value : ConfigDataEnum.values()) {
            ClassPathResource classPathResource = new ClassPathResource(CONFIG_FOLDER + value.getTableName() + ".json");
            if (!classPathResource.exists()) {
                log.warn("{}.json not exists", value.getTableName());
                continue;
            }

            try {
                byte[] fileData = FileCopyUtils.copyToByteArray(classPathResource.getInputStream());
                // 通过文件名获取 moduleCode
                int moduleCode = value.getModelCode();
                String entityName = value.getEntityName();
                if (StringUtils.isNotEmpty(entityName)) {
                    Class clazz = Class.forName(entityName);

                    List dataList = readJson(fileData, clazz);
                    log.debug("saveCachedData moduleCode:{}, filename:{}, class:{}, size:{}", moduleCode, value.getTableName(), entityName, dataList.size());
                    // 存入缓存
                    saveCachedData(moduleCode, dataList, clazz);
                }

            } catch (Exception e) {
                log.error("load " + value.getTableName() + ".json, error", e);
            }
        }
    }

    private static <T> List<T> readJson(byte[] fileData, Class<T> clazz) {
        return readJson(new String(fileData, StandardCharsets.UTF_8), clazz);
    }

    private static <T> List<T> readJson(String fileContent, Class<T> clazz) {
        ArrayList<T> list = new ArrayList<>();
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
                ((ConfigData) data).onload();
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
                        log.warn("checkJsonArrayKey exception, json:{}", json);
                        json.put(entry.getKey(), valueString);
                    }
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> List<T> getCachedData(int modelCode, Class<T> clazz) {
        String tableName = ConfigDataEnum.getTableName(modelCode);
        if (StringUtils.isEmpty(tableName)) {
            return null;
        }
        String key = genCacheKey(tableName, modelCode);
        return (List<T>) configDataMap.get(key);
    }

    @Override
    public <T> List<T> getCachedData(ConfigDataEnum dataEnum, Class<T> clazz) {
        return getCachedData(dataEnum.getModelCode(), clazz);
    }

    /**
     * 以序列化的方式存储到redis中
     *
     * @param modelCode  {@linkplain ConfigDataEnum#getModelCode()}
     * @param objectList 配置数据列表
     */
    @Override
    public void saveCachedData(int modelCode, List<Object> objectList, Class clazz) {
        String tableName = ConfigDataEnum.getTableName(modelCode);
        if (modelCode > 0) {
            // 处理 CQEngine 的索引集合
            setupClassIndexedCollection(modelCode, clazz, objectList);
            String key = genCacheKey(tableName, modelCode);
            configDataMap.put(key, objectList);
        } else {
            if (log.isDebugEnabled()) {
                log.debug("data model config error");
            }

            throw new RuntimeException("配置数据表错误!");
        }
    }

    @Override
    public <T> T selectOne(ConfigDataEnum dataEnum, Class<T> clazz, Query<T> query) {
        return selectOne(dataEnum.getModelCode(), clazz, query, null);
    }

    @Override
    public <T> T selectOne(int modelCode, Class<T> clazz, Query<T> query) {
        return selectOne(modelCode, clazz, query, null);
    }

    @Override
    public <T> T selectOne(ConfigDataEnum dataEnum, Class<T> clazz, Query<T> query, QueryOptions queryOptions) {
        return selectOne(dataEnum.getModelCode(), clazz, query, queryOptions);
    }

    @Override
    public <T> T selectOne(ConfigDataEnum dataEnum, Class<T> clazz, QueryOptions queryOptions) {
        return selectOne(dataEnum.getModelCode(), clazz, QueryFactory.all(clazz), queryOptions);
    }

    @Override
    public <T> T selectOne(int modelCode, Class<T> clazz, Query<T> query, QueryOptions queryOptions) {

        ResultSet<T> resultSet = retrieve(modelCode, clazz, query, queryOptions);
        if (resultSet != null && resultSet.isNotEmpty()) {
            for (T t : resultSet) {
                if (t != null) {
                    return t;
                }
            }
        }

        return null;
    }

    @Override
    public <T> List<T> selectList(ConfigDataEnum dataEnum, Class<T> clazz, Query<T> query) {
        return selectList(dataEnum.getModelCode(), clazz, query, null);
    }

    @Override
    public <T> List<T> selectList(int modelCode, Class<T> clazz, Query<T> query) {
        return selectList(modelCode, clazz, query, null);
    }

    @Override
    public <T> List<T> selectList(ConfigDataEnum dataEnum, Class<T> clazz, Query<T> query, QueryOptions queryOptions) {
        return selectList(dataEnum.getModelCode(), clazz, query, queryOptions);
    }

    @Override
    public <T> List<T> selectList(int modelCode, Class<T> clazz, Query<T> query, QueryOptions queryOptions) {

        List<T> resultList = new ArrayList<>();
        ResultSet<T> resultSet = retrieve(modelCode, clazz, query, queryOptions);
        if (resultSet != null && resultSet.isNotEmpty()) {
            for (T t : resultSet) {
                resultList.add(t);
            }
        }

        return resultList;
    }

    @Override
    public <T> List<T> selectList(int modelCode, Class<T> clazz, QueryOptions queryOptions) {
        List<T> resultList = new ArrayList<>();
        ResultSet<T> resultSet = retrieve(modelCode, clazz, QueryFactory.all(clazz), queryOptions);
        if (resultSet != null && resultSet.isNotEmpty()) {
            for (T t : resultSet) {
                resultList.add(t);
            }
        }
        return resultList;
    }

    @Override
    public <T> List<T> selectList(ConfigDataEnum dataEnum, Class<T> clazz, QueryOptions queryOptions) {
        return selectList(dataEnum.getModelCode(), clazz, queryOptions);
    }

    @Override
    public <T> T selectOneRandom(int modelCode, Class<T> clazz) {
        List<T> list = getCachedData(modelCode, clazz);
        return ConfigUtils.getRandomItem(list);
    }

    @Override
    public <T> T selectOneRandom(ConfigDataEnum dataEnum, Class<T> clazz) {
        return selectOneRandom(dataEnum.getModelCode(), clazz);
    }

    private <T> ResultSet<T> retrieve(int modelCode, Class<T> clazz, Query<T> query, QueryOptions queryOptions) {
        log.debug("retrieve modelCode:{} class:{} query:{} queryOption:{}", modelCode, clazz.getSimpleName(), query, queryOptions);
        IndexedCollection<T> indexedCollection = getClassIndexedCollection(modelCode, clazz);
        return CQEngineUtils.retrieve(indexedCollection, query, queryOptions);
    }


    @SuppressWarnings("unchecked")
    private void setupClassIndexedCollection(int modelCode, Class clazz, List objectList) {
        Method indexedCollectionMethod = ReflectionUtils.findMethod(clazz, INDEXED_COLLECTION);
        if (indexedCollectionMethod != null) {
            IndexedCollection<Object> indexedCollection = (IndexedCollection<Object>) ReflectionUtils.invokeMethod(indexedCollectionMethod, clazz);
            if (indexedCollection != null && objectList != null && objectList.size() > 0) {
                log.debug("setupClassIndexedCollection class:{} size:{}", clazz.getSimpleName(), objectList.size());
                indexedCollection.addAll(objectList);

                String indexedCollectionKey = genCacheKey(clazz.getName(), modelCode);
                indexedCollectionMap.put(indexedCollectionKey, indexedCollection);
            }
        }
    }

    private static String genCacheKey(String className, int modelCode) {
        return className + ":" + modelCode;
    }

    @SuppressWarnings("unchecked")
    private <T> IndexedCollection<T> getClassIndexedCollection(int modelCode, Class<T> clazz) {
        String indexedCollectionKey = genCacheKey(clazz.getName(), modelCode);
        return (IndexedCollection<T>) indexedCollectionMap.get(indexedCollectionKey);
    }

}

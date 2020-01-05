package cn.youai.xiuzhen.common.data;

import com.googlecode.cqengine.query.Query;
import com.googlecode.cqengine.query.option.QueryOptions;

import java.util.List;

/**
 * @author buliangliang
 * 获取存在redis中的数据集合工具
 */
public interface ConfigDataService {

    /**
     * 加载数据
     */
    void load();

    /**
     * 从缓存中获取配置数据
     *
     * @param modelCode 模型编码
     * @param clazz     类型
     * @param <T>
     * @return
     */
    <T> List<T> getCachedData(int modelCode, Class<T> clazz);

    /**
     * 从缓存中获取配置数据
     *
     * @param dataEnum 模型编码
     * @param clazz    类型
     * @param <T>      类型模板
     * @return 缓存的数据
     */
    <T> List<T> getCachedData(ConfigDataEnum dataEnum, Class<T> clazz);

    /**
     * 保存配置数据到缓存
     *
     * @param modelCode  模型编码
     * @param objectList 数据列表
     * @param clazz      对象类型
     */
    void saveCachedData(int modelCode, List<Object> objectList, Class clazz);

    /**
     * 根据条件查找满足条件的数据，只返回一条
     *
     * @param dataEnum
     * @param clazz
     * @param query
     * @param <T>
     * @return
     */
    <T> T selectOne(ConfigDataEnum dataEnum, Class<T> clazz, Query<T> query);

    /**
     * 根据条件查找满足条件的数据，只返回一条
     *
     * @param modelCode 模型编码
     * @param clazz     类型
     * @param query     查询条件
     * @return
     */
    <T> T selectOne(int modelCode, Class<T> clazz, Query<T> query);

    /**
     * 根据条件查找满足条件的数据，只返回一条
     *
     * @param dataEnum     模型枚举
     * @param clazz        类型
     * @param query        查询条件
     * @param queryOptions 查询选项，eg: order by
     * @param <T>
     * @return
     */
    <T> T selectOne(ConfigDataEnum dataEnum, Class<T> clazz, Query<T> query, QueryOptions queryOptions);

    /**
     * 根据排序选项返回列表第一条
     *
     * @param dataEnum
     * @param clazz
     * @param queryOptions
     * @param <T>
     * @return
     */
    <T> T selectOne(ConfigDataEnum dataEnum, Class<T> clazz, QueryOptions queryOptions);

    /**
     * 根据条件查找满足条件的数据，只返回一条
     *
     * @param modelCode    模型编码
     * @param clazz        类型
     * @param query        查询条件
     * @param queryOptions 查询选项，eg: order by
     * @return
     */
    <T> T selectOne(int modelCode, Class<T> clazz, Query<T> query, QueryOptions queryOptions);

    /**
     * 根据条件查找满足条件的数据
     *
     * @param dataEnum 模型枚举
     * @param clazz    类型
     * @param query    查询条件
     * @param <T>
     * @return
     */
    <T> List<T> selectList(ConfigDataEnum dataEnum, Class<T> clazz, Query<T> query);

    /**
     * 根据条件查找满足条件的数据
     *
     * @param modelCode 模型编码
     * @param clazz     类型
     * @param query     查询条件
     * @return
     */
    <T> List<T> selectList(int modelCode, Class<T> clazz, Query<T> query);

    /**
     * 根据条件查找满足条件的数据
     *
     * @param dataEnum     模型枚举
     * @param clazz        类型
     * @param query        查询条件
     * @param queryOptions
     * @param <T>
     * @return
     */
    <T> List<T> selectList(ConfigDataEnum dataEnum, Class<T> clazz, Query<T> query, QueryOptions queryOptions);

    /**
     * 根据条件查找满足条件的数据
     *
     * @param modelCode    模型编码
     * @param clazz        类型
     * @param query        查询条件
     * @param queryOptions 查询选项，eg: order by
     * @return
     */
    <T> List<T> selectList(int modelCode, Class<T> clazz, Query<T> query, QueryOptions queryOptions);

    /**
     * 获取所有数据，根据 queryOptions 排序
     *
     * @param modelCode
     * @param clazz
     * @param queryOptions
     * @param <T>
     * @return
     */
    <T> List<T> selectList(int modelCode, Class<T> clazz, QueryOptions queryOptions);

    /**
     * 获取所有数据，根据 queryOptions 排序
     *
     * @param dataEnum
     * @param clazz
     * @param queryOptions
     * @param <T>
     * @return
     */
    <T> List<T> selectList(ConfigDataEnum dataEnum, Class<T> clazz, QueryOptions queryOptions);

    /**
     * 随机获取一条记录
     *
     * @param modelCode
     * @param clazz
     * @param <T>
     * @return
     */
    <T> T selectOneRandom(int modelCode, Class<T> clazz);

    /**
     * 随机获取一条记录
     *
     * @param dataEnum
     * @param clazz
     * @param <T>
     * @return
     */
    <T> T selectOneRandom(ConfigDataEnum dataEnum, Class<T> clazz);

}

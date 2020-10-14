package cn.youai.xiuzhen.common.data;

import com.googlecode.cqengine.query.Query;
import com.googlecode.cqengine.query.option.QueryOptions;

import java.util.List;

/**
 * @author luopeihuan
 */
public interface ConfigDataService {

    /**
     * 加载数据
     */
    void load();

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
     * 根据条件查找满足条件的数据，只返回一条
     *
     * @param dataEnum 配置
     * @param clazz    类型
     * @param query    查询条件
     * @param <T>      配置数据类型
     * @return 目标配置数据
     */
    <T> T selectOne(ConfigDataEnum dataEnum, Class<T> clazz, Query<T> query);

    /**
     * 根据条件查找满足条件的数据，只返回一条
     *
     * @param dataEnum  配置
     * @param clazz     类型
     * @param query     查询条件
     * @param <T>       配置数据类型
     * @param showError 未查到是否打印错误
     * @return 目标配置数据
     */
    <T> T selectOne(ConfigDataEnum dataEnum, Class<T> clazz, Query<T> query, boolean showError);

    /**
     * 根据条件查找满足条件的数据，只返回一条
     *
     * @param dataEnum     模型枚举
     * @param clazz        类型
     * @param query        查询条件
     * @param queryOptions 查询选项，eg: order by
     * @param <T>          配置数据类型
     * @return 目标配置数据
     */
    <T> T selectOne(ConfigDataEnum dataEnum, Class<T> clazz, Query<T> query, QueryOptions queryOptions);

    /**
     * 根据条件查找满足条件的数据，只返回一条
     *
     * @param dataEnum     模型枚举
     * @param clazz        类型
     * @param query        查询条件
     * @param queryOptions 查询选项，eg: order by
     * @param <T>          配置数据类型
     * @param showError    未查到是否打印错误
     * @return 目标配置数据
     */
    <T> T selectOne(ConfigDataEnum dataEnum, Class<T> clazz, Query<T> query, QueryOptions queryOptions, boolean showError);

    /**
     * 根据排序选项返回列表第一条
     *
     * @param dataEnum     模型枚举
     * @param clazz        类型
     * @param queryOptions 查询选项，eg: order by
     * @param <T>          配置数据类型
     * @return 目标配置数据
     */
    <T> T selectOne(ConfigDataEnum dataEnum, Class<T> clazz, QueryOptions queryOptions);

    /**
     * 根据排序选项返回列表第一条
     *
     * @param dataEnum     模型枚举
     * @param clazz        类型
     * @param queryOptions 查询选项，eg: order by
     * @param <T>          配置数据类型
     * @param showError    未查到是否打印错误
     * @return 目标配置数据
     */
    <T> T selectOne(ConfigDataEnum dataEnum, Class<T> clazz, QueryOptions queryOptions, boolean showError);

    /**
     * 根据条件查找满足条件的数据
     *
     * @param dataEnum 模型枚举
     * @param clazz    类型
     * @param query    查询条件
     * @param <T>      配置数据类型
     * @return 目标配置数据列表
     */
    <T> List<T> selectList(ConfigDataEnum dataEnum, Class<T> clazz, Query<T> query);

    <T> List<T> selectList(ConfigDataEnum dataEnum, Class<T> clazz, Query<T> query, boolean showError);

    /**
     * 根据条件查找满足条件的数据
     *
     * @param dataEnum     模型枚举
     * @param clazz        类型
     * @param query        查询条件
     * @param queryOptions 查询选项，eg: order by
     * @param <T>          配置数据类型
     * @return 目标配置数据列表
     */
    <T> List<T> selectList(ConfigDataEnum dataEnum, Class<T> clazz, Query<T> query, QueryOptions queryOptions);

    /**
     * 根据条件查找满足条件的数据
     *
     * @param dataEnum     模型枚举
     * @param clazz        类型
     * @param query        查询条件
     * @param queryOptions 查询选项，eg: order by
     * @param <T>          配置数据类型
     * @param showError    未查到是否打印错误
     * @return 目标配置数据列表
     */
    <T> List<T> selectList(ConfigDataEnum dataEnum, Class<T> clazz, Query<T> query, QueryOptions queryOptions, boolean showError);

    /**
     * 获取所有数据，根据 queryOptions 排序
     *
     * @param dataEnum     模型枚举
     * @param clazz        类型
     * @param queryOptions 查询选项，eg: order by
     * @param <T>          配置数据类型
     * @return 目标配置数据列表
     */
    <T> List<T> selectList(ConfigDataEnum dataEnum, Class<T> clazz, QueryOptions queryOptions);

    /**
     * 获取所有数据，根据 queryOptions 排序
     *
     * @param dataEnum     模型枚举
     * @param clazz        类型
     * @param queryOptions 查询选项，eg: order by
     * @param <T>          配置数据类型
     * @param showError    未查到是否打印错误
     * @return 目标配置数据列表
     */
    <T> List<T> selectList(ConfigDataEnum dataEnum, Class<T> clazz, QueryOptions queryOptions, boolean showError);

    /**
     * 随机获取一条记录
     *
     * @param dataEnum 模型枚举
     * @param clazz    类型
     * @param <T>      配置数据类型
     * @return 目标配置数据
     */
    <T> T selectOneRandom(ConfigDataEnum dataEnum, Class<T> clazz);

}

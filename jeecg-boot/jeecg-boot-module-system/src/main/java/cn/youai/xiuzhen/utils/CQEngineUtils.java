/*
 * Copyright (C) 2016 venshine.cn@gmail.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.youai.xiuzhen.utils;


import com.googlecode.cqengine.IndexedCollection;
import com.googlecode.cqengine.query.Query;
import com.googlecode.cqengine.query.option.QueryOptions;
import com.googlecode.cqengine.resultset.ResultSet;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * CQEngine工具类
 *
 * @author luopeihuan
 */
@Slf4j
public final class CQEngineUtils {

    private CQEngineUtils() {
    }

    /**
     * 查询满足条件的一条记录，查找不到返回空
     *
     * @param indexedCollection 索引和数据集合
     * @param query             查询条件
     * @param <T>
     * @return
     */
    public static <T> T selectOne(IndexedCollection<T> indexedCollection, Query<T> query) {
        return selectOne(indexedCollection, query, null);
    }

    /**
     * 查询满足条件的一条记录，查找不到返回空
     *
     * @param indexedCollection 索引和数据集合
     * @param query             查询条件
     * @param queryOptions      查询选项
     * @param <T>
     * @return
     */
    public static <T> T selectOne(IndexedCollection<T> indexedCollection, Query<T> query, QueryOptions queryOptions) {

        ResultSet<T> resultSet = retrieve(indexedCollection, query, queryOptions);
        if (resultSet != null && resultSet.isNotEmpty()) {
            for (T t : resultSet) {
                if (t != null) {
                    return t;
                }
            }
        }

        return null;
    }

    /**
     * 查询满足条件的数组
     *
     * @param indexedCollection 索引和数据集合
     * @param query             查询条件
     * @param <T>
     * @return
     */
    public static <T> List<T> selectList(IndexedCollection<T> indexedCollection, Query<T> query) {
        return selectList(indexedCollection, query, null);
    }

    public static <T> List<T> selectList(IndexedCollection<T> indexedCollection, Query<T> query, QueryOptions queryOptions) {

        List<T> resultList = new ArrayList<>();
        ResultSet<T> resultSet = retrieve(indexedCollection, query, queryOptions);
        if (resultSet != null && resultSet.isNotEmpty()) {
            for (T t : resultSet) {
                resultList.add(t);
            }
        }

        return resultList;
    }

    /**
     * 查询结果集
     *
     * @param indexedCollection
     * @param query
     * @param <T>
     * @return
     */
    public static <T> ResultSet<T> retrieve(IndexedCollection<T> indexedCollection, Query<T> query) {
        return retrieve(indexedCollection, query, null);
    }

    public static <T> ResultSet<T> retrieve(IndexedCollection<T> indexedCollection, Query<T> query, QueryOptions queryOptions) {
        ResultSet<T> resultSet = null;
        try {
            if (queryOptions == null) {
                resultSet = indexedCollection.retrieve(query);
            } else {
                resultSet = indexedCollection.retrieve(query, queryOptions);
            }
        } catch (Exception e) {
            log.error("retrieve error, query:" + query + ", options:" + queryOptions, e);
        }
        return resultSet;
    }
}

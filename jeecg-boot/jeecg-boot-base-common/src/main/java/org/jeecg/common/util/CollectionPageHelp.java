package org.jeecg.common.util;

import cn.hutool.core.collection.CollectionUtil;
import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName CollectionPageHelp
 * @Description CollectionPageHelp
 * @Author buliangliang
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2021/3/26 2:35 下午
 */
public class CollectionPageHelp {

    /**
     * 多集合分页
     * 超出结果查询范围，返回空集合
     */
    public static <E> List<E> pageBySubList(List<E> list, int pageSize, int currentPage) {
        if (pageSize <= 0 || currentPage <= 0 || CollectionUtil.isEmpty(list)) {
            return new ArrayList<>(0);
        }

        int totalCount = list.size();
        int totalPage = 0;

        List<E> subList;
        int m = totalCount % pageSize;
        if (m > 0) {
            totalPage = totalCount / pageSize + 1;
        } else {
            totalPage = totalCount / pageSize;
        }

        if (m == 0) {
            if (totalCount < pageSize * currentPage) {
                subList = list;
            } else {
                subList = list.subList((currentPage - 1) * pageSize, pageSize * currentPage);
            }
        } else {
            if (currentPage == totalPage) {
                subList = list.subList((currentPage - 1) * pageSize, totalCount);
            } else if (currentPage > totalPage) { // 下标大于当前最大分页
                subList = new ArrayList<>(0);
            }else {
                subList = list.subList((currentPage - 1) * pageSize, pageSize * currentPage);
            }
        }
        return subList;
    }
}

package org.jeecg.common.util;

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
     */

    public static <E> List<E> pageBySubList(List<E> list, int pageSize, int currentPage) {
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
            subList = list.subList((currentPage - 1) * pageSize, pageSize * (currentPage));
        } else {
            if (currentPage == totalPage) {
                subList = list.subList((currentPage - 1) * pageSize, totalCount);
            } else {
                subList = list.subList((currentPage - 1) * pageSize, pageSize * (currentPage));
            }
        }
        return subList;
    }
}

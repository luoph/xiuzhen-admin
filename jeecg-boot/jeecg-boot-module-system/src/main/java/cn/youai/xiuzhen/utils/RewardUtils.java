package cn.youai.xiuzhen.utils;

import cn.hutool.core.collection.CollUtil;
import cn.youai.server.model.ItemVO;

import java.util.*;
import java.util.stream.Collectors;

/**
 * RewardUtils
 *
 * @author ocean
 * @since 202303171132
 */
public final class RewardUtils {

    /**
     * 合并列表，相同id的道具数量相加
     *
     * @param list 道具列表
     * @return 合并后的掉落列表
     */
    public static List<ItemVO> merge(Collection<? extends ItemVO> list) {
        boolean canMerge = canMerge(list);
        List<ItemVO> resultList = new ArrayList<>();
        if (canMerge) {
            Map<Integer, List<ItemVO>> groupMap = groupByItemId(list);
            for (Map.Entry<Integer, List<ItemVO>> entry : groupMap.entrySet()) {
                List<ItemVO> items = entry.getValue();
                long totalNum = items.stream().mapToLong(ItemVO::getNum).sum();
                if (totalNum > 0) {
                    resultList.add(new ItemVO(entry.getKey(), totalNum));
                }
            }
        } else {
            if (CollUtil.isNotEmpty(list)) {
                resultList.addAll(list);
            }
        }
        return resultList;
    }

    private static boolean canMerge(Collection<? extends ItemVO> list) {
        if (CollUtil.isEmpty(list)) {
            return false;
        }

        Set<Integer> itemIdSet = new HashSet<>();
        for (ItemVO item : list) {
            if (item.getNum() == 0) {
                return true;
            }

            if (itemIdSet.contains(item.getItemId())) {
                return true;
            }
            itemIdSet.add(item.getItemId());
        }
        return false;
    }

    private static Map<Integer, List<ItemVO>> groupByItemId(Collection<? extends ItemVO> list) {
        return list.stream().filter(t -> t.getNum() > 0).collect(Collectors.groupingBy(ItemVO::getItemId, HashMap::new, Collectors.toCollection(ArrayList::new)));
    }
}

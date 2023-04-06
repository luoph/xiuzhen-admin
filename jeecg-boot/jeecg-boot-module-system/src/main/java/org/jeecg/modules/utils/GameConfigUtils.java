package org.jeecg.modules.utils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.youai.server.component.ConfigManager;
import cn.youai.server.conf.ConfItem;
import cn.youai.xiuzhen.conf.ConfMainStory;
import cn.youai.xiuzhen.conf.ConfMedicine;
import cn.youai.xiuzhen.conf.ConfRefineEquip;
import cn.youai.xiuzhen.core.config.GameConfig;
import com.googlecode.cqengine.query.Query;
import com.googlecode.cqengine.query.QueryFactory;
import com.googlecode.cqengine.query.logical.And;
import com.googlecode.cqengine.query.option.QueryOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.googlecode.cqengine.query.QueryFactory.equal;

/**
 * 配置查找工具类
 *
 * @author luopeihuan
 */
public final class GameConfigUtils {

    public static ConfItem getItemById(int itemId) {
        return ConfigManager.one(GameConfig.ITEM, ConfItem.class, equal(ConfItem.ITEM_ID, itemId), true);
    }

    public static List<ConfItem> allItemList() {
        return ConfigManager.all(GameConfig.ITEM, ConfItem.class);
    }

    public static List<ConfItem> getConfItemList(Integer itemId, String itemName) {
        if (itemId != null) {
            ConfItem confItem = GameConfigUtils.getItemById(itemId);
            if (confItem != null) {
                return CollUtil.newArrayList(confItem);
            }
            return CollUtil.newArrayList();
        } else if (StrUtil.isNotBlank(itemName)) {
            return GameConfigUtils.getItemListByName(itemName);
        }
        return allItemList();
    }

    public static List<ConfItem> getConfItemList(List<Integer> itemIdList, List<String> itemNameList) {
        return CollUtil.isNotEmpty(itemIdList) ? itemIdList.stream().map(GameConfigUtils::getItemById).filter(Objects::nonNull).collect(Collectors.toList())
                : CollUtil.isNotEmpty(itemNameList) ? itemNameList.stream().map(GameConfigUtils::getItemListByName).filter(CollUtil::isNotEmpty).collect(ArrayList::new, ArrayList::addAll, ArrayList::addAll)
                : allItemList();
    }

    public static List<ConfItem> getItemListByName(String itemName) {
        QueryOptions queryOptions = QueryFactory.queryOptions(QueryFactory.orderBy(QueryFactory.ascending(ConfItem.ITEM_ID)));
        return ConfigManager.list(GameConfig.ITEM, ConfItem.class, equal(ConfItem.NAME, itemName), queryOptions, true);
    }

    public static List<ConfMainStory> getConfMainStoryList(List<Integer> levelIds) {
        QueryOptions queryOptions = QueryFactory.queryOptions(QueryFactory.orderBy(QueryFactory.ascending(ConfMainStory.LEVEL)));
        Query<ConfMainStory> in = QueryFactory.in(ConfMainStory.LEVEL, levelIds);
        return ConfigManager.list(GameConfig.MAIN_STORY, ConfMainStory.class, in, queryOptions);
    }

    public static List<ConfMedicine> getConfMedicineList(List<Integer> ids) {
        QueryOptions queryOptions = QueryFactory.queryOptions(ConfMedicine.ITEM_ID);
        // 极品丹药
        Query<ConfMedicine> in = QueryFactory.in(ConfMedicine.ITEM_ID, ids);
        And<ConfMedicine> and = QueryFactory.and(in, QueryFactory.equal(ConfMedicine.QUA, 4));
        return ConfigManager.list(GameConfig.REFINE_MEDICINE, ConfMedicine.class, and, queryOptions);
    }

    public static List<ConfRefineEquip> getConfRefineEquipList(List<Integer> ids) {
        QueryOptions queryOptions = QueryFactory.queryOptions(QueryFactory.orderBy(QueryFactory.ascending(ConfRefineEquip.ITEM_ID)));
        Query<ConfRefineEquip> in = QueryFactory.in(ConfRefineEquip.ITEM_ID, ids);
        // 仙器以上
        Query<ConfRefineEquip> greaterThan = QueryFactory.greaterThan(ConfRefineEquip.SUIT_TYPE, 2);
        And<ConfRefineEquip> and = QueryFactory.and(in, greaterThan);
        return ConfigManager.list(GameConfig.REFINE_EQUIP, ConfRefineEquip.class, and, queryOptions);
    }

}

package cn.youai.xiuzhen.entity.pojo;

import cn.youai.basics.model.ConfigData;
import com.alibaba.fastjson2.annotation.JSONField;
import com.googlecode.cqengine.ConcurrentIndexedCollection;
import com.googlecode.cqengine.IndexedCollection;
import com.googlecode.cqengine.attribute.SimpleAttribute;
import com.googlecode.cqengine.index.hash.HashIndex;
import com.googlecode.cqengine.index.navigable.NavigableIndex;
import com.googlecode.cqengine.query.option.QueryOptions;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 炼器道具
 *
 * @author lijunchi
 * @version 1.0
 * @date 2021-5-10 17:58
 */
@Data
@EqualsAndHashCode()
public class ConfRefineEquip implements Serializable, ConfigData {

    private static final long serialVersionUID = 1L;

    public static final SimpleAttribute<ConfRefineEquip, Integer> ITEM_ID = new SimpleAttribute<ConfRefineEquip, Integer>("itemId") {
        @Override
        public Integer getValue(ConfRefineEquip model, QueryOptions queryOptions) {
            return model.itemId;
        }
    };

    public static final SimpleAttribute<ConfRefineEquip, Integer> RECIPE_ID = new SimpleAttribute<ConfRefineEquip, Integer>("recipeId") {
        @Override
        public Integer getValue(ConfRefineEquip model, QueryOptions queryOptions) {
            return model.recipeId;
        }
    };

    public static final SimpleAttribute<ConfRefineEquip, Integer> SUIT_TYPE = new SimpleAttribute<ConfRefineEquip, Integer>("suit_type") {
        @Override
        public Integer getValue(ConfRefineEquip model, QueryOptions queryOptions) {
            return model.suitType;
        }
    };

    @JSONField(name = "id")
    private Integer id;

    @JSONField(name = "item")
    private Integer itemId;

    @JSONField(name = "recipe_id")
    private Integer recipeId;

    @JSONField(name = "suit_type")
    private Integer suitType;

    public static IndexedCollection<ConfRefineEquip> indexedCollection() {
        IndexedCollection<ConfRefineEquip> indexedCollection = new ConcurrentIndexedCollection<>();
        indexedCollection.addIndex(HashIndex.onAttribute(ITEM_ID));
        indexedCollection.addIndex(NavigableIndex.onAttribute(RECIPE_ID));
        indexedCollection.addIndex(NavigableIndex.onAttribute(SUIT_TYPE));
        return indexedCollection;
    }
}

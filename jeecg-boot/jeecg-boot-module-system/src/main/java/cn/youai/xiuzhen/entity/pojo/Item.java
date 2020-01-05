package cn.youai.xiuzhen.entity.pojo;

import com.alibaba.fastjson.annotation.JSONField;
import com.googlecode.cqengine.ConcurrentIndexedCollection;
import com.googlecode.cqengine.IndexedCollection;
import com.googlecode.cqengine.attribute.SimpleAttribute;
import com.googlecode.cqengine.index.hash.HashIndex;
import com.googlecode.cqengine.index.navigable.NavigableIndex;
import com.googlecode.cqengine.query.option.QueryOptions;
import lombok.Data;

import java.io.Serializable;

/**
 * @author buliangliang
 */
@Data
public class Item implements Serializable {
    /**
     * 序列化id
     */
    private static final long serialVersionUID = -115158228256254L;

    public static final SimpleAttribute<Item, Integer> ITEM_ID = new SimpleAttribute<Item, Integer>("itemId") {
        @Override
        public Integer getValue(Item model, QueryOptions queryOptions) {
            return model.itemId;
        }
    };

    public static final SimpleAttribute<Item, Integer> TYPE = new SimpleAttribute<Item, Integer>("type") {
        @Override
        public Integer getValue(Item model, QueryOptions queryOptions) {
            return model.type;
        }
    };

    public static final SimpleAttribute<Item, Integer> QUALITY = new SimpleAttribute<Item, Integer>("quality") {
        @Override
        public Integer getValue(Item model, QueryOptions queryOptions) {
            return model.quality;
        }
    };

    public static IndexedCollection<Item> indexedCollection() {
        IndexedCollection<Item> indexedCollection = new ConcurrentIndexedCollection<>();
        indexedCollection.addIndex(HashIndex.onAttribute(ITEM_ID));
        indexedCollection.addIndex(NavigableIndex.onAttribute(TYPE));
        indexedCollection.addIndex(NavigableIndex.onAttribute(QUALITY));
        return indexedCollection;
    }

    /**
     * 配置表字段
     */
    private Integer id;

    @JSONField(name = "item_id")
    private Integer itemId;

    /**
     * 类型：1、货币 2、装备 3、礼包 4普通
     */
    @JSONField(name = "type")
    private Integer type;

    /**
     * 背包 BackpackType
     */
    @JSONField(name = "backpack")
    private Integer backpack;

    /**
     * 品质 ：1白 2绿 3蓝 4紫 5橙 6红
     */
    @JSONField(name = "quality")
    private Integer quality;

    @JSONField(name = "name")
    private String name;

    @JSONField(name = "tips")
    private String tips;

    @JSONField(name = "icon_1")
    private String icon1;

    @JSONField(name = "icon_2")
    private String icon2;

    @JSONField(name = "overlying")
    private Integer overlying;

    @JSONField(name = "lv")
    private Integer lv;

    @JSONField(name = "button")
    private String button;

    @JSONField(name = "link")
    private String link;

    @JSONField(name = "star")
    private Integer star;

    @JSONField(name = "offer")
    private Integer offer;
}

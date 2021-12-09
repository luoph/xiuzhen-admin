package cn.youai.xiuzhen.entity.pojo;

import cn.youai.server.model.ItemVO;
import cn.youai.server.model.ReadonlyRoleAttr;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.googlecode.cqengine.ConcurrentIndexedCollection;
import com.googlecode.cqengine.IndexedCollection;
import com.googlecode.cqengine.attribute.SimpleAttribute;
import com.googlecode.cqengine.index.hash.HashIndex;
import com.googlecode.cqengine.index.navigable.NavigableIndex;
import com.googlecode.cqengine.query.option.QueryOptions;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * 丹药
 *
 * @author lijunchi
 * @version 1.0
 * @date 2021-5-10 20:04
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ConfMedicine extends ReadonlyRoleAttr {

    private static final long serialVersionUID = 3440026283839693334L;

    public static final SimpleAttribute<ConfMedicine, Integer> ITEM_ID = new SimpleAttribute<ConfMedicine, Integer>("itemId") {
        @Override
        public Integer getValue(ConfMedicine model, QueryOptions queryOptions) {
            return model.itemId;
        }
    };

    public static final SimpleAttribute<ConfMedicine, Integer> RECIPE_ID = new SimpleAttribute<ConfMedicine, Integer>("recipeId") {
        @Override
        public Integer getValue(ConfMedicine model, QueryOptions queryOptions) {
            return model.recipeId;
        }
    };

    public static final SimpleAttribute<ConfMedicine, Integer> TYPE = new SimpleAttribute<ConfMedicine, Integer>("type") {
        @Override
        public Integer getValue(ConfMedicine model, QueryOptions queryOptions) {
            return model.type;
        }
    };

    public static final SimpleAttribute<ConfMedicine, Integer> STAR = new SimpleAttribute<ConfMedicine, Integer>("star") {
        @Override
        public Integer getValue(ConfMedicine model, QueryOptions queryOptions) {
            return model.star;
        }
    };

    public static final SimpleAttribute<ConfMedicine, Integer> QUA = new SimpleAttribute<ConfMedicine, Integer>("qua") {
        @Override
        public Integer getValue(ConfMedicine model, QueryOptions queryOptions) {
            return model.qua;
        }
    };

    public static IndexedCollection<ConfMedicine> indexedCollection() {
        IndexedCollection<ConfMedicine> indexedCollection = new ConcurrentIndexedCollection<>();
        indexedCollection.addIndex(HashIndex.onAttribute(ITEM_ID));
        indexedCollection.addIndex(NavigableIndex.onAttribute(RECIPE_ID));
        indexedCollection.addIndex(NavigableIndex.onAttribute(TYPE));
        indexedCollection.addIndex(NavigableIndex.onAttribute(STAR));
        indexedCollection.addIndex(NavigableIndex.onAttribute(QUA));
        return indexedCollection;
    }

    @JSONField(name = "id")
    private Integer id;

    @JSONField(name = "item")
    private Integer itemId;

    @JSONField(name = "recipe_id")
    private Integer recipeId;

    /**
     * 1.普通丹药
     * 2.仙丹
     * 3.不显示
     * 4.修为效率加成丹药
     */
    @JSONField(name = "type")
    private Integer type;

    /**
     * 阶数 1-9
     * eg：3阶丹药
     */
    @JSONField(name = "star")
    private Integer star;

    /**
     * 品质 1-4
     */
    @JSONField(name = "qua")
    private Integer qua;

    @Deprecated
    @JSONField(name = "maximum")
    private Integer maximum;

    /**
     * 加成修为效率持续时间，分钟
     */
    @JSONField(name = "time")
    private Integer time;

    /**
     * 分组
     */
    @JSONField(name = "group")
    private Integer group;

    @JSONField(name = "decompose")
    private String decompose;

    @JSONField(serialize = false, deserialize = false)
    private List<ItemVO> decomposeReward;

    @Override
    public void onload(JSONObject data, String tableName) {
        super.onload(data, tableName);
        if (StringUtils.isNotBlank(this.decompose)) {
            this.decomposeReward = JSONArray.parseArray(this.decompose, ItemVO.class);
        }
    }
}


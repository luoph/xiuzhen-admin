package cn.youai.xiuzhen.entity.pojo;

import cn.youai.xiuzhen.entity.common.WeightValuable;
import com.alibaba.fastjson.annotation.JSONField;
import com.googlecode.cqengine.ConcurrentIndexedCollection;
import com.googlecode.cqengine.IndexedCollection;
import com.googlecode.cqengine.attribute.SimpleAttribute;
import com.googlecode.cqengine.index.hash.HashIndex;
import com.googlecode.cqengine.index.navigable.NavigableIndex;
import com.googlecode.cqengine.query.option.QueryOptions;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 每日礼包
 * 
 * @author ocean
 * @since 20200615160808
 */
@Data
public class ConfDailyGiftType implements Serializable, WeightValuable {
    
    /**
     * 
     */
    private static final long serialVersionUID = 7658912591818587986L;
    
    public static final SimpleAttribute<ConfDailyGiftType, Integer> ID = new SimpleAttribute<ConfDailyGiftType, Integer>("id") {
        @Override
        public Integer getValue(ConfDailyGiftType model, QueryOptions queryOptions) {
            return model.id;
        }
    };
    
    public static final SimpleAttribute<ConfDailyGiftType, Integer> TYPE = new SimpleAttribute<ConfDailyGiftType, Integer>("type") {
        @Override
        public Integer getValue(ConfDailyGiftType model, QueryOptions queryOptions) {
            return model.type;
        }
    };
    
    public static final SimpleAttribute<ConfDailyGiftType, Integer> GIFT_TYPE = new SimpleAttribute<ConfDailyGiftType, Integer>("giftType") {
        @Override
        public Integer getValue(ConfDailyGiftType model, QueryOptions queryOptions) {
            return model.giftType;
        }
    };
    
    public static final SimpleAttribute<ConfDailyGiftType, Integer> OPEN_TIME = new SimpleAttribute<ConfDailyGiftType, Integer>("openTime") {
        @Override
        public Integer getValue(ConfDailyGiftType model, QueryOptions queryOptions) {
            return model.openTime;
        }
    };

    public static IndexedCollection<ConfDailyGiftType> indexedCollection() {
        IndexedCollection<ConfDailyGiftType> indexedCollection = new ConcurrentIndexedCollection<>();
        indexedCollection.addIndex(HashIndex.onAttribute(ID));
        indexedCollection.addIndex(NavigableIndex.onAttribute(TYPE));
        indexedCollection.addIndex(NavigableIndex.onAttribute(GIFT_TYPE));
        indexedCollection.addIndex(NavigableIndex.onAttribute(OPEN_TIME));
        return indexedCollection;
    }

    @JSONField(name = "id")
    private Integer id;
    
    @JSONField(name = "type")
    private Integer type;
    
    @JSONField(name = "name")
    private String name;
    
    @JSONField(name = "weight")
    private Integer weight;
    
    @JSONField(name = "gift_id")
    private List<Integer> giftId;
    
    /**
     * {@link cn.youai.xiuzhen.dailygift.constant.DailyGiftType}
     */
    @JSONField(name = "gift_type")
    private Integer giftType;
    
    /**
     * 开服第n天，第1天为0
     */
    @JSONField(name = "open_time")
    private Integer openTime;

    @Override
    public Integer weightValue() {
        return weight;
    }
}

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
 * 仙职
 * 
 * @author ocean
 * @since 20191112162032
 */
@Data
public class ConfImmortalType implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7145184477113248002L;

	public static final SimpleAttribute<ConfImmortalType, Integer> ID = new SimpleAttribute<ConfImmortalType, Integer>("id") {
		@Override
		public Integer getValue(ConfImmortalType model, QueryOptions queryOptions) {
			return model.id;
		}
	};
	
	public static final SimpleAttribute<ConfImmortalType, Integer> TYPE_ID = new SimpleAttribute<ConfImmortalType, Integer>("typeId") {
        @Override
        public Integer getValue(ConfImmortalType model, QueryOptions queryOptions) {
            return model.typeId;
        }
    };
    
    public static final SimpleAttribute<ConfImmortalType, Integer> RECHARGE_GOODS_ID = new SimpleAttribute<ConfImmortalType, Integer>("rechargeGoodsId") {
        @Override
        public Integer getValue(ConfImmortalType model, QueryOptions queryOptions) {
            return model.goodsId;
        }
    };

	public static IndexedCollection<ConfImmortalType> indexedCollection() {
		IndexedCollection<ConfImmortalType> indexedCollection = new ConcurrentIndexedCollection<>();
		indexedCollection.addIndex(HashIndex.onAttribute(ID));
		indexedCollection.addIndex(NavigableIndex.onAttribute(TYPE_ID));
		indexedCollection.addIndex(NavigableIndex.onAttribute(RECHARGE_GOODS_ID));
		return indexedCollection;
	}

	@JSONField(name = "id")
	private Integer id;

	@JSONField(name = "type_id")
	private Integer typeId;

	@JSONField(name = "immortal_name")
	private String immortalName;

	@JSONField(name = "goods_id")
	private Integer goodsId;

	// 周期（天数）, e.g. 30.30天, 0.无期限
	@JSONField(name = "expiry_date")
	private Integer expiryDate;

}

package cn.youai.xiuzhen.entity.pojo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.googlecode.cqengine.ConcurrentIndexedCollection;
import com.googlecode.cqengine.IndexedCollection;
import com.googlecode.cqengine.attribute.SimpleAttribute;
import com.googlecode.cqengine.index.hash.HashIndex;
import com.googlecode.cqengine.query.option.QueryOptions;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

/**
 * @ClassName ConfDailyGiftPackage
 * @Description 每日礼包
 * @Author buliangliang
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2020-04-07 15:51
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ConfDailyGiftPackage extends ConfSimpleReward {

    private static final long serialVersionUID = 1933913188058349182L;

    public static final SimpleAttribute<ConfDailyGiftPackage, Integer> ID = new SimpleAttribute<ConfDailyGiftPackage, Integer>("id") {
        @Override
        public Integer getValue(ConfDailyGiftPackage model, QueryOptions queryOptions) {
            return model.getId();
        }
    };

    public static final SimpleAttribute<ConfDailyGiftPackage, Integer> GOODS_ID = new SimpleAttribute<ConfDailyGiftPackage, Integer>("rechargeGoodsId") {
        @Override
        public Integer getValue(ConfDailyGiftPackage model, QueryOptions queryOptions) {
            return model.goodsId;
        }
    };

    public static IndexedCollection<ConfDailyGiftPackage> indexedCollection() {
        IndexedCollection<ConfDailyGiftPackage> indexedCollection = new ConcurrentIndexedCollection<>();
        indexedCollection.addIndex(HashIndex.onAttribute(ID));
        indexedCollection.addIndex(HashIndex.onAttribute(GOODS_ID));
        return indexedCollection;
    }

    @JSONField(name = "type_id")
    private Integer typeId;

    @JSONField(name = "goods_id")
    private Integer goodsId;

    @JSONField(name = "stock")
    private Integer stock;

    @JSONField(name = "price")
    private String price;

    @JSONField(deserialize = false, serialize = false)
    private ItemVo costItem;

    @Override
    public void onload(JSONObject data) {
        if (StringUtils.isNotBlank(this.price)) {
            this.costItem = JSON.parseObject(this.price, ItemVo.class);
        }
    }
}
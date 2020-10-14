package cn.youai.xiuzhen.entity.pojo;

import cn.youai.xiuzhen.entity.common.ConfigData;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.googlecode.cqengine.ConcurrentIndexedCollection;
import com.googlecode.cqengine.IndexedCollection;
import com.googlecode.cqengine.attribute.SimpleAttribute;
import com.googlecode.cqengine.index.hash.HashIndex;
import com.googlecode.cqengine.query.option.QueryOptions;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName ConfMonthCardType
 * @Description 月卡类型
 * @Author buliangliang
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2020-03-30 10:51
 */
@Data
public class ConfMonthCardType implements Serializable, ConfigData {

    private static final long serialVersionUID = -3742326095308773538L;

    public static final SimpleAttribute<ConfMonthCardType, Integer> ID = new SimpleAttribute<ConfMonthCardType, Integer>("id") {
        @Override
        public Integer getValue(ConfMonthCardType model, QueryOptions queryOptions) {
            return model.id;
        }
    };

    public static final SimpleAttribute<ConfMonthCardType, Integer> GOODS_ID = new SimpleAttribute<ConfMonthCardType, Integer>("rechargeGoodsId") {
        @Override
        public Integer getValue(ConfMonthCardType model, QueryOptions queryOptions) {
            return model.goodsId;
        }
    };

    public static IndexedCollection<ConfMonthCardType> indexedCollection() {
        IndexedCollection<ConfMonthCardType> indexedCollection = new ConcurrentIndexedCollection<>();
        indexedCollection.addIndex(HashIndex.onAttribute(ID));
        indexedCollection.addIndex(HashIndex.onAttribute(GOODS_ID));
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
    @JSONField(name = "expiry_date")
    private Integer expiryDate;
    @JSONField(name = "daily_reward")
    private String dailyReward;
    @JSONField(name = "email_id")
    private Integer emailId;

    @JSONField(serialize = false, deserialize = false)
    private List<ItemVo> items;

   /* @Override
    public void onload() {
        if (StringUtils.isNotEmpty(this.dailyReward)) {
            this.items = JSON.parseArray(this.dailyReward, ItemVo.class);
        }
    }*/
    @Override
    public void onload(JSONObject data) {
        if (StringUtils.isNotEmpty(this.dailyReward)) {
            this.items = JSON.parseArray(this.dailyReward, ItemVo.class);
        }
    }
}

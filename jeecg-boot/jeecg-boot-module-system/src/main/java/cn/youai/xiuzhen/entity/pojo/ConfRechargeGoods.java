package cn.youai.xiuzhen.entity.pojo;

import cn.youai.xiuzhen.entity.common.ConfigData;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.googlecode.cqengine.ConcurrentIndexedCollection;
import com.googlecode.cqengine.IndexedCollection;
import com.googlecode.cqengine.attribute.SimpleAttribute;
import com.googlecode.cqengine.index.hash.HashIndex;
import com.googlecode.cqengine.index.navigable.NavigableIndex;
import com.googlecode.cqengine.query.option.QueryOptions;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 充值商品
 *
 * @author luopeihuan
 */
@Data
public class ConfRechargeGoods implements ConfigData, Serializable {

    /**
     * 序列化
     */
    private static final long serialVersionUID = -9109637694928147341L;


    public static final SimpleAttribute<ConfRechargeGoods, Integer> ID = new SimpleAttribute<ConfRechargeGoods, Integer>("id") {
        @Override
        public Integer getValue(ConfRechargeGoods model, QueryOptions queryOptions) {
            return model.id;
        }
    };

    public static final SimpleAttribute<ConfRechargeGoods, Integer> AMOUNT_STAT = new SimpleAttribute<ConfRechargeGoods, Integer>("amountStat") {
        @Override
        public Integer getValue(ConfRechargeGoods model, QueryOptions queryOptions) {
            return model.amountStat;
        }
    };

    public static final SimpleAttribute<ConfRechargeGoods, Integer> GOODS_TYPE = new SimpleAttribute<ConfRechargeGoods, Integer>("goodsType") {
        @Override
        public Integer getValue(ConfRechargeGoods model, QueryOptions queryOptions) {
            return model.goodsType;
        }
    };

    public static IndexedCollection<ConfRechargeGoods> indexedCollection() {
        IndexedCollection<ConfRechargeGoods> indexedCollection = new ConcurrentIndexedCollection<>();
        indexedCollection.addIndex(HashIndex.onAttribute(ID));
        indexedCollection.addIndex(NavigableIndex.onAttribute(AMOUNT_STAT));
        indexedCollection.addIndex(NavigableIndex.onAttribute(GOODS_TYPE));
        return indexedCollection;
    }

    /**
     * 商品 id
     */
    @JSONField(name = "id")
    private Integer id;

    /**
     * 是否记入累充（0 - 不计入 1 - 记入）
     */
    @JSONField(name = "amount_stat")
    private Integer amountStat;

    /**
     * 充值分类
     * {@link cn.youai.xiuzhen.entity.common.RechargeGoodsType}
     */
    @JSONField(name = "goods_type")
    private Integer goodsType;

    /**
     * 货币类型
     */
    @JSONField(name = "currency")
    private Integer currency;

    /**
     * 价格
     */
    @JSONField(name = "price")
    private BigDecimal price;

    /**
     * 折扣后价格
     */
    @JSONField(name = "discount")
    private BigDecimal discount;

    /**
     * 游戏币与人民币(元)的兑换比例
     */
    @JSONField(name = "exchange")
    private BigDecimal exchange;

    /**
     * 商品名称
     */
    @JSONField(name = "name")
    private String name;

    /**
     * 获得的商品
     */
    @JSONField(name = "items")
    private String items;

    /**
     * 首次额外赠送
     */
    @JSONField(name = "addition")
    private String addition;

    /**
     * 备注
     */
    @JSONField(name = "remark")
    private String remark;


    /**
     * 解析goods对应的道具
     */
    @JSONField(serialize = false, deserialize = false)
    private List<ItemVO> itemList;

    /**
     * 解析addition对应的道具
     */
    @JSONField(serialize = false, deserialize = false)
    private List<ItemVO> additionList;

    @Override
    public void onload(JSONObject data) {
        if (StringUtils.isNotEmpty(this.items)) {
            this.itemList = JSON.parseArray(this.items, ItemVO.class);
        }

        if (StringUtils.isNotEmpty(this.addition)) {
            this.additionList = JSON.parseArray(this.addition, ItemVO.class);
        }
    }
}

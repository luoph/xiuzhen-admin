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
 * 仙职特权
 * 
 * @author ocean
 * @since 20200529181930
 */
@Data
public class ConfImmortalPrivilege implements Serializable {
    
    /**
     * 
     */
    private static final long serialVersionUID = 997769377508952175L;

    public static final SimpleAttribute<ConfImmortalPrivilege, Integer> ID = new SimpleAttribute<ConfImmortalPrivilege, Integer>("id") {
        @Override
        public Integer getValue(ConfImmortalPrivilege model, QueryOptions queryOptions) {
            return model.id;
        }
    };
    
    public static final SimpleAttribute<ConfImmortalPrivilege, Integer> TYPE_ID = new SimpleAttribute<ConfImmortalPrivilege, Integer>("typeId") {
        @Override
        public Integer getValue(ConfImmortalPrivilege model, QueryOptions queryOptions) {
            return model.typeId;
        }
    };
    
    public static IndexedCollection<ConfImmortalPrivilege> indexedCollection() {
        IndexedCollection<ConfImmortalPrivilege> indexedCollection = new ConcurrentIndexedCollection<>();
        indexedCollection.addIndex(HashIndex.onAttribute(ID));
        indexedCollection.addIndex(NavigableIndex.onAttribute(TYPE_ID));
        return indexedCollection;
    }
    
    @JSONField(name = "id")
    private Integer id;

    /**
     * immortal_type.type_id
     * 同类型的官职特权可以叠加
     */
    @JSONField(name = "type_id")
    private Integer typeId;
    
    @JSONField(name = "name")
    private String name;
    
    /**
     * 加修为效率 +5%, +10%
     */
    @JSONField(name = "practice")
    private Integer practice;
    
    /**
     * 渡劫成功率+5%（最终总成功率不超过100%）
     */
    @JSONField(name = "practice_rate")
    private Integer practiceRate;
    
    /**
     * 奇兽山挑战次数+1
     */
    @JSONField(name = "pet_challenge_num")
    private Integer petChallengeNum;
    
    /**
     * 剧情boss挑战次数
     */
    @JSONField(name = "boss_story_num")
    private Integer bossStoryNum;
    
    /**
     * 游历仙山免费提升品质次数 +n
     */
    @JSONField(name = "travel_hill_free_up_num")
    private Integer travelHillFreeUpNum;
    
    /**
     * 快速冒险次数
     */
    @JSONField(name = "quick_type_times")
    private Integer quickTypeTimes;
    
    /**
     * 剧情游历额外时间，单位/分钟
     */
    @JSONField(name = "story_travel_extra_time")
    private Integer storyTravelExtraTime;
    
    /**
     * 收费快速游历次数
     */
    @JSONField(name = "quick_story_travel")
    private Integer quickStoryTravel;
    
    /**
     * 收费快速游历次数
     */
    @JSONField(name = "free_quick_story_travel")
    private Integer freeQuickStoryTravel;

    /**
     * 仙器秘境挑战购买次数
     */
    @JSONField(name = "tier_map_num")
    private Integer tierMapNum;

    /**
     * 跳过渡劫动画特权
     */
    @JSONField(name = "skip_practice")
    private Integer skipPractice;

    /**
     * 称号
     */
    @JSONField(name = "title")
    private String title;
    
}

package cn.youai.xiuzhen.entity.pojo;

import cn.youai.server.model.ItemVO;
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
import java.util.List;

/**
 * @ClassName ConfMainStory
 * @Author lijunchi
 * @Description json main_story
 * @Date 2020/4/10 9:56
 * @Version 1.0
 */
@Data
public class ConfMainStory implements Serializable, ConfigData {
    /**
     * 序列化id
     */
    private static final long serialVersionUID = -5498993428282090432L;

    public static final SimpleAttribute<ConfMainStory, Integer> ID = new SimpleAttribute<ConfMainStory, Integer>("id") {
        @Override
        public Integer getValue(ConfMainStory model, QueryOptions queryOptions) {
            return model.id;
        }
    };

    public static final SimpleAttribute<ConfMainStory, Integer> LEVEL = new SimpleAttribute<ConfMainStory, Integer>("level") {
        @Override
        public Integer getValue(ConfMainStory model, QueryOptions queryOptions) {
            return model.level;
        }
    };

    private Integer id;

    @JSONField(name = "level")
    private Integer level;

    @JSONField(name = "prime_level")
    private Integer primeLevel;

    @JSONField(name = "prime_level")
    private Integer preLevel;

    @JSONField(name = "show_grade")
    private Integer showGrade;

    @JSONField(name = "unlock_grade")
    private Integer unlockGrade;

    @JSONField(name = "unlock_level")
    private Integer unlockLevel;

    @JSONField(name = "plot")
    private Integer plot;

    @JSONField(name = "chapter_num")
    private String chapterNum;

    @JSONField(name = "rumor")
    private Integer rumor;

    @JSONField(name = "chapter_name")
    private String chapterName;

    @JSONField(name = "reward")
    private Integer reward;

    @JSONField(name = "help_battle_reward")
    private String helpBattleReward;

    @JSONField(name = "show_mvp")
    private Integer showMvp;

    @JSONField(serialize = false, deserialize = false)
    private List<ItemVO> itemList;

    public static IndexedCollection<ConfMainStory> indexedCollection() {
        IndexedCollection<ConfMainStory> indexedCollection = new ConcurrentIndexedCollection<>();
        indexedCollection.addIndex(HashIndex.onAttribute(ID));
        indexedCollection.addIndex(NavigableIndex.onAttribute(LEVEL));
        return indexedCollection;
    }

    @Override
    public void onload(JSONObject data) {
        if (StringUtils.isNotEmpty(this.helpBattleReward)) {
            this.itemList = JSON.parseArray(this.helpBattleReward, ItemVO.class);
        }
    }
}


package cn.youai.xiuzhen.entity.pojo;

import cn.youai.basics.model.ConfigData;
import cn.youai.server.model.ItemVO;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author luopeihuan
 */
@Data
public class ConfSimpleReward implements Serializable, ConfigData {

    private static final long serialVersionUID = 1106438727155776104L;

    private Integer id;

    @JSONField(name = "reward")
    private String reward;

    @JSONField(serialize = false, deserialize = false)
    private List<ItemVO> rewardList;

    /*@Override
    public void onload() {
        rewardList = JSON.parseArray(this.reward, ItemVo.class);
    }*/

    @Override
    public void onload(JSONObject data, String tableName) {
        rewardList = JSON.parseArray(this.reward, ItemVO.class);
    }
}

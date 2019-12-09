package org.jeecg.common.util.jsonschema.validate;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecg.common.util.jsonschema.CommonProperty;

import java.util.HashMap;
import java.util.Map;

/**
 * 字典属性
 *
 * @author 86729
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class HiddenProperty extends CommonProperty {

    private static final long serialVersionUID = -8939298551502162479L;

    public HiddenProperty() {
    }

    public HiddenProperty(String key, String title) {
        this.type = "string";
        this.view = "hidden";
        this.key = key;
        this.title = title;
    }

    @Override
    public Map<String, Object> getPropertyJson() {
        Map<String, Object> map = new HashMap<>();
        map.put("key", getKey());
        JSONObject prop = getCommonJson();
        prop.put("hidden", true);
        map.put("prop", prop);
        return map;
    }

}

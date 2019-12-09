package org.jeecg.common.util.jsonschema;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * JsonSchema 模式类
 * < http://json-schema.org/draft-07/schema# >
 */
@Data
@Accessors(chain = true)
public class JsonSchemaDescrip implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 7682073117441544718L;


    private String schema = "http://json-schema.org/draft-07/schema#";

    /**
     * 用它给我们的模式提供了标题。
     */
    private String title;

    /**
     * 关于模式的描述。
     */
    private String description;

    /**
     * type 关键字在我们的 JSON 数据上定义了第一个约束：必须是一个 JSON 对象。 可以直接设置成object
     */
    private String type;

    private List<String> required;

    public JsonSchemaDescrip() {
    }

    public JsonSchemaDescrip(List<String> required) {
        this.description = "我是一个jsonschema description";
        this.title = "我是一个jsonschema title";
        this.type = "object";
        this.required = required;
    }

}

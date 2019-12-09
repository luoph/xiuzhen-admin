package org.jeecg.common.system.query;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
public class QueryCondition implements Serializable {

    private static final long serialVersionUID = 4740166316629191651L;

    private String field;
    private String type;
    private String rule;
    private String val;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (field == null || "".equals(field)) {
            return "";
        }
        sb.append(this.field).append(" ").append(this.rule).append(" ").append(this.type).append(" ").append(this.val);
        return sb.toString();
    }
}

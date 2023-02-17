package org.jeecg.common.system.vo;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * @Description: 字典类
 * @author: jeecg-boot
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class DictModel implements Serializable {
    private static final long serialVersionUID = 1L;

    public DictModel() {
        this.pretty = false;
    }

    public DictModel(String value, String text) {
        this.value = value;
        this.text = text;
        this.pretty = false;
    }

    /**
     * 字典value
     */
    private String value;
    /**
     * 字典文本
     */
    private String text;

    private boolean pretty;

    /**
     * 特殊用途： JgEditableTable
     *
     * @return
     */
    public String getTitle() {
        return this.text;
    }

    /**
     * 特殊用途： vue3 Select组件
     */
    public String getLabel() {
        return this.text;
    }

    public static void prettyText(DictModel model) {
        if (!model.pretty && !StrUtil.equals(model.getText(), model.getValue())) {
            model.setText(model.getText() + "[" + model.getValue() + "]");
            model.pretty = true;
        }
    }

    public static List<DictModel> prettyText(List<DictModel> list) {
        if (CollUtil.isNotEmpty(list)) {
            list.forEach(DictModel::prettyText);
        }
        return list;
    }
}

package org.jeecg.modules.system.model;

import lombok.Data;
import lombok.experimental.Accessors;
import org.jeecg.modules.system.entity.SysPermission;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 树形列表用到
 */
@Data
@Accessors(chain = true)
public class TreeModel implements Serializable {

    private static final long serialVersionUID = 4013193970046502756L;

    private String key;

    private String title;

    private String slotTitle;

    private boolean isLeaf;

    private String icon;

    private Integer ruleFlag;

    private Map<String, String> scopedSlots;

    private List<TreeModel> children;

    private String parentId;

    private String label;

    private String value;

    public TreeModel() {
    }

    public TreeModel(SysPermission permission) {
        this.key = permission.getId();
        this.icon = permission.getIcon();
        this.parentId = permission.getParentId();
        this.title = permission.getName();
        this.slotTitle = permission.getName();
        this.value = permission.getId();
        this.isLeaf = permission.isLeaf();
        this.label = permission.getName();
        if (!permission.isLeaf()) {
            this.children = new ArrayList<>();
        }
    }

    public TreeModel(String key, String parentId, String slotTitle, Integer ruleFlag, boolean isLeaf) {
        this.key = key;
        this.parentId = parentId;
        this.ruleFlag = ruleFlag;
        this.slotTitle = slotTitle;
        Map<String, String> map = new HashMap<>();
        map.put("title", "hasDatarule");
        this.scopedSlots = map;
        this.isLeaf = isLeaf;
        this.value = key;
        if (!isLeaf) {
            this.children = new ArrayList<>();
        }
    }
}

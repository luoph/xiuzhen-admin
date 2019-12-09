package org.jeecg.modules.system.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * 树形下拉框
 */
@Data
@Accessors(chain = true)
public class TreeSelectModel implements Serializable {

    private static final long serialVersionUID = 9016390975325574747L;

    private String key;

    private String title;

    private boolean isLeaf;

    private String icon;

    private String parentId;

    private String value;

    private String code;

    private List<TreeSelectModel> children;
}

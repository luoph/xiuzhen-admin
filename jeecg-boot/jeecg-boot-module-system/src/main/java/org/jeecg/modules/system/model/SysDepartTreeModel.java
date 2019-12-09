package org.jeecg.modules.system.model;

import lombok.Data;
import org.jeecg.modules.system.entity.SysDepart;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 部门表 存储树结构数据的实体类
 * <p>
 *
 * @author Steve
 * @since 2019-01-22
 */
@Data
public class SysDepartTreeModel implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 对应SysDepart中的id字段,前端数据树中的key
     */
    private String key;

    /**
     * 对应SysDepart中的id字段,前端数据树中的value
     */
    private String value;

    /**
     * 对应depart_name字段,前端数据树中的title
     */
    private String title;


    private boolean isLeaf;
    // 以下所有字段均与SysDepart相同

    private String id;

    private String parentId;

    private String departName;

    private String departNameEn;

    private String departNameAbbr;

    private Integer departOrder;

    private Object description;

    private String orgCategory;

    private String orgType;

    private String orgCode;

    private String mobile;

    private String fax;

    private String address;

    private String memo;

    private String status;

    private String delFlag;

    private String createBy;

    private Date createTime;

    private String updateBy;

    private Date updateTime;

    private List<SysDepartTreeModel> children = new ArrayList<>();


    /**
     * 将SysDepart对象转换成SysDepartTreeModel对象
     *
     * @param sysDepart
     */
    public SysDepartTreeModel(SysDepart sysDepart) {
        this.key = sysDepart.getId();
        this.value = sysDepart.getId();
        this.title = sysDepart.getDepartName();
        this.id = sysDepart.getId();
        this.parentId = sysDepart.getParentId();
        this.departName = sysDepart.getDepartName();
        this.departNameEn = sysDepart.getDepartNameEn();
        this.departNameAbbr = sysDepart.getDepartNameAbbr();
        this.departOrder = sysDepart.getDepartOrder();
        this.description = sysDepart.getDescription();
        this.orgCategory = sysDepart.getOrgCategory();
        this.orgType = sysDepart.getOrgType();
        this.orgCode = sysDepart.getOrgCode();
        this.mobile = sysDepart.getMobile();
        this.fax = sysDepart.getFax();
        this.address = sysDepart.getAddress();
        this.memo = sysDepart.getMemo();
        this.status = sysDepart.getStatus();
        this.delFlag = sysDepart.getDelFlag();
        this.createBy = sysDepart.getCreateBy();
        this.createTime = sysDepart.getCreateTime();
        this.updateBy = sysDepart.getUpdateBy();
        this.updateTime = sysDepart.getUpdateTime();
    }

    public void setChildren(List<SysDepartTreeModel> children) {
        if (children == null) {
            this.isLeaf = true;
        }
        this.children = children;
    }

    public SysDepartTreeModel() {
    }

    /**
     * 重写equals方法
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SysDepartTreeModel model = (SysDepartTreeModel) o;
        return Objects.equals(id, model.id) &&
                Objects.equals(parentId, model.parentId) &&
                Objects.equals(departName, model.departName) &&
                Objects.equals(departNameEn, model.departNameEn) &&
                Objects.equals(departNameAbbr, model.departNameAbbr) &&
                Objects.equals(departOrder, model.departOrder) &&
                Objects.equals(description, model.description) &&
                Objects.equals(orgCategory, model.orgCategory) &&
                Objects.equals(orgType, model.orgType) &&
                Objects.equals(orgCode, model.orgCode) &&
                Objects.equals(mobile, model.mobile) &&
                Objects.equals(fax, model.fax) &&
                Objects.equals(address, model.address) &&
                Objects.equals(memo, model.memo) &&
                Objects.equals(status, model.status) &&
                Objects.equals(delFlag, model.delFlag) &&
                Objects.equals(createBy, model.createBy) &&
                Objects.equals(createTime, model.createTime) &&
                Objects.equals(updateBy, model.updateBy) &&
                Objects.equals(updateTime, model.updateTime) &&
                Objects.equals(children, model.children);
    }

    /**
     * 重写hashCode方法
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, parentId, departName, departNameEn, departNameAbbr,
                departOrder, description, orgCategory, orgType, orgCode, mobile, fax, address,
                memo, status, delFlag, createBy, createTime, updateBy, updateTime,
                children);
    }

}

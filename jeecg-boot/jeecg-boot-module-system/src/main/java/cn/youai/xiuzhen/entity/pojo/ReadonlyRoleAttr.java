package cn.youai.xiuzhen.entity.pojo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 只读配置属性
 *
 * @author luopeihuan
 * @version 1.0
 * @date 2019-10-16.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ReadonlyRoleAttr extends RoleAttr {

    private boolean isReadOnly = false;

    /**
     * 序列化字段
     */
    private static final long serialVersionUID = 1428455004654564977L;

    public ReadonlyRoleAttr() {
        super.reset();
    }

    public ReadonlyRoleAttr(MapValue mapValue) {
        super.reset();
        super.copy(mapValue);
    }

    @Override
    public void reset() {
        // DO Nothing
    }

    @Override
    public void clearProperty(RoleAttrType attrType) {
        if (isReadOnly) {
            notSupport();
            return;
        }

        super.clearProperty(attrType);
    }

    @Override
    public void setProperty(RoleAttrType attrType, Number value) {
        if (isReadOnly) {
            notSupport();
            return;
        }

        super.setProperty(attrType, value);
    }

    @Override
    public void setProperty(RoleAttrType attrType, String value) {
        if (isReadOnly) {
            notSupport();
            return;
        }

        super.setProperty(attrType, value);
    }

    @Override
    public void copy(MapValue mapValue) {
        if (isReadOnly) {
            notSupport();
            return;
        }

        super.copy(mapValue);
    }

    @Override
    public RoleAttr clearAdditionProperty() {
        if (isReadOnly) {
            notSupport();
            return this;
        }

        return super.clearAdditionProperty();
    }

    @Override
    public RoleAttr clearNoneEffectProperty() {
        if (isReadOnly) {
            notSupport();
            return this;
        }

        return super.clearNoneEffectProperty();
    }

    @Override
    public RoleAttr clearBaseProperty() {
        if (isReadOnly) {
            notSupport();
            return this;
        }

        return super.clearBaseProperty();
    }

    private void notSupport() {
        throw new RuntimeException(getClass().getSimpleName() + " is readonly!!!");
    }
}

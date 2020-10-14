package cn.youai.xiuzhen.entity.pojo;

import cn.youai.xiuzhen.entity.common.ConfigData;
import cn.youai.xiuzhen.utils.NumberUtils;
import com.alibaba.fastjson.JSONObject;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author ocean
 * @since 20200320150420
 */
@Getter
public class MapValue implements Serializable, ConfigData {
    /**
     * 序列化字段
     */
    private static final long serialVersionUID = 6514090844530607581L;

    protected Map<String, BigDecimal> values = new ConcurrentHashMap<>();

    public MapValue() {
    }

    public MapValue(JSONObject object) {
        onload(object);
    }

    public void reset() {
        values.clear();
    }

    public long longValue(RoleAttrType attrType) {
        return getProperty(attrType).longValue();
    }

    public int intValue(RoleAttrType attrType) {
        return getProperty(attrType).intValue();
    }

    public double doubleValue(RoleAttrType attrType) {
        return getProperty(attrType).doubleValue();
    }

    public BigDecimal getProperty(RoleAttrType attrType) {
        BigDecimal value = values.get(attrType.getProperty());
        if (value == null) {
            return BigDecimal.ZERO;
        }

        return value;
    }

    public void setProperty(RoleAttrType attrType, String value) {
        if (StringUtils.isNotEmpty(value)) {
            values.put(attrType.getProperty(), new BigDecimal(value));
        }
    }

    public void clearProperty(RoleAttrType attrType) {
        values.remove(attrType.getProperty());
    }

    @SuppressWarnings("rawtypes")
    public void setProperty(RoleAttrType attrType, Number value) {
        if (value != null) {
            Class fieldType = attrType.getClazz();
            if (NumberUtils.isZero(value)) {
                // 0 值直接删除
                values.remove(attrType.getProperty());
            } else {
                if (fieldType == int.class || fieldType == Integer.class) {
                    values.put(attrType.getProperty(), BigDecimal.valueOf(value.intValue()));
                } else if (fieldType == double.class || fieldType == Double.class) {
                    values.put(attrType.getProperty(), BigDecimal.valueOf(value.doubleValue()));
                } else if (fieldType == long.class || fieldType == Long.class) {
                    values.put(attrType.getProperty(), BigDecimal.valueOf(value.longValue()));
                } else if (fieldType == short.class || fieldType == Short.class) {
                    values.put(attrType.getProperty(), BigDecimal.valueOf(value.intValue()));
                } else if (fieldType == float.class || fieldType == Float.class) {
                    values.put(attrType.getProperty(), BigDecimal.valueOf(value.floatValue()));
                } else {
                    values.put(attrType.getProperty(), new BigDecimal(String.valueOf(value)));
                }
            }
        }
    }

    public void copy(MapValue mapValue) {
        if (mapValue == null) {
            return;
        }

        reset();
        values.putAll(mapValue.getValues());
    }


    @Override
    public void onload(JSONObject data) {
        for (RoleAttrType e : RoleAttrType.values()) {
            String value = data.getString(e.getColumn());
            setProperty(e, value);
        }
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{values=" + values + '}';
    }
}

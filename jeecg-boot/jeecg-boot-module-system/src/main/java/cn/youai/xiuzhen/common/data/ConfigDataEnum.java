package cn.youai.xiuzhen.common.data;

import cn.youai.xiuzhen.entity.pojo.*;
import lombok.Getter;

/**
 * @author buliangliang
 * 数据表 实体类  modulecode映射关系 类似于数据字典
 * 后续可将其改成xml的形式配置
 */
@SuppressWarnings("rawtypes")
@Getter
public enum ConfigDataEnum {

    /** =================== 以下是道具&修为相关 =================== */
    /**
     * 道具
     */
    /** =================== 以下是道具&修为相关 =================== */
    /**
     * 道具
     */
    ITEM("item", ConfItem.class),

    /**
     * 每日礼包
     */
    DAILY_GIFT_PACKAGE("day_gift_type", ConfDailyGiftPackage.class),

    DAILY_GIFT_TYPE("daily_gift_type", ConfDailyGiftType.class),

    /**
     * 月卡
     */
    MONTH_CARD("month_card_type", ConfMonthCardType.class),


    /**
     * ====================================== 仙职 ==========================================
     */
    IMMORTAL_TYPE("immortal_type", ConfImmortalType.class),

    IMMORTAL_PRIVILEGE("immortal_privilege", ConfImmortalPrivilege.class),;




    /**
     * json 表名，不含扩展名
     */
    private final String tableName;

    /**
     * 实体类
     */
    private final Class entityClass;

    ConfigDataEnum(String tableName, Class entityClass) {
        this.tableName = tableName;
        this.entityClass = entityClass;
    }

    public static ConfigDataEnum valueOfTableName(String tableName) {
        for (ConfigDataEnum cde : ConfigDataEnum.values()) {
            if (cde.tableName.equals(tableName)) {
                return cde;
            }
        }
        return null;
    }
}

package cn.youai.xiuzhen.config;

import cn.youai.server.conf.ConfItem;
import cn.youai.server.model.ConfigTable;
import cn.youai.basics.utils.ReflectUtils;
import cn.youai.xiuzhen.entity.pojo.ConfMainStory;
import cn.youai.xiuzhen.entity.pojo.ConfMedicine;
import cn.youai.xiuzhen.entity.pojo.ConfRefineEquip;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 配置表常量
 *
 * @author luopeihuan
 */
@Slf4j
public class GameConfig {
    private GameConfig() {
    }

    private static final Map<String, ConfigTable> ALL = new ConcurrentHashMap<>();

    /**
     * 道具
     */
    public static final ConfigTable ITEM = ConfigTable.valueOf("item", ConfItem.class);
    /**
     * 主剧情
     */
    public static final ConfigTable MAIN_STORY = ConfigTable.valueOf("main_story", ConfMainStory.class);

    /**
     * 丹药
     */
    public static final ConfigTable REFINE_MEDICINE = ConfigTable.valueOf("refine_medicine", ConfMedicine.class);

    /**
     * 炼器道具
     */
    public static final ConfigTable REFINE_EQUIP = ConfigTable.valueOf("refine_equip", ConfRefineEquip.class);

    public static void init() {
        try {
            List<Field> allFields = new LinkedList<>();
            ReflectUtils.getAllFields(allFields, GameConfig.class);
            for (Field field : allFields) {
                if (Modifier.isStatic(field.getModifiers()) && field.getType() == ConfigTable.class) {
                    ConfigTable configTable = (ConfigTable) field.get(null);
                    if (ALL.containsKey(configTable.getTableName())) {
                        log.error("{} is duplicated!", configTable.getTableName());
                    } else {
                        ALL.put(configTable.getTableName(), configTable);
                    }
                }
            }

            ConfigTable.tables().addAll(ALL.values());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ConfigTable getConfigTable(String tableName) {
        for (ConfigTable e : ALL.values()) {
            if (e.getTableName().equals(tableName)) {
                return e;
            }
        }
        return null;
    }
}

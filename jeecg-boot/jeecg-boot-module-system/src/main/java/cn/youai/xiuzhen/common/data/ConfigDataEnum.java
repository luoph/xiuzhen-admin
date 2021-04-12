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

	/**
	 * 道具
	 */
	ITEM("item", ConfItem.class),
	/**
	 * 充值
	 */
	RECHARGE_GOODS("recharge_goods", ConfRechargeGoods.class),
	//end
	/**
	 * 主剧情
	 */
	MAIN_STORY("main_story", ConfMainStory.class),
	;

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

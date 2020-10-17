package cn.youai.xiuzhen.entity.pojo;

import lombok.Getter;

/**
 * @ClassName ItemReduce
 * @Author buliangliang
 * @Description 各系统奖励物品掉落消息提示
 * @Date 2019/10/29 14:53
 * @Version 1.0
 */
@Getter
public enum ItemReduce {
    /**
     * 系统定义
     */
    DEFAULT(0, "缺省默认系统"),
    ANIMA_FIELD(1, "洞府-灵田-生产"),
    ANIMA_FIELD_UPGRADE(2, "洞府-灵田-升级"),
    SPIRIT_ELEMENT_UPGRADE(3, "洞府-灵根-灵根升级"),
    SPIRIT_ROOT_UPGRADE(4, "洞府-灵根-聚灵珠升级"),
    GONG_METHOD_LEARN(5, "洞府-功法-学习功法"),
    GONG_METHOD_GRADE(6, "洞府-功法-功法升级"),
    GONG_METHOD_INFER(7, "洞府-功法-推演"),
    ALCHEMY_LEARN(8, "洞府-炼丹-学习丹方"),
    ALCHEMY(9, "洞府-炼丹-炼丹"),
    REFINER_LEARN(10, "洞府-炼器-学习图纸"),
    REFINER(11, "洞府-炼器-炼器"),
    COMPOSE(12, "洞府-合成"),
    WEAPON_ACTIVATE(13, "法宝-激活法宝"),
    WEAPON_LEVEL_UP(14, "法宝-精炼"),
    WEAPON_MEDICINE(15, "法宝-精粹"),
    WEAPON_SUIT_UPGRADE(16, "法宝-套装锻造"),
    WEAPON_STAR(17, "法宝-拓星"),
    WEAPON_JEWEL_COMPOSE(18, "法宝-符文-快捷合成"),
    WEAPON_JEWEL_UPGRADE(19, "法宝-符文精炼"),
    WEAPON_JEWEL_STAR(20, "法宝-符文拓星"),
    WEAPON_SPIRIT(21, "法宝-铸灵"),
    WEAPON_ADD(22, "法宝-追加"),
    PET_ACTIVATE(23, "仙兽-激活"),
    PET_UPGRADE(24, "仙兽-喂养"),
    PET_STAR(25, "仙兽-觉醒"),
    PET_WEAPON_ACTIVATE(26, "仙兽-装备激活"),
    PET_WEAPON_UPGRADE(27, "仙兽-装备升级"),
    PET_DRAW(28, "仙兽-仙兽宫"),
    FACTION_DONATION(29, "仙盟-大殿-贡献"),
    FACTION_MEAL(30, "仙盟-仙宴"),
    FACTION_DEPOSITORY(31, "仙盟-仓库"),
    BAG_ITEM_USE(32, "储物袋-道具使用"),
    ITEM_SALE(33, "储物袋-道具出售"),
    BAG_UPGRADE(34, "储物袋-储物袋升级"),
    REFINE_BODY_GATHER_ANIMA(35, "储物袋-炼体-聚灵"),
    REFINE_BODY_DESTINY(36, "储物袋-炼体-天命冲穴"),
    ORNAMENTS(37, "储物袋-灵宝升阶"),
    PERSON_SKILL(38, "储物袋-仙命-仙术推演"),
    ADD_POINT(39, "储物袋-仙命-命盘加点"),
    FASHION(40, "储物袋-时装-激活"),
    FASHION_UPGRADE(41, "储物袋-时装-升级"),
    LITTLE_PET_ACTIVATE(42, "储物袋-圣灵-激活"),
    LITTLE_PET_UPGRADE(43, "储物袋-圣灵-升阶"),
    FATE_EQUIP_UPGRADE(44, "储物袋-飞剑-打磨"),
    FATE_EQUIP_STAR(45, "储物袋-飞剑-重铸"),
    FATE_EQUIP_MEDICINE(46, "储物袋-飞剑-精粹"),
    PLOT_HANG_UP_QUICK(47, "剧情-快速挂机"),
    MONEY_TREE(48, "福利-灵石商人"),
    FESTIVAL_EXCHANGE(49, "节日活动-兑换"),
    DAY_GIFT(50, "仙福-特惠礼包"),
    OPEN_SERVICE_GIFT(51, "开服排行-优惠礼包"),
    FACTION_TITTLE(52, "仙榜-称号激活称号"),
    CROSS_ROBBERY_MEDICINE(53, "渡劫-吃渡劫丹"),
    ARENA_TIMES(54, "斗法-福地夺宝-购买次数"),
    ARENA_REFRESH(55, "斗法-福地夺宝-刷新对手"),
    ARENA_CD_REFRESH(56, "斗法-福地夺宝-清除冷却"),
    ARENA_EXCHANGE(57, "斗法-福地夺宝-兑换"),
    TRAVEL_HILL_QUALITY(58, "斗法-上古遗迹-提升品质"),
    GHOST_WAR_EXCITATION(59, "斗法-神魔战场-激励"),
    GHOST_WAR_REVIVE(60, "斗法-神魔战场-复活"),
    BOSS_LEVEL_TIMES(61, "封魔-法宝器灵-购买次数"),
    BOSS_STORY_TIMES(62, "封魔-灵宝幻魔-购买次数"),
    BOSS_WORLD_CD(63, "封魔-魔王入侵-清除冷却"),
    PET_FAIRYLAND(64, "秘境-仙兽秘境-冒险"),
    PET_FAIRYLAND_QUICK(65, "秘境-仙兽秘境-快速通关"),
    PET_FAIRYLAND_TRAVEL(66, "秘境-仙兽秘境-神游"),
    PET_FAIRYLAND_TRAVEL_QUICK(67, "秘境-仙兽秘境-快速神游"),
    MEDICINAL_FAIRYLAND_SHOP(68, "秘境-丹药秘境-商店"),
    MEDICINAL_FAIRYLAND_REVIVE(69, "秘境-丹药秘境-复活"),
    SEVEN_TASKS_GIFT(70, "七日目标-每日福利-购买"),
    STONE_RETRIEVE(71, "日常-灵石找回"),
    JADE_RETRIEVE(72, "日常-仙玉找回"),
    SHOP(73, "坊市-购买"),
    CELESTIAL_REALM(74, "飞升仙界-仙脉消耗"),
    CELESTIAL_REALM_BOSS(75, "仙尊-购买次数（飞升仙界boss）"),
    EQUIPMENT_ALCHEMY(76, "法宝炼灵"),
    SPIRITUAL_WORLD_BOSS(77, "炼灵玄妖（灵界器灵）"),
    FACTION_BOSS_CHALLENGE(78, "仙盟——仙盟妖兽-挑战（仙盟boss）"),
    HOLY_GHOST_UPGRADE(79, "储物袋-圣灵-升级"),
    HOLY_GHOST_APPEARANCE_ACTIVATE(80, "储物袋-圣灵-激活化形"),
    HOLY_GHOST_PITHY(82, "储物袋-圣灵-精粹"),
    EQUIPMENT_SUIT(83, "套装"),
    TIER_MAP_EXPLORE(84, "套装秘境"),
    IDENTIFY_LOOK(85, "鉴宝"),
    LEVEL_GIFT(86, "境界每日礼包"),

    // end
    ;

    private final int type; //  途径
    private final String name; // 模块名

    ItemReduce(int type, String name) {
        this.type = type;
        this.name = name;
    }

    public static ItemReduce valueOf(int module) {
        for (ItemReduce itemReduce : ItemReduce.values()) {
            if (module == itemReduce.getType()) {
                return itemReduce;
            }
        }
        return DEFAULT;
    }
}

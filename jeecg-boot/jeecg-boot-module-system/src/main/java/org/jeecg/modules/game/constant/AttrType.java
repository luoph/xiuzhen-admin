package org.jeecg.modules.game.constant;


import lombok.Getter;

/**
 * 属性模块
 *
 * @author luopeihuan
 */
@Getter
public enum AttrType {
    /**
     * 属性模块9
     */
    PRACTICE(1001, "境界"),
    SPIRIT_ROOT(1002, "灵根"),
    SPIRIT_ELEMENT(1003, "聚灵珠(五行)"),
    //    GONG_METHOD(1004, "功法"),
    GONG_METHOD_SUIT(1005, "门派功法套装"),
    IMMORTALS_ABODE(1006, "洞府"),
    MEDICINAL_PILLS(1007, "丹药"),
    FATE_EQUIPMENT(1008, "本命法宝"),
    EXPERIENCE(1009, "日常阅历"),
    PET(1010, "宠物"),
    LOTTERY(1011, "灵兽抽卡"),
    FASHION(1012, "时装"),
    CELESTIAL_REALM(1013, "仙界"),
    TRAVEL(1014, "游历"),
    LITTLE_PET(1015, "小宠", true),
    NATAL_CHART(1016, "命盘"),
    REFINE_BODY(1017, "炼体"),
    PET_RESONANCE(1018, "灵兽羁绊"),
    NEW_WEAPON(1019, "第五版新法宝"),
    NEW_WEAPON_JEWEL(1021, "第五版法宝宝石"),
    NEW_WEAPON_MEDICINE(1022, "法宝精粹"),
    NEW_WEAPON_SUIT(1023, "新法宝套装"),
    WEAPON_ALCHEMY(1024, "法宝炼灵"),
    WEAPON_SOUL(1025, "仙兽、飞剑之魂"),
    HOLY_GHOST(1026, "圣灵", true),
    HOLY_GHOST_APPEARANCE(1027, "圣灵-化形", true),
    HOLY_GHOST_SKILL(1028, "圣灵-技能", true),
    HOLY_GHOST_PITHY(1029, "圣灵-精粹", true),
    EQUIPMENT_SUIT(1030, "仙器"),
    GONG(1031, "功法"),
    GONG_COMBINATION(1032, "功法组合"),
    BATTLE_SKILL(1033, "仙术"),
    MATE(1034, "仙缘"),
    MATE_KEEPSAKE_SSR(1035, "仙缘信物极品属性（全局）", true),
    EQUIPMENT_SUIT_UPGRADE(1036, "仙器器灵"),
    FACTION_SKILL(1037, "仙盟技能"),
    WEAPON_EQUIP_SUIT(1038, "法宝新套装"),
    PRACTICE_ADDITION(1039, "境界全局加成", true),
    HEAVEN_BOOK(1040, "天书"),
    HOLY_REALM(1041, "圣界"),
    BIG_DIPPER(1042, "北斗七星"),
    MARRY_RING(1043, "结婚-誓戒"),
    MARRY_KEEPSAKE(1044, "结婚-信物"),
    SIX_SWORD(1045, "六道剑阵", true),
    PRECEPT_POWER(1046, "仙力护符"),

    // 炼丹炼器
    REFINE_DAN(1047, "新版丹药"),
    DAN_MASTER(1048, "炼丹大师"),
    EQUIP_MASTER(1059, "炼器大师"),

    FATE_EQUIP_COMBINE(1050, "飞剑羁绊"),

    ;

    private final String name;
    private final int code;
    private final boolean isGlobal;

    AttrType(int code, String name) {
        this.code = code;
        this.name = name;
        this.isGlobal = false;
    }

    AttrType(int code, String name, boolean isGlobal) {
        this.code = code;
        this.name = name;
        this.isGlobal = isGlobal;
    }


    public static AttrType valueOf(int code) {
        for (AttrType e : AttrType.values()) {
            if (e.getCode() == code) {
                return e;
            }
        }
        return null;
    }

    public static String getNameByCode(int code) {
        for (AttrType value : AttrType.values()) {
            if (value.getCode() == code) {
                return value.getName();
            }
        }
        return "未知";
    }
}

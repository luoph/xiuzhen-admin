package cn.youai.xiuzhen.entity.pojo;

import cn.hutool.core.collection.ConcurrentHashSet;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * 属性枚举
 * 字段命名规则：
 * 1. ATT - attack 攻击
 * 2. DEF - defence 防御
 * 3. CRIT - Critical 暴击
 * 4. PCT - percent 百分比
 *
 * @author buliangliang
 */
@Getter
@SuppressWarnings("rawtypes")
public enum RoleAttrType {

    /**
     * 角色属性类型
     */
    Q_MIN(100, "q_min", "最小修为", "qualityMin", Long.class),
    Q_MAX(101, "q_max", "最大修为", "qualityMax", Long.class),
    L_MAX(102, "l_max", "灵气效率", "animalMax", Long.class),

    HP(2000, "hp", "生命", "hp", Long.class),
    DEF(2001, "def", "防御", "def", Long.class),
    DODGE(2002, "dodge", "闪避", "dodge", Long.class),
    HIT(2003, "hit", "命中", "hit", Long.class),
    SPEED(2004, "speed", "速度", "speed", Long.class),
    CRIT(2005, "crit", "暴击", "crit", Long.class),
    CRIT_DEF(2006, "crit_def", "暴抗", "critDef", Long.class),

    CRIT_PCT(2007, "crit_pct", "暴击%", "critPct", Double.class, true),
    HP_PCT(2008, "hp_pct", "生命%", "hpPct", Double.class, HP),
    HIT_PCT(2009, "hit_pct", "命中%", "hitPct", Double.class, true),
    DODGE_PCT(2010, "dodge_pct", "闪避%", "dodgePct", Double.class, true),
    CRIT_DEF_PCT(2011, "crit_def_pct", "暴抗%", "critDefPct", Double.class, true),

    CRIT_DMG_PCT(2012, "crit_dmg_pct", "爆伤%", "critDmgPct", Double.class, true),
    HUMAN_HARM(2013, "human_harm", "人族伤害", "humanHarm", Long.class),
    DEVIL_HARM(2014, "devil_harm", "魔族伤害", "devilHarm", Long.class),
    DEMON_HARM(2015, "demon_harm", "妖族伤害", "demonHarm", Long.class),
    SPIRIT_HARM(2016, "spirit_harm", "灵族伤害", "spiritHarm", Long.class),
    DRAGON_HARM(2017, "dragon_harm", "龙族伤害", "dragonHarm", Long.class),
    DEITY_HARM(2018, "deity_harm", "神族伤害", "deityHarm", Long.class),
    FAIRY_HARM(2019, "fairy_harm", "仙族伤害", "fairyHarm", Long.class),
    HUMAN_ATT(2020, "human_att", "人族攻击", "humanAtt", Long.class),
    DEVIL_ATT(2021, "devil_att", "魔族攻击", "devilAtt", Long.class),
    DEMON_ATT(2022, "demon_att", "妖族攻击", "demonAtt", Long.class),
    SPIRIT_ATT(2023, "spirit_att", "灵族攻击", "spiritAtt", Long.class),
    DRAGON_ATT(2024, "dragon_att", "龙族攻击", "dragonAtt", Long.class),
    DEITY_ATT(2025, "deity_att", "神族攻击", "deityAtt", Long.class),
    FAIRY_ATT(2026, "fairy_att", "仙族攻击", "fairyAtt", Long.class),
    HUMAN_DEF(2027, "human_def", "人族防御", "humanDef", Long.class),
    DEVIL_DEF(2028, "devil_def", "魔族防御", "devilDef", Long.class),
    DEMON_DEF(2029, "demon_def", "妖族防御", "demonDef", Long.class),
    SPIRIT_DEF(2030, "spirit_def", "灵族防御", "spiritDef", Long.class),
    DRAGON_DEF(2031, "dragon_def", "龙族防御", "dragonDef", Long.class),
    DEITY_DEF(2032, "deity_def", "神族防御", "deityDef", Long.class),
    FAIRY_DEF(2033, "fairy_def", "仙族防御", "fairyDef", Long.class),
    THUNDER_RESIST(2034, "thunder_resist", "雷抗", "thunderResist", Long.class),
    ATT(2035, "att", "攻击", "att", Long.class),

    // 最大攻击 & 最小攻击
    ATT_RANGE_MIN(3000, "att_range_min", "人物攻击下限", "attRangeMin", Long.class),
    ATT_RANGE_MAX(3001, "att_range_max", "人物攻击上限", "attRangeMax", Long.class),
    ATT_PCT(2036, "att_pct", "攻击%", "attPct", Double.class, ATT, ATT_RANGE_MIN, ATT_RANGE_MAX),

    DEF_PCT(2037, "def_pct", "防御%", "defPct", Double.class, DEF),
    SPEED_PCT(2038, "speed_pct", "速度%", "speedPct", Double.class, SPEED),

    COMBAT_POWER(2039, "combat_power", "仙力", "combatPower", Long.class),

    GAS_ZW(2040, "gas_zw", "真武之气", "gasZw", Long.class),
    GAS_XD(2041, "gas_xd", "玄盾之气", "gasXd", Long.class),
    GAS_SY(2042, "gas_sy", "生蕴之气", "gasSy", Long.class),
    GAS_XY(2043, "gas_xy", "仙运之气", "gasXy", Long.class),

    // 破军率	%	攻击*破军率%*50
    @Deprecated
    BREAK_ATT_PCT(2050, "break_att_pct", "破军率%", "breakAttPct", Double.class, true),
    // 破军抵抗率	%	攻击*破军抵抗率%*50
    @Deprecated
    BREAK_DEF_PCT(2051, "break_def_pct", "破军抵抗率%", "breakDefPct", Double.class, true),
    // 破军伤害率	%	攻击*破军伤害率%*50
    @Deprecated
    BREAK_DMG_PCT(2052, "break_dmg_pct", "破军伤害率%", "breakDmgPct", Double.class, true),
    // 破军免伤率	%	攻击*破军免伤率%*50
    @Deprecated
    BREAK_DR_PCT(2053, "break_dr_pct", "破军免伤率%", "breakDrPct", Double.class, true),

    // 卓越一击率	%	攻击*卓越一击率%*50
    EXCEL_ATT_PCT(2054, "excel_att_pct", "卓越率%", "excelAttPct", Double.class, true),
    // 卓越一击抵抗率	%	攻击*卓越一击抵抗率%*50
    EXCEL_DEF_PCT(2055, "excel_def_pct", "卓越抵抗%", "excelDefPct", Double.class, true),
    // 卓越一击伤害率	%	攻击*卓越一击伤害率%*50
    EXCEL_DMG_PCT(2056, "excel_dmg_pct", "卓越伤害%", "excelDmgPct", Double.class, true),
    // 卓越一击免伤率	%	攻击*卓越一击免伤率%*50
    EXCEL_DR_PCT(2057, "excel_dr_pct", "卓越免伤%", "excelDrPct", Double.class, true),

    // 会心一击率	%	攻击*会心一击率%*50
    INSIGHT_ATT_PCT(2058, "insight_att_pct", "会心率%", "insightAttPct", Double.class, true),
    // 会心一击抵抗率	%	攻击*会心一击抵抗率%*50
    INSIGHT_DEF_PCT(2059, "insight_def_pct", "会心抵抗%", "insightDefPct", Double.class, true),
    // 会心一击伤害率	%	攻击*会心一击伤害率%*50
    INSIGHT_DMG_PCT(2060, "insight_dmg_pct", "会心伤害%", "insightDmgPct", Double.class, true),
    // 会心一击免伤率	%	攻击*会心一击免伤率%*50
    INSIGHT_DR_PCT(2061, "insight_dr_pct", "会心免伤%", "insightDrPct", Double.class, true),

    // 修为加成
    PRACTICE_PCT(2062, "practice_pct", "修为加成%", "practicePct", Double.class, true),

    // 增伤
    ED_PCT(2063, "ed_pct", "增伤%", "edPct", Double.class, true),
    // 减伤
    RD_PCT(2064, "rd_pct", "减伤%", "rdPct", Double.class, true),

    // 命中提升%（影响命中值加成）
    HIT_INC_PCT(2065, "hit_inc_pct", "命中提升%", "hitIncPct", Double.class, HIT),
    // 闪避提升%（影响闪避值加成）
    DODGE_INC_PCT(2066, "dodge_inc_pct", "闪避提升%", "dodgeIncPct", Double.class, DODGE),

    // 破甲
    ARP(2070, "arp", "破甲", "arp", Long.class),
    // 破甲提升%（影响破甲值加成）
    ARP_INC_PCT(2071, "arp_inc_pct", "破甲提升%", "ArpIncPct", Double.class, ARP),
    // END
    ;

    private final int code;

    private final String column;

    private final String name;

    private final String property;

    // 影响的基础属性
    private final RoleAttrType[] effectAttr;

    private final Class clazz;

    private final boolean isPercentage;

    // 百分比属性影响的基础属性类型
    @Getter
    public final static Set<RoleAttrType> EFFECT_ATTR_TYPE_SET = new ConcurrentHashSet<>();

    static {
        for (RoleAttrType value : RoleAttrType.values()) {
            if (value.getEffectAttr() != null) {
                EFFECT_ATTR_TYPE_SET.addAll(Arrays.asList(value.getEffectAttr()));
            }
        }
    }

    RoleAttrType(int code, String column, String name, String property, Class clazz) {
        this.code = code;
        this.column = column;
        this.name = name;
        this.property = property;
        this.clazz = clazz;
        this.effectAttr = null;
        this.isPercentage = false;
    }

    RoleAttrType(int code, String column, String name, String property, Class clazz, boolean isPercentage) {
        this.code = code;
        this.column = column;
        this.name = name;
        this.property = property;
        this.clazz = clazz;
        this.isPercentage = isPercentage;
        this.effectAttr = null;
    }

    RoleAttrType(int code, String column, String name, String property, Class clazz, RoleAttrType... effectAttrs) {
        this.code = code;
        this.column = column;
        this.name = name;
        this.property = property;
        this.effectAttr = effectAttrs;
        this.isPercentage = true;
        this.clazz = clazz;
    }

    /**
     * 获取属性对应的列名 
     */
    public static String getColumn(int code) {
        for (RoleAttrType e : RoleAttrType.values()) {
            if (e.getCode() == code) {
                return e.getColumn();
            }
        }
        return null;
    }

    /**
     * 获取属性名
     */
    public static String getName(int code) {
        for (RoleAttrType e : RoleAttrType.values()) {
            if (e.getCode() == code) {
                return e.getName();
            }
        }
        return null;
    }

    /**
     * 获取属性
     */
    public static String getProperty(int code) {
        for (RoleAttrType e : RoleAttrType.values()) {
            if (e.getCode() == code) {
                return e.getProperty();
            }
        }
        return null;
    }

    public static int getCode(String string) {
        for (RoleAttrType e : RoleAttrType.values()) {
            if (e.getColumn().equals(string)) {
                return e.getCode();
            } else if (e.getProperty().equals(string)) {
                return e.getCode();
            }
        }
        return 0;
    }

    public static String getPropertyByColumn(String column) {
        for (RoleAttrType e : RoleAttrType.values()) {
            if (e.getColumn().equals(column)) {
                return e.getProperty();
            }
        }
        return null;
    }

    public static RoleAttrType valueOf(int code) {
        for (RoleAttrType e : RoleAttrType.values()) {
            if (e.getCode() == code) {
                return e;
            }
        }
        return null;
    }

    // -------------------------------- BaseAttrType --------------------------------
    public static boolean isBaseAttrType(int code) {
        return code < HP.code;
    }

    public static boolean isBaseAttrType(List<Integer> attIds) {
        return null != attIds && attIds.stream().anyMatch(RoleAttrType::isBaseAttrType);
    }

    public static RoleAttrType getBaseAttrType(int code) {
        for (RoleAttrType baseAttr : RoleAttrType.values()) {
            if (baseAttr.code == code) {
                return baseAttr;
            }
            if (!isBaseAttrType(code)) {
                break;
            }
        }
        return null;
    }

}

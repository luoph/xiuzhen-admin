package cn.youai.xiuzhen.entity.pojo;

import cn.youai.xiuzhen.utils.NumberUtils;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import org.apache.commons.lang3.RandomUtils;

/**
 * 玩家属性
 *
 * @author luopeihuan
 * @version 1.0
 * @date 2019-10-16.
 */
public class RoleAttr extends MapValue {

    /**
     * 序列化字段
     */
    private static final long serialVersionUID = -7029677935476385568L;

    public RoleAttr() {
        reset();
    }

    public RoleAttr(JSONObject object) {
        super(object);
    }

    public RoleAttr(MapValue mapValue) {
        reset();
        copy(mapValue);
    }

    public static RoleAttr valueOf(MapValue mapValue) {
        return new RoleAttr(mapValue);
    }

    @Override
    public void reset() {
        super.reset();
    }

    @JSONField(serialize = false)
    public long getCombatPower() {
        return longValue(RoleAttrType.COMBAT_POWER);
    }

    @JSONField(deserialize = false)
    public void setCombatPower(long combatPower) {
        setProperty(RoleAttrType.COMBAT_POWER, combatPower);
    }

    /**
     * 是否有百分比加成属性
     *
     * @return 是否有百分比加成属性
     */
    public boolean hasAdditionProperty() {
        for (RoleAttrType value : RoleAttrType.values()) {
            if (value.getEffectAttr() != null) {
                double rateValue = doubleValue(value);
                if (NumberUtils.isNotZero(rateValue)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 清空基础属性, 仅保留百分比加成属性
     * HP_PCT(2008, "hp_pct", "生命%", "hpPct", Double.class, HP),
     * ATT_PCT(2036, "att_pct", "攻击%", "attPct", Double.class, ATT, ATT_RANGE_MIN, ATT_RANGE_MAX),
     * DEF_PCT(2037, "def_pct", "防御%", "defPct", Double.class, DEF),
     * SPEED_PCT(2038, "speed_pct", "速度%", "speedPct", Double.class, SPEED),
     * HIT_INC_PCT(2065, "hit_inc_pct", "命中提升%", "hitIncPct", Double.class, HIT),
     * DODGE_INC_PCT(2066, "dodge_inc_pct", "闪避提升%", "dodgeIncPct", Double.class, DODGE),
     */
    public RoleAttr clearBaseProperty() {
        for (RoleAttrType value : RoleAttrType.values()) {
            if (null == value.getEffectAttr()) {
                clearProperty(value);
            }
        }
        return this;
    }

    /**
     * 清理加成系数（HP_PCT、ATT_PCT、DEF_PCT、SPEED_PCT、HIT_INC_PCT、DODGE_INC_PCT）
     */
    public RoleAttr clearAdditionProperty() {
        for (RoleAttrType value : RoleAttrType.values()) {
            if (value.getEffectAttr() != null) {
                clearProperty(value);
            }
        }
        return this;
    }

    /**
     * 清理不受百分比属性影响的属性
     * 仅保留基础属性（HP、ATT、ATT_RANGE_MAX、ATT_RANGE_MIN、DEF、SPEED、HIT、DODGE）
     */
    public RoleAttr clearNoneEffectProperty() {
        for (RoleAttrType value : RoleAttrType.values()) {
            if (!RoleAttrType.EFFECT_ATTR_TYPE_SET.contains(value)) {
                clearProperty(value);
            }
        }
        return this;
    }

    @JSONField(serialize = false)
    public long getHp() {
        return longValue(RoleAttrType.HP);
    }

    @JSONField(serialize = false)
    public long getDef() {
        return longValue(RoleAttrType.DEF);
    }

    @JSONField(serialize = false)
    public long getDodge() {
        return longValue(RoleAttrType.DODGE);
    }

    @JSONField(serialize = false)
    public long getHit() {
        return longValue(RoleAttrType.HIT);
    }

    @JSONField(serialize = false)
    public long getSpeed() {
        return longValue(RoleAttrType.SPEED);
    }

    @JSONField(serialize = false)
    public long getCrit() {
        return longValue(RoleAttrType.CRIT);
    }

    @JSONField(serialize = false)
    public long getCritDef() {
        return longValue(RoleAttrType.CRIT_DEF);
    }

    @JSONField(serialize = false)
    public double getCritPct() {
        return doubleValue(RoleAttrType.CRIT_PCT);
    }

    @JSONField(serialize = false)
    public double getHpPct() {
        return doubleValue(RoleAttrType.HP_PCT);
    }

    @JSONField(serialize = false)
    public double getHitPct() {
        return doubleValue(RoleAttrType.HIT_PCT);
    }

    @JSONField(serialize = false)
    public double getDodgePct() {
        return doubleValue(RoleAttrType.DODGE_PCT);
    }

    @JSONField(serialize = false)
    public double getCritDefPct() {
        return doubleValue(RoleAttrType.CRIT_DEF_PCT);
    }


    @JSONField(serialize = false)
    public double getCritDmgPct() {
        return doubleValue(RoleAttrType.CRIT_DMG_PCT);
    }

    @JSONField(serialize = false)
    public long getHumanHarm() {
        return longValue(RoleAttrType.HUMAN_HARM);
    }

    @JSONField(serialize = false)
    public long getDevilHarm() {
        return longValue(RoleAttrType.DEVIL_HARM);
    }

    @JSONField(serialize = false)
    public long getDemonHarm() {
        return longValue(RoleAttrType.DEMON_HARM);
    }

    @JSONField(serialize = false)
    public long getSpiritHarm() {
        return longValue(RoleAttrType.SPIRIT_HARM);
    }

    @JSONField(serialize = false)
    public long getDragonHarm() {
        return longValue(RoleAttrType.DRAGON_HARM);
    }

    @JSONField(serialize = false)
    public long getDeityHarm() {
        return longValue(RoleAttrType.DEITY_HARM);
    }

    @JSONField(serialize = false)
    public long getFairyHarm() {
        return longValue(RoleAttrType.FAIRY_HARM);
    }

    @JSONField(serialize = false)
    public long getHumanAtt() {
        return longValue(RoleAttrType.HUMAN_ATT);
    }

    @JSONField(serialize = false)
    public long getDevilAtt() {
        return longValue(RoleAttrType.DEVIL_ATT);
    }

    @JSONField(serialize = false)
    public long getDemonAtt() {
        return longValue(RoleAttrType.DEMON_ATT);
    }

    @JSONField(serialize = false)
    public long getSpiritAtt() {
        return longValue(RoleAttrType.SPIRIT_ATT);
    }

    @JSONField(serialize = false)
    public long getDragonAtt() {
        return longValue(RoleAttrType.DRAGON_ATT);
    }

    @JSONField(serialize = false)
    public long getDeityAtt() {
        return longValue(RoleAttrType.DEITY_ATT);
    }

    @JSONField(serialize = false)
    public long getFairyAtt() {
        return longValue(RoleAttrType.FAIRY_ATT);
    }

    @JSONField(serialize = false)
    public long getHumanDef() {
        return longValue(RoleAttrType.HUMAN_DEF);
    }

    @JSONField(serialize = false)
    public long getDevilDef() {
        return longValue(RoleAttrType.DEVIL_DEF);
    }

    @JSONField(serialize = false)
    public long getDemonDef() {
        return longValue(RoleAttrType.DEMON_DEF);
    }

    @JSONField(serialize = false)
    public long getSpiritDef() {
        return longValue(RoleAttrType.SPIRIT_DEF);
    }

    @JSONField(serialize = false)
    public long getDragonDef() {
        return longValue(RoleAttrType.DRAGON_DEF);
    }

    @JSONField(serialize = false)
    public long getDeityDef() {
        return longValue(RoleAttrType.DEITY_DEF);
    }

    @JSONField(serialize = false)
    public long getFairyDef() {
        return longValue(RoleAttrType.FAIRY_DEF);
    }

    @JSONField(serialize = false)
    public long getThunderResist() {
        return longValue(RoleAttrType.THUNDER_RESIST);
    }

    @JSONField(serialize = false)
    public long getAtt() {
        return longValue(RoleAttrType.ATT);
    }

    /**
     * 随机攻击值
     */
    @JSONField(serialize = false)
    public long getRandomAtt() {
        // 兼容机器人 & Monster 属性
        return RandomUtils.nextLong(getAttRangeMin(), getAttRangeMax() + 1);
    }

    /**
     * 平均攻击值
     */
    @JSONField(serialize = false)
    public long getAverageAtt() {
        // 兼容机器人 & Monster 属性
        return (getAttRangeMin() + getAttRangeMax()) / 2;
    }

    @JSONField(serialize = false)
    public double getAttPct() {
        return doubleValue(RoleAttrType.ATT_PCT);
    }

    @JSONField(serialize = false)
    public double getDefPct() {
        return doubleValue(RoleAttrType.DEF_PCT);
    }

    @JSONField(serialize = false)
    public double getSpeedPct() {
        return doubleValue(RoleAttrType.SPEED_PCT);
    }

    @JSONField(serialize = false)
    public long getGasZw() {
        return longValue(RoleAttrType.GAS_ZW);
    }

    @JSONField(serialize = false)
    public long getGasXd() {
        return longValue(RoleAttrType.GAS_XD);
    }

    @JSONField(serialize = false)
    public long getGasSy() {
        return longValue(RoleAttrType.GAS_SY);
    }

    @JSONField(serialize = false)
    public long getGasXy() {
        return longValue(RoleAttrType.GAS_XY);
    }

    // 卓越一击率    %   攻击*卓越一击率%*50
    @JSONField(serialize = false)
    public double getExcelAttPct() {
        return doubleValue(RoleAttrType.EXCEL_ATT_PCT);
    }

    // 卓越一击抵抗率  %   攻击*卓越一击抵抗率%*50
    @JSONField(serialize = false)
    public double getExcelDefPct() {
        return doubleValue(RoleAttrType.EXCEL_DEF_PCT);
    }

    // 卓越一击伤害率  %   攻击*卓越一击伤害率%*50
    @JSONField(serialize = false)
    public double getExcelDmgPct() {
        return doubleValue(RoleAttrType.EXCEL_DMG_PCT);
    }

    // 卓越一击免伤率  %   攻击*卓越一击免伤率%*50
    @JSONField(serialize = false)
    public double getExcelDrPct() {
        return doubleValue(RoleAttrType.EXCEL_DR_PCT);
    }


    // 会心一击率    %   攻击*会心一击率%*50
    @JSONField(serialize = false)
    public double getInsightAttPct() {
        return doubleValue(RoleAttrType.INSIGHT_ATT_PCT);
    }

    // 会心一击抵抗率  %   攻击*会心一击抵抗率%*50
    @JSONField(serialize = false)
    public double getInsightDefPct() {
        return doubleValue(RoleAttrType.INSIGHT_DEF_PCT);
    }

    // 会心一击伤害率  %   攻击*会心一击伤害率%*50
    @JSONField(serialize = false)
    public double getInsightDmgPct() {
        return doubleValue(RoleAttrType.INSIGHT_DMG_PCT);
    }

    // 会心一击免伤率  %   攻击*会心一击免伤率%*50
    @JSONField(serialize = false)
    public double getInsightDrPct() {
        return doubleValue(RoleAttrType.INSIGHT_DR_PCT);
    }


    // 修为加成
    @JSONField(serialize = false)
    public double getPracticePct() {
        return doubleValue(RoleAttrType.PRACTICE_PCT);
    }

    // 增伤
    @JSONField(serialize = false)
    public double getEdPct() {
        return doubleValue(RoleAttrType.ED_PCT);
    }

    // 减伤
    @JSONField(serialize = false)
    public double getRdPct() {
        return doubleValue(RoleAttrType.RD_PCT);
    }

    // 命中提升%（影响命中值加成）
    @JSONField(serialize = false)
    public double getHitIncPct() {
        return doubleValue(RoleAttrType.HIT_INC_PCT);
    }

    // 闪避提升%（影响闪避值加成）
    @JSONField(serialize = false)
    public double getDodgeIncPct() {
        return doubleValue(RoleAttrType.DODGE_INC_PCT);
    }

    // 破甲
    @JSONField(serialize = false)
    public long getArp() {
        return longValue(RoleAttrType.ARP);
    }
    
    // 破甲提升%
    @JSONField(serialize = false)
    public double getArpIncPct() {
        return doubleValue(RoleAttrType.ARP_INC_PCT);
    }

    @JSONField(serialize = false)    // 范围属性
    public long getAttRangeMin() {
        return longValue(RoleAttrType.ATT_RANGE_MIN);
    }

    @JSONField(serialize = false)
    public long getAttRangeMax() {
        return longValue(RoleAttrType.ATT_RANGE_MAX);
    }

}

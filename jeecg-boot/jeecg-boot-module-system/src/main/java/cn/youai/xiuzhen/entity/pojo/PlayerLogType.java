package cn.youai.xiuzhen.entity.pojo;


import lombok.Getter;

/**
 * 玩家统计类型
 *
 * @author luopeihuan
 */
@Getter
public enum PlayerLogType {
    /**
     * 玩家数值统计类型（最大值）
     */
    PLAYER_LEVEL(1, "玩家等级"),
    COMBAT_POWER(2, "战力"),
    REAM_LEVEL(3, "境界等级"),
    STORY_LEVEL(4, "剧情等级"),
    MAP_LEVEL(5, "冒险等级"),
    PRACTICE_YEAR(6, "修炼年数"),
    LINGSHAN_CHECKPOINT(7, "符文秘境-小灵山"),
    PRACTICE_VALUE(8, "玩家修为"),
    CHANGE_COMBAT_POWER(9, "战力前后变化值"),

    // 100 以上是次数统计
    ONLINE_TIME(101, "在线时长"),
    RECHARGE(102, "当天充值"),
    CONSUME_MONEY(103, "玉髓消耗"),
    EXPERIENCE(104, "阅历值"),

    GAIN_RARE_STONE(105, "极品灵石获得"),
    COST_RARE_STONE(106, "极品灵石消耗"),

    // 剧情关卡
    MAIN_STORY_LEVEL(110, "剧情小关卡"),
    FACTION_BANQUET(111, "仙盟仙宴"),
    MAIN_STORY_TRAVEL_QUICK(112, "剧情-快速挂机"),

    // 斗法
    ARENA_BATTLE(120, "福地夺宝/斗法-挑战"),
    TRAVEL_HILL(121, "上古遗迹（游历仙山）"),
    GHOST_WAR(122, "神魔战场"),
    TEAM_BOSS(123, "组队BOSS/须弥幻境"),
    MARRY_BOSS(124, "金兰副本"),
    QUALIFYING(125, "谪仙台"),

    // 秘境
    MAP_EXPLORE(130, "仙兽秘境/冒险探索"),
    AUTO_EXPLORE(131, "神游次数"),
    GOD_ROAD(132, "丹药秘境（成仙路）"),
    TIER_MAP_EXPLORE(133, "仙器秘境"),
    SPIRIT_STONE_MAP_EXPLORE(134, "灵石秘境"),
    PRACTICE_MAP_EXPLORE(135, "修为秘境"),
    MATE_PRACTICE(136, "仙缘双休"),
    WEAPON_MAP_EXPLORE(137, "法宝秘境"),

    // 封魔
    MAIN_STORY_BOSS(140, "北冥魔海"),
    PET_BOSS(141, "不死魔巢"),
    SPIRITUAL_WORLD_BOSS(142, "蛇陵魔窟（灵界器灵）"),
    WORLD_BOSS(143, "魔王入侵（世界boss）"),
    MATE_BOSS(144, "仙缘试炼"),

    // 炼丹炼器, param1 为丹方、器方id
    REFINE_DAN(150, "成功炼丹次数"),
    REFINE_EQUIP(151, "成功炼器次数"),

    // 秘境扫荡
    SPIRIT_STONE_MAP_SWEEP(160, "灵石秘境扫荡"),
    PRACTICE_MAP_SWEEP(161, "修为秘境扫荡"),
    TIER_MAP_SWEEP(162, "仙器秘境扫荡"),
    WEAPON_MAP_SWEEP(163, "法宝秘境扫荡"),

    // 礼包类型 value: 金额，param1: 礼包ID
    OPEN_SERVICE_RANK_PACK(1000, "开服排行礼包"),
    GIFT_PACK(1001, "特惠礼包"),

    // 特权boss
    IMMORTAL_HIGH(1010, "八荒天魔一键挑战"),
    IMMORTAL_LOW(1011, "九天魔君一键挑战"),
    MONTH_CARD_HIGH(1012, "至尊月卡一键挑战"),
    MONTH_CARD_LOW(1013, "特惠月卡一键挑战"),
    ;

    /**
     * 名称
     */
    private final String name;

    /**
     * 类型
     */
    private final int type;

    PlayerLogType(int type, String name) {
        this.type = type;
        this.name = name;
    }

    public static String getName(int type) {
        for (PlayerLogType c : PlayerLogType.values()) {
            if (c.getType() == type) {
                return c.getName();
            }
        }
        return null;
    }

    public static int getType(String name) {
        for (PlayerLogType c : PlayerLogType.values()) {
            if (c.getName() == name) {
                return c.getType();
            }
        }
        return 0;
    }

    public static boolean isCount(int type) {
        return type >= ONLINE_TIME.type;
    }
}


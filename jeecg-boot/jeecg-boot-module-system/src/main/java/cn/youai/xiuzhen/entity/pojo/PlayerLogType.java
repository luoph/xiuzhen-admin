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

    // 100 以上是次数统计
    ONLINE_TIME(101, "在线时长"),
    RECHARGE(102, "当天充值"),
    CONSUME_MONEY(103, "玉髓消耗"),
    EXPERIENCE(104, "阅历值"),

    // 剧情关卡
    MAIN_STORY_LEVEL(110, "剧情小关卡"),
    FACTION_BANQUET(111, "仙盟仙宴"),

    // 斗法
    ARENA_BATTLE(120, "福地夺宝/斗法-挑战"),
    TRAVEL_HILL(121, "上古遗迹（游历仙山）"),
    GHOST_WAR(122, "神魔战场"),

    // 秘境
    MAP_EXPLORE(130, "仙兽秘境/冒险探索"),
    AUTO_EXPLORE(131, "神游次数"),
    GOD_ROAD(132, "丹药秘境（成仙路）"),
    TIER_MAP_EXPLORE(133, "仙器秘境"),
    MATE_PRACTICE(136, "仙缘双休"),

    // 封魔
    MAIN_STORY_BOSS(140, "北冥魔海"),
    PET_BOSS(141, "不死魔巢"),
    SPIRITUAL_WORLD_BOSS(142, "蛇陵魔窟（灵界器灵）"),
    WORLD_BOSS(143, "魔王入侵（世界boss）"),
    MATE_BOSS(144, "仙缘试炼"),

    // 礼包类型 value: 金额，param1: 礼包ID
    OPEN_SERVICE_RANK_PACK(1000, "开服排行礼包"),
    GIFT_PACK(1001, "特惠礼包"),
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
}


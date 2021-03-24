package org.jeecg.modules.game.constant;

import lombok.Getter;

/**
 * 背包扣减物品 way 类型
 * @author Administrator
 */
@Getter
public enum ItemReduce {
    /**
     * 系统定义
     */
    DEFAULT(0, "默认"),
    GM(1, "GM命令"),
    ADMIN(2, "运营后台"),
    ANIMA_FIELD(3, "洞府-灵田-生产"),
    ANIMA_FIELD_UPGRADE(4, "洞府-灵田-升级"),
    SPIRIT_ELEMENT_UPGRADE(5, "洞府-灵根-灵根升级"),
    SPIRIT_ROOT_UPGRADE(6, "洞府-灵根-聚灵珠升级"),
    GONG_METHOD_LEARN(7, "洞府-功法-学习功法"),
    GONG_METHOD_GRADE(8, "洞府-功法-功法升级"),
    GONG_METHOD_INFER(9, "洞府-功法-推演"),
    ALCHEMY_LEARN(10, "洞府-炼丹-学习丹方"),
    ALCHEMY(11, "洞府-炼丹-炼丹"),
    REFINER_LEARN(12, "洞府-炼器-学习图纸"),
    REFINER(13, "洞府-炼器-炼器"),
    COMPOSE(14, "洞府-合成"),
    WEAPON_ACTIVATE(15, "法宝-激活法宝"),
    WEAPON_LEVEL_UP(16, "法宝-精炼"),
    WEAPON_MEDICINE(17, "法宝-精粹"),
    WEAPON_SUIT_UPGRADE(18, "法宝-套装锻造"),
    WEAPON_STAR(19, "法宝-拓星"),
    WEAPON_JEWEL_COMPOSE(20, "法宝-符文-快捷合成"),
    WEAPON_JEWEL_UPGRADE(21, "法宝-符文精炼"),
    WEAPON_JEWEL_STAR(22, "法宝-符文拓星"),
    WEAPON_SPIRIT(23, "法宝-铸灵"),
    WEAPON_ADD(24, "法宝-追加"),
    PET_ACTIVATE(25, "仙兽-激活"),
    PET_UPGRADE(26, "仙兽-喂养"),
    PET_STAR(27, "仙兽-觉醒"),
    PET_WEAPON_ACTIVATE(28, "仙兽-装备激活"),
    PET_WEAPON_UPGRADE(29, "仙兽-装备升级"),
    PET_DRAW(30, "仙兽-仙兽宫"),
    FACTION_DONATION(31, "仙盟-大殿-贡献"),
    FACTION_MEAL(32, "仙盟-仙宴"),
    FACTION_DEPOSITORY(33, "仙盟-仓库"),
    BAG_ITEM_USE(34, "储物袋-道具使用"),
    ITEM_SALE(35, "储物袋-道具出售"),
    BAG_UPGRADE(36, "储物袋-储物袋升级"),
    REFINE_BODY_GATHER_ANIMA(37, "储物袋-炼体-聚灵"),
    REFINE_BODY_DESTINY(38, "储物袋-炼体-天命冲穴"),
    ORNAMENTS(39, "储物袋-灵宝升阶"),
    PERSON_SKILL(40, "储物袋-仙命-仙术推演"),
    ADD_POINT(41, "储物袋-仙命-命盘加点"),
    FASHION(42, "储物袋-时装-激活"),
    FASHION_UPGRADE(43, "储物袋-时装-升级"),
    LITTLE_PET_ACTIVATE(44, "储物袋-圣灵-激活"),
    LITTLE_PET_UPGRADE(45, "储物袋-圣灵-升阶"),
    FATE_EQUIP_UPGRADE(46, "储物袋-飞剑-打磨"),
    FATE_EQUIP_STAR(47, "储物袋-飞剑-重铸"),
    FATE_EQUIP_MEDICINE(48, "储物袋-飞剑-精粹"),
    PLOT_HANG_UP_QUICK(49, "剧情-快速挂机"),
    MONEY_TREE(50, "福利-灵石商人"),
    FESTIVAL_EXCHANGE(51, "节日活动-兑换"),
    DAY_GIFT(52, "仙福-特惠礼包"),
    OPEN_SERVICE_GIFT(53, "开服排行-优惠礼包"),
    FACTION_TITTLE(54, "仙榜-称号激活称号"),
    CROSS_ROBBERY_MEDICINE(55, "渡劫-吃渡劫丹"),
    ARENA_TIMES(56, "斗法-福地夺宝-购买次数"),
    ARENA_REFRESH(57, "斗法-福地夺宝-刷新对手"),
    ARENA_CD_REFRESH(58, "斗法-福地夺宝-清除冷却"),
    ARENA_EXCHANGE(59, "斗法-福地夺宝-兑换"),
    TRAVEL_HILL_QUALITY(60, "斗法-上古遗迹-提升品质"),
    GHOST_WAR_EXCITATION(61, "斗法-神魔战场-激励"),
    GHOST_WAR_REVIVE(62, "斗法-神魔战场-复活"),
    BOSS_LEVEL_TIMES(63, "封魔-北冥魔海-购买次数"),
    BOSS_STORY_TIMES(64, "封魔-灵宝幻魔-购买次数"),
    BOSS_WORLD_CD(65, "封魔-魔王入侵-清除冷却"),
    PET_FAIRYLAND(66, "秘境-仙兽秘境-冒险"),
    PET_FAIRYLAND_QUICK(67, "秘境-仙兽秘境-快速通关"),
    PET_FAIRYLAND_TRAVEL(68, "秘境-仙兽秘境-神游"),
    PET_FAIRYLAND_TRAVEL_QUICK(69, "秘境-仙兽秘境-快速神游"),
    MEDICINAL_FAIRYLAND_SHOP(70, "秘境-丹药秘境-商店"),
    MEDICINAL_FAIRYLAND_REVIVE(71, "秘境-丹药秘境-复活"),
    SEVEN_TASKS_GIFT(72, "七日目标-每日福利-购买"),
    STONE_RETRIEVE(73, "日常-灵石找回"),
    JADE_RETRIEVE(74, "日常-仙玉找回"),
    SHOP(75, "坊市-购买"),
    CELESTIAL_REALM(76, "飞升仙界-仙脉消耗"),
    CELESTIAL_REALM_BOSS(77, "仙尊-购买次数"),
    EQUIPMENT_ALCHEMY(78, "法宝炼灵"),
    SPIRITUAL_WORLD_BOSS(79, "炼灵玄妖-购买次数"),
    FACTION_BOSS_CHALLENGE(80, "仙盟-仙盟妖兽-挑战"),
    HOLY_GHOST_UPGRADE(81, "储物袋-圣灵-升级"),
    HOLY_GHOST_APPEARANCE_ACTIVATE(82, "储物袋-圣灵-激活化形"),
    HOLY_GHOST_APPEARANCE_UPGRADE(83, "储物袋-圣灵-升星"),
    HOLY_GHOST_PITHY(84, "储物袋-圣灵-精粹"),
    EQUIPMENT_SUIT(85, "套装"),
    TIER_MAP_BUY_NUM(86, "仙器秘境-购买挑战次数"),
    IDENTIFY_LOOK(87, "鉴宝"),
    LEVEL_GIFT(88, "境界每日礼包"),
    DUEL_FIELD_CHALLENGE_TIMES(89, "斗灵道场-购买挑战次数"),
    BATTLE_PASS_UNLOCK(90, "解锁高级战令"),
    PRACTICE_FUND(91, "购买成长基金"),
    MATE_BOSS_COMBAT_BUY_NUM(92, "购买试炼挑战次数"),
    MATE_FIND_BUY_NUM(93, "购买寻缘次数"),
    MATE_PRACTISE_BUY_NUM(94, "购买双修次数"),
    MATE_COMBINE_BUY_NUM(95, "增加结缘上限"),
    MATE_KEEPSAKE_SYNTHESIS(96, "合成信物"),
    MATE_KEEPSAKE_UPGRADE(97, "强化信物"),
    MAP_TRAVEL_NUM(98, "仙兽秘境神游次数"),
    EQUIPMENT_SUIT_UPGRADE(99, "仙器器灵强化"),
    ZERO_BUY(100, "0元购购买"),
    SPIRIT_STONE_MAP_BUY_NUM(101, "灵石秘境-购买挑战次数"),
    PRACTICE_MAP_BUY_NUM(102, "修为秘境-购买挑战次数"),
    ACHIEVE_UPGRADE(103, "成就升级"),
    BLACK_SHOP_BUY(104, "黑市购买"),
    DAILY_UPGRADE(105, "阅历树升级"),
    FACTION_BUILD(106, "仙盟创建"),
    FACTION_RENAME(107, "仙盟改名"),
    GOD_ROAD_RESET(108, "丹药秘境重置"),
    GONG_METHOD_EXCHANGE(109, "功法兑换"),
    GONG_METHOD_INFER_QUICK(110, "功法快速完领悟"),
    PET_DRAW_UPGRADE(111, "仙兽宫升级"),
    PLOT_GAME(112, "剧情小玩法"),
    REWARD_TASK_QUICK(113, "悬赏任务加速完成"),
    WEAPON_JEWEL_ACTIVATE(114, "法宝符文穿戴 "),
    GONG_METHOD_DECOMPOSE(115, "功法分解"),
    CHANGE_NICKNAME(116, "改名卡"),
    FACTION_SKILL(117, "仙盟技能"),
    WEAPON_SYNTHESIS(118, "法宝合成"),
    FORTUNE_CAT(119, "招财猫"),
    WEAPON_EQUIP_SUIT(120, "法宝新套装"),
    WEAPON_ONE_KEY_SALE(121, "法宝已将出售"),
    SPIRIT_STONE_MAP_SWEEP(122, "灵石秘境-扫荡"),
    PRACTICE_MAP_SWEEP(123, "修为秘境-扫荡"),
    XIAN_ZHI(124, "仙福-仙职"),
    OPEN_SERVICE_CAMPAIGN_GIFT(125, "开服活动-开服礼包"),
    OPEN_SERVICE_CAMPAIGN_LOTTERY(126, "开服活动-排行夺宝（抽奖消耗）"),
    HEAVEN_BOOK(127, "天书"),
    TEAM_BOSS_BUY(128, "斗法-组队boss"),
    DIPPER_STAR(129, "北斗七星"),
    FIREWORK(130, "烟花"),
    WEAPON_MAP_BUY_NUM(131, "法宝秘境-购买挑战次数"),
    MARRY_CASH_GIFT(132, "结婚-礼金"),
    MARRY_DIVORCE(133, "结婚-离婚"),
    MARRY_INTIMACY_LEVEL(134, "结婚-亲密度等级"),
    MARRY_RING(135, "结婚-誓戒"),
    MARRY_KEEPSAKE(136, "结婚-信物"),
    MARRY_BOSS_BUY_NUM(137, "结婚-双修BOSS购买次数"),
    MARRY_BOSS_SINGLE_BATTLE(138, "结婚-双修BOSS单身挑战"),
    TREASURE_DRAW(139, "藏宝阁"),
    GOD_ROAD_SWEEP(140, "丹药秘境扫荡"),
    QUALIFYING_BUY(141, "谪仙台-购买次数"),
    SIX_SWORD(142, "六道剑阵"),
    PRECEPT_POWER(143, "战力护符"),
    BUSINESS_THRIVING(144, "招财进宝"),
    SPIRIT_CHANGE(145, "逆天改命"),

    THROWING_EGGS(146, "砸蛋"),
    THROWING_EGGS_SHOP(147, "砸蛋商城"),
    THROWING_EGGS_GIFT(148, "砸蛋礼包"),

    // endå
    ;

    private final Integer id;
    private final String name;

    ItemReduce(Integer type, String name) {
        this.id = type;
        this.name = name;
    }

    public static ItemReduce valueOf(int type) {
        for (ItemReduce itemReduce : ItemReduce.values()) {
            if (type == itemReduce.getId()) {
                return itemReduce;
            }
        }
        return DEFAULT;
    }
}
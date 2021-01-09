package org.jeecg.modules.game.constant;

import lombok.Getter;

/**
 * 背包获得物品 way 类型
 */
@Getter
public enum ItemRuleId {
    /**
     * 背包获得物品 way 类型
     */
    /**
     /**
     * 奖励模块
     */
    DEFAULT(0, "系统默认"),
    GM(1, "GM命令获取"),
    ADMIN(2, "后台操作"),
    REDEEM_CODE(11, "兑换码"),
    ORDER(12, "支付订单"),
    PRACTICE_UPGRADE(13, "境界提升奖励"),
    UNLOCK(14, "功能解锁"),
    EMAIL(15, "传书"),
    PRACTICE(16, "福利-修炼奖励"),
    SYNTHESIS(17, "背包-合成"),
    ITEM_SALE(18, "储物袋-出售道具"),
    UNGIFT_ITEM(19, "储物袋-使用非礼包道具"),
    GIFT_ITEM(20, "储物袋-使用各类礼包道具"),
    ITEM_QUICKLY_BUY(21, "道具不足快捷购买"),
    SHOP(22, "坊市"),
    GONG_METHOD(23, "洞府-功法"),
    ALCHEMY_HOUSE(24, "洞府-炼丹"),
    REFINERY_WORK_SHOP(25, "洞府-炼器"),
    VISITING_CARD(26, "洞府-仙榜-名帖"),
    SPIRIT(27, "灵根"),
    ANIMA_FIELD(28, "灵田"),
    MAIN_TASK(29, "主线任务"),
    REALM(30, "飞升界奖励"),
    GONG(31, "功法"),
    GONG_EXCHANGE(32, "功法-兑换"),
    GONG_DECOMPOSE(33, "功法-分解"),
    GONG_COMPREHEND(34, "功法-领悟"),
    STORY(35, "剧情"),
    STORY_REWARD_TRAVEL(36, "剧情-剧情游历"),
    STORY_RANK(37, "剧情-万仙榜"),
    STORY_PRACTICE_ROAD(38, "剧情-修真之路"),
    DAILY_ACTIVITY_REWARD(39, "日常-活跃度奖励"),
    DAILY_TASK_REWARD(40, "日常-完成任务奖励"),
    DAILY_TASK(41, "日常-阅历奖励"),
    DAILY_TASK_RETRIEVE(42, "日常找回"),
    PET_TREASURE(43, "仙兽宫"),
    PET_EXP(44, "仙兽宫-抽奖额外掉落"),
    PET_RESONANCE_REWARD(45, "仙兽宫-羁绊-外援奖励"),
    MUMBO_JUMBO(46, "荣誉-成就-成就奖励"),
    RANKING(47, "荣誉-仙榜-膜拜"),
    DUEL_FIELD(48, "斗法-斗灵道场"),
    ARENA_SEASON_RANK(49, "斗法-福地夺宝-榜单奖励"),
    ARENA_EXCHANGE(50, "斗法-福地夺宝-兑换"),
    ARENA_SCORE_REWARD(51, "斗法-福地夺宝-积分奖励"),
    ARENA_BATTLE(52, "斗法-福地夺宝-掠夺"),
    TRAVEL_REWARD(53, "斗法-上古遗迹-领奖"),
    TRAVEL_BATTLE(54, "斗法-上古遗迹-掠夺"),
    GHOST_WAR_DAILY(55, "斗法-神魔战场-每日奖励"),
    GHOST_WAR_BATTLE(56, "斗法-神魔战场-战斗奖励"),
    PET_MOUNTAIN(57, "封魔-境灵"),
    DEVIL_INVASION(58, "封魔-魔王入侵"),
    SPIRITUAL_WORLD_BOSS(59, "封魔-蛇陵魔窟"),
    MAGIC_WEAPON(60, "封魔-心魔"),
    LEVEL_GIFT(61, "福利-境界奖励"),
    MONEY_TREE(62, "福利-灵石商人"),
    SIGN_IN(63, "福利-签到"),
    ONLINE(64, "福利-在线奖励"),
    MEDICINAL_FAIRYLAND_BOX(65, "秘境-丹药秘境-宝箱"),
    GOD_ROAD_BUY(66, "秘境-丹药秘境-购买道具"),
    GOD_ROAD_BATTLE(67, "秘境-丹药秘境-战斗奖励"),
    LINGSHAN_PUPPET(68, "秘境-符文秘境-傀儡探险"),
    LINGSHAN_BATTLE(69, "秘境-符文秘境-挑战"),
    SPIRIT_STONE_MAP_BOX(70, "秘境-灵石秘境-宝箱"),
    SPIRIT_STONE_MAP_BATTLE(71, "秘境-灵石秘境-怪物"),
    TIER_MAP_BOX(72, "秘境-套装秘境-宝箱"),
    TIER_MAP_BATTLE(73, "秘境-套装秘境-怪物"),
    ADVENTURE_BOX(74, "秘境-仙兽秘境-宝箱"),
    ADVENTURE_FAIRY_GIFT(75, "秘境-仙兽秘境-领取仙礼"),
    DIVINER_TOUR(76, "秘境-仙兽秘境-神游"),
    ADVENTURE_BATTLE(77, "秘境-仙兽秘境-战斗"),
    PRACTICE_MAP_BOX(78, "秘境-修为秘境-宝箱"),
    PRACTICE_MAP_BATTLE(79, "秘境-修为秘境-怪物"),
    FACTION_DEPOSITORY(80, "仙盟-仓库"),
    FACTION_DONATE(81, "仙盟-大殿-捐献"),
    FACTION_SHOP(82, "仙盟-店铺"),
    FACTION_WELFARE(83, "仙盟-福利"),
    LUCKY_MONEY(84, "仙盟-红包"),
    FACTION_COMMON_BOSS(85, "仙盟-仙盟妖灵"),
    FACTION_BOSS(86, "仙盟-仙盟妖兽"),
    LEVEL_PRIVILEGE(87, "境界特权（废弃）"),
    LEVEL_PRIVILEGE_REWARD(88, "境界特权-每日奖励"),
    LEVEL_PRIVILEGE_GIFT(89, "境界特权-每日礼包"),
    MATE_INTIMACY(90, "仙缘-亲密等级"),
    MATE_BOSS(91, "仙缘-试炼"),
    MATE_PRACTISE_SYNTHESIS(92, "仙缘-双修-合成"),
    MATE_FIND(93, "仙缘-寻缘"),
    REWARD_TASK(94, "除魔录-云游-悬赏任务"),
    SUIT_LEGEND(95, "传说-奖励"),
    LEVEL_CHALLENGE(96, "剑仙挑战"),
    TREASURE_IDENTIFY_COMMON(97, "鉴宝-普通奖励"),
    TREASURE_IDENTIFY_RARE(98, "鉴宝-稀有奖励"),
    VISITING_CARD_LOOK(99, "名帖-瞻仰"),
    XIANZONG_REWARD(100, "挑战仙尊掉落"),
    EXAM(101, "文曲下凡"),
    GHOST_WAR_WORSHIP(102, "至尊神殿"),
    BATTLE_PASS(103, "战令奖励"),
    BATTLE_PASS_EXCHANGE(104, "战令经验转化货币"),
    TREASURE(105, "藏宝阁"),
    FIND_TREASURE(106, "寻宝-寻宝"),
    ZERO_BUY(107, "活动-0元购购买"),
    ZERO_BUY_RECEIVE(108, "活动-0元购领取"),
    GROW_UP_FUND(109, "活动-成长基金奖励"),
    SIGN_IN_GIFT(110, "活动-登录有礼奖励"),
    FESTIVAL_EXCHANGE(111, "活动-节日活动-兑换"),
    FESTIVAL_TOTAL_RECHARGE(112, "活动-节日活动-累充"),
    FESTIVAL_SIGN(113, "活动-节日活动-签到"),
    FESTIVAL_TASK(114, "活动-节日活动-任务"),
    DAY_GIFT_PACKAGE(115, "活动-今日礼包"),
    OPEN_SERVICE_RANK(116, "活动-开服排行"),
    ACCUMULATE_RECHARGE(117, "活动-累计天数充值"),
    DAILY_RECHARGE(118, "活动-每日充值"),
    FIRST_RECHARGE(119, "活动-首充"),
    LIMIT_TIME_RECHARGE_ITEM(120, "活动-限时累充"),
    XIAN_ZHI(121, "仙福-官职"),
    COMBO_RECHARGE(122, "仙福-连续充值"),
    DAILY_GIFT(123, "仙福-每日礼包"),
    MONTH_CARD_BUY(124, "仙福-月卡-购买"),
    MONTH_CARD_REWARD(125, "仙福-月卡-领奖"),
    CELESTIAL_REALM_BOSS(126, "仙界-仙界boss"),
    SEVEN_DAY_TASK_SCORE(127, "七日目标积分奖励"),
    SEVEN_DAY_TASK_GIFT(128, "七日目标礼包"),
    SEVEN_DAY_TASK(129, "七日目标任务奖励"),
    PRACTICE_TASK(130, "天劫"),
    WEAPON_SYNTHESIS(131, "法宝-合成"),
    FORTUNE_CAT(132, "招财猫"),
    WEAPON_SALE(133, "法宝出售"),
    SPIRIT_STONE_MAP_SWEEP(134, "秘境-灵石秘境-扫荡"),
    PRACTICE_MAP_SWEEP(135, "秘境-修为秘境-扫荡"),
    XIANZHI_IMMORTAL(136, "仙福-仙职位"),
    OPEN_SERVICE_CAMPAIGN_RANK(137, "开服活动-开服排行-排名奖励"),
    OPEN_SERVICE_CAMPAIGN_STANDARD(138, "开服活动-开服排行-达标奖励"),
    OPEN_SERVICE_CAMPAIGN_GIFT(139, "开服活动-开服礼包"),
    OPEN_SERVICE_CAMPAIGN_SINGLE_GIFT(140, "开服活动-单笔好礼"),
    OPEN_SERVICE_CAMPAIGN_LOTTERY(141, "开服活动-排行夺宝-中奖奖励"),
    OPEN_SERVICE_CAMPAIGN_LOTTERY_RANK(142, "开服活动-排行夺宝-排名奖励"),
    OPEN_SERVICE_CAMPAIGN_LOTTERY_SCORE(143, "开服活动-排行夺宝-积分奖励"),
    OPEN_SERVICE_CAMPAIGN_CONSUME_REWARD(144, "开服活动-消耗有礼"),
    TEAM_BOSS_BATTLE(145, "斗法-须弥法界-伤害奖励"),
    TEAM_BOSS_SINGLE_INJURY(146, "斗法-须弥法界-周单次伤害排行奖励"),
    TEAM_BOSS_TOTAL_INJURY(147, "斗法-须弥法界-周累计伤害排行奖励"),
    TEAM_BOSS_ASSIST_REWARD(148, "斗法-须弥法界-助战奖励"),

    // end
    ;

    private final Integer id;
    private final String name;

    ItemRuleId(Integer type, String name) {
        this.id = type;
        this.name = name;
    }

    public static ItemRuleId valueOf(int type) {
        for (ItemRuleId itemReduce : ItemRuleId.values()) {
            if (type == itemReduce.getId()) {
                return itemReduce;
            }
        }
        return DEFAULT;
    }
}
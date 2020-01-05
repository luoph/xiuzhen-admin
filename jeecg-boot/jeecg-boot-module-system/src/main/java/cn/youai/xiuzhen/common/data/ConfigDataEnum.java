package cn.youai.xiuzhen.common.data;

import lombok.Getter;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * @author buliangliang
 * 数据表 实体类  modulecode映射关系 类似于数据字典
 * 后续可将其改成xml的形式配置
 */
public enum ConfigDataEnum {

    /**
     * 配置表与pojo类的映射关系
     */

    /** =================== 以下是帐号&登录相关 =================== */

    /**
     * 服务器信息
     */
    BOY_NAME("boy_name", "cn.youai.xiuzhen.entity.pojo.RandomName", 110),
    GIRL_NAME("girl_name", "cn.youai.xiuzhen.entity.pojo.RandomName", 111),
    INIT_REWARD("init_reward", "cn.youai.xiuzhen.entity.pojo.InitReward", 112),
    BIRTH("birth", "cn.youai.xiuzhen.entity.pojo.Birth", 113),

    /**
     * 系统开放
     */
    SYSTEM_OPEN("system_open", "cn.youai.xiuzhen.entity.pojo.SystemOpen", 200),

    /**
     * 功能开放
     */
    FEATURE("feature", "cn.youai.xiuzhen.entity.pojo.Feature", 201),

    /**
     * 新手任务
     */
    NOVICE_TASK("novice_task", "cn.youai.xiuzhen.entity.pojo.NoviceTask", 301),
    NOVICE_REWARD("novice_reward", "cn.youai.xiuzhen.entity.pojo.Reward", 302),
    NOVICE_ITEM("novice_item", "cn.youai.xiuzhen.entity.pojo.FallItem", 303),

    /**
     * 数值模块关系表
     */
    MODULE_RELATION("module_relation", "cn.youai.xiuzhen.entity.pojo.ModuleRelation", 400),

    /**
     * 按钮
     */
    BUTTON("button", "cn.youai.xiuzhen.entity.conf.ConfButton", 900),

    /** =================== 以下是道具&修为相关 =================== */
    /**
     * 道具
     */
    ITEM("item", "cn.youai.xiuzhen.entity.pojo.Item", 1000),

    /**
     * 淬体经验
     */
    BODY_EXP("body_exp", "cn.youai.xiuzhen.entity.pojo.BodyExp", 1001),
    /**
     * 淬体属性
     */
    BODY_UPGRADE("body_upgrade", "cn.youai.xiuzhen.entity.pojo.BodyUpgrade", 1002),
    /**
     * 修炼经验
     */
    PRACTICE_EXP("practice_exp", "cn.youai.xiuzhen.entity.pojo.PracticeExp", 1003),
    /**
     * 修炼经验
     */
    PRACTICE_UPGRADE("practice_upgrade", "cn.youai.xiuzhen.entity.pojo.PracticeUpgrade", 1004),
    /**
     * 战斗奖励捆绑配置
     */
    CHECKPOINT_FIGHT("checkpoint_fight_1", "cn.youai.xiuzhen.entity.pojo.CheckpointFight", 1005),
    /**
     * 掉落道具配置
     */
    CHECKPOINT_ITEM("checkpoint_item_1", "cn.youai.xiuzhen.entity.pojo.FallItem", 1006),
    /**
     * 奖励组配置
     */
    CHECKPOINT_REWARD("checkpoint_reward_1", "cn.youai.xiuzhen.entity.pojo.Reward", 1007),
    /**
     * 怪物表
     */
    CHECKPOINT_MONSTER("checkpoint_monster_1", "cn.youai.xiuzhen.entity.pojo.MapCheckpointMonster", 1008),

    /**
     * 道具使用效果
     */
    ITEM_USE_EFFECT("item_use", "cn.youai.xiuzhen.entity.pojo.ItemUseEffect", 1009),

    /**
     * 灵池表
     */
    SPIRIT_LIST("spirit_list", "cn.youai.xiuzhen.entity.pojo.SpiritList", 1010),

    /**
     * 灵根等级
     */
    SPIRIT_ROOT("spirit_root", "cn.youai.xiuzhen.entity.pojo.SpiritRoot", 1011),

    /***
     *  修炼消息
     */
    PRACTICE_MESSAGE("practice_message", "cn.youai.xiuzhen.entity.pojo.PracticeMessage", 1012),

    /** =================== 以下是地图相关 =================== */
    /**
     * 一级地图关卡
     */
    MAP_PRIME_CHECKPOINT("map_prime_checkpoint", "cn.youai.xiuzhen.entity.pojo.MapPrimeCheckpoint", 1100),

    /**
     * 二级关卡
     */
    MAP_CHECKPOINT("map_checkpoint", "cn.youai.xiuzhen.entity.pojo.MapCheckpoint", 1101),

    /**
     * 地图关卡数据
     */
    MAP_CHECKPOINT_DATA("map_checkpoint_data", "cn.youai.xiuzhen.entity.pojo.MapCheckpointData", 1102),

    /**
     * 地图关卡怪物数据
     */
    MAP_CHECKPOINT_MONSTER("map_checkpoint_monster", "cn.youai.xiuzhen.entity.pojo.MapCheckpointMonster", 1103),

    /**
     * 地图关卡Item
     */
    MAP_CHECKPOINT_ITEM("map_checkpoint_item", "cn.youai.xiuzhen.entity.pojo.FallItem", 1104),

    /**
     * 地图关卡Reward配置
     */
    MAP_CHECKPOINT_REWARD("map_checkpoint_reward", "cn.youai.xiuzhen.entity.pojo.Reward", 1105),

    /**
     * 地图道具分组
     */
    MAP_ITEM_TYPE("map_item_type", "cn.youai.xiuzhen.entity.pojo.MapItemType", 1106),

    /**
     * 出窍脚本
     */
    MAP_CHECKPOINT_SCRIPT("map_checkpoint_script", "cn.youai.xiuzhen.entity.pojo.MapCheckpointScript", 1107),

    /**
     * 出窍脚本配置
     */
    MAP_SCRIPT_TEXT("map_script_text", "cn.youai.xiuzhen.entity.pojo.MapScriptText", 1108),

    /**========================以下是功法相关导表===============================*/
    /**
     * 功法表
     */
    GONG_METHOD("gong_method", "cn.youai.xiuzhen.entity.pojo.GongMethod", 1200),
    /**
     * 功法限制
     */
    GONGMETHOD_LIMIT("gongmethod_limit", "cn.youai.xiuzhen.entity.pojo.GongMethodLimit", 1201),
    /**
     * 功法限制升级
     */
    GONGMETHOD_LIMIT_UPGRADE("gongmethod_limit_upgrade", "cn.youai.xiuzhen.entity.pojo.GongMethodLimitUpgrade", 1202),
    /**
     * 功法升级
     */
    GONGMETHOD_UPGRADE("gongmethod_upgrade", "cn.youai.xiuzhen.entity.pojo.GongMethodUpgrade", 1203),

    /**=========================以下是商城系統导表==============================*/
    /**
     * 商城表
     */
    SHOP("shop", "cn.youai.xiuzhen.entity.pojo.ShopMall", 1300),
    /**
     * 货物表
     */
    SHOP_GOODS("shop_goods", "cn.youai.xiuzhen.entity.pojo.ShopGoods", 1301),
    /** =================== 以下是洞府相关 =================== */
    /**
     * 洞府消耗配置
     */
    ABODE_EXPEND("abode_expend", "cn.youai.xiuzhen.entity.pojo.AbodeExpend", 1302),
    /**
     * 洞府基础配置
     */
    ABODE_RESOURCES("abode_resources", "cn.youai.xiuzhen.entity.pojo.AbodeResources", 1303),

    /** =================== 以下是传承相关 =================== */
    /**
     * 传承基础配置
     */
    INHERIT("inherit", "cn.youai.xiuzhen.entity.pojo.Inherit", 1400),

    /**
     * 传承类目
     */
    INHERIT_STAR("inherit_star", "cn.youai.xiuzhen.entity.pojo.InheritStar", 1401),

    /**
     * =======================门派相关配置=======================
     */
    /**
     * 门派职位配置
     */
    SECT_POSITION("sect_position", "cn.youai.xiuzhen.entity.pojo.SectPosition", 1500),
    /**
     * 门派兑换
     */
    SECT_EXCHANGE("sect_exchange", "cn.youai.xiuzhen.entity.pojo.SectExchange", 1501),
    /**
     * 门派功法
     */
    SECT_GONGMETHOD("sect_gong_method", "cn.youai.xiuzhen.entity.pojo.SectGongMethod", 1502),
    /**
     * 门派功能开放
     */
    SECT_FUNCTION("sect_function", "cn.youai.xiuzhen.entity.pojo.SectFunction", 1503),
    /**
     * 门派任务
     */
    SECT_TASK("sect_task", "cn.youai.xiuzhen.entity.pojo.SectTask", 1504),
    /**
     * 门派奖励组配置
     */
    SECT_REWARD("sect_reward", "cn.youai.xiuzhen.entity.pojo.Reward", 1505),
    /**
     * 门派奖励道具配置
     */
    SECT_ITEM("sect_item", "cn.youai.xiuzhen.entity.pojo.FallItem", 1506),
    /**
     * 门派炼丹主配置
     */
    ITEM_COMPOSE("item_compose", "cn.youai.xiuzhen.entity.pojo.ItemCompose", 1507),
    /**
     * 门派炼丹类型配置
     */
    ITEM_COMPOSE_TYPE("item_compose_type", "cn.youai.xiuzhen.entity.pojo.ItemComposeType", 1508),
    /**
     * 门派炼丹炉子配置
     */
    ITEM_COMPOSE_STOVE("item_compose_stove", "cn.youai.xiuzhen.entity.pojo.ItemComposeStove", 1509),
    /**
     * 门派炼丹炉子成长线
     */
    DRUG_LEVEL("drug_level", "cn.youai.xiuzhen.entity.pojo.DrugLevel", 1510),
    /**
     * 门派炼丹合成掉落配置
     */
    //ITEM_COMPOSE_ITEM("item_compose_item", "cn.youai.xiuzhen.entity.pojo.ItemComposeItem", 1511),
    /**
     * 炼器配置
     */
    APPLIANCES_LEVEL("appliances_level", "cn.youai.xiuzhen.entity.pojo.AppliancesLevel", 1512),
    /**
     * 游历系统配置
     */
    TRAVEL("travel", "cn.youai.xiuzhen.entity.pojo.Travel", 1513),
    /**
     * 游历奖励道具使用详情配置
     */
    TRAVEL_USE("travel_use", "cn.youai.xiuzhen.entity.pojo.TravelUse", 1514),
    /**
     * 练功房打坐配置
     */
    SIT_MEDITATED("practice_room", "cn.youai.xiuzhen.entity.pojo.SitMeditated", 1515),
    /**
     * 练功房聚气配置
     */
    GAS_GATHERING("practice_room_pay", "cn.youai.xiuzhen.entity.pojo.GasGather", 1516),
    /**
     * ======================装备系统（法宝）==========================
     */
    /**
     * 法宝属性
     */
    EQUIP("equip", "cn.youai.xiuzhen.entity.pojo.Equip", 1600),

    /**
     * 法宝升级
     */
    EQUIP_UPGRADE("equip_upgrade", "cn.youai.xiuzhen.entity.pojo.EquipUpgrade", 1601),

    /**
     * ===========================以下是宝箱系统的奖励物品======================================
     */
    /**
     * 道具组
     */
    ITEM_GROUP("item_group", "cn.youai.xiuzhen.entity.pojo.ItemGroup", 1701),
    /**
     * 奖励物品
     */
    ITEM_GROUP_DETAIL("item_group_detail", "cn.youai.xiuzhen.entity.pojo.ItemGroupDetail", 1702),

    /**
     * ===========================以下是小灵山的相关配置==========================================
     */
    CHECKPOINT_LINGSHAN("explore_prime", "cn.youai.xiuzhen.entity.pojo.CheckpointLingShan", 1801),
    CHECKPOINT_LINGSHAN_LIST("explore_main", "cn.youai.xiuzhen.entity.pojo.CheckpointLingShanList", 1802),
    CHECKPOINT_REWARD_EXPLOREDAILY("reward_exploredaily", "cn.youai.xiuzhen.entity.pojo.ExploreDailyReward", 1803),
    CHECKPOINT_ITEM_EXPLORE("item_explore", "cn.youai.xiuzhen.entity.pojo.FallItem", 1804),
    CHECKPOINT_REWARD_EXPLORE("reward_explore", "cn.youai.xiuzhen.entity.pojo.Reward", 1805),

    /**
     * 战斗奖励捆绑配置
     */
    EXPLORE_CHECKPOINT_FIGHT("checkpoint_fight_explore", "cn.youai.xiuzhen.entity.pojo.CheckpointFight", 1806),
    /**
     * 掉落道具配置
     */
    EXPLORE_CHECKPOINT_ITEM("checkpoint_item_explore", "cn.youai.xiuzhen.entity.pojo.FallItem", 1807),
    /**
     * 奖励组配置
     */
    EXPLORE_CHECKPOINT_REWARD("checkpoint_reward_explore", "cn.youai.xiuzhen.entity.pojo.Reward", 1808),
    /**
     * 怪物表
     */
    EXPLORE_CHECKPOINT_MONSTER("checkpoint_monster_explore", "cn.youai.xiuzhen.entity.pojo.MapCheckpointMonster", 1809),

    /**
     * 境界特权配置
     */
    LEVEL_PRIVILEGE("level_privilege", "cn.youai.xiuzhen.entity.pojo.LevelPrivilege", 1900),

    /**
     * 境界特权奖励配置
     */
    LEVEL_PRIVILEGE_REWARD("level_privilege_reward", "cn.youai.xiuzhen.entity.pojo.Reward", 1901),
    /**
     * 境界特权奖励道具配置
     */
    LEVEL_ITEM("level_item", "cn.youai.xiuzhen.entity.pojo.FallItem", 1902),
    /**
     * 问卷调查题目配置
     */
    SURVEY("survey", "cn.youai.xiuzhen.entity.pojo.Survey", 1903),
    /**
     * 问卷调查奖励配置
     */
    SURVEY_REWARD("survey_reward", "cn.youai.xiuzhen.entity.pojo.SurveyReward", 1904),

    /**
     * =======================丹药系统=======================
     */
    /**
     * 丹药信息
     */
    MEDICINE("medicine", "cn.youai.xiuzhen.entity.pojo.Medicine", 2001),
    /**
     * 丹药属性
     */
    MEDICINE_VALUE("medicine_value", "cn.youai.xiuzhen.entity.pojo.MedicineValue", 2002),

    /**
     * =====================背包扩容===============================
     */
    BAG("bag", "cn.youai.xiuzhen.entity.pojo.Bag", 2101),

    /**
     * ==========================日常任务==============================
     */
    DAILY_MAIN("daily_main", "cn.youai.xiuzhen.entity.pojo.DailyMain", 2201),
    DAILY_TODAY_AWARD("daily_today_award", "cn.youai.xiuzhen.entity.pojo.DailyTodayAward", 2202),
    DAILY_UPGRADE("daily_upgrade", "cn.youai.xiuzhen.entity.pojo.DailyUpgrade", 2203),
    DAILY_REWARD("daily_reward", "cn.youai.xiuzhen.entity.pojo.Reward", 2204),
    DAILY_REWARD_ITEM("daily_item", "cn.youai.xiuzhen.entity.pojo.FallItem", 2205),

    /**
     * ==========================配置邮件列表===================================
     */
    CONFIG_EMAIL("config_email", "cn.youai.xiuzhen.entity.pojo.ConfigEmail", 2301),

    /**
     * =============================境界特权奖励===================================
     */

    LEVEL_SYSTEM_INTRODUCE("level_system_introduce", "cn.youai.xiuzhen.entity.pojo.LevelSystemIntroduce", 2401),

    LEVEL_SYSTEM_REWARD("level_system_reward", "cn.youai.xiuzhen.entity.pojo.Reward", 2402),

    LEVEL_SYSTEM_ITEM("level_system_item", "cn.youai.xiuzhen.entity.pojo.FallItem", 2403),

    /**
     * =================================主线任务奖励==================================
     */
    TASK_MAIN("task_main", "cn.youai.xiuzhen.entity.pojo.TaskMain", 2405),

    TASK_MAIN_REWARD("task_main_reward", "cn.youai.xiuzhen.entity.pojo.Reward", 2406),

    TASK_MAIN_ITEM("task_main_item", "cn.youai.xiuzhen.entity.pojo.FallItem", 2407),

    /**
     * =========================活动邮件自动下发====================================
     */
    ACTIVITY_CONFIG("activity_config", "cn.youai.xiuzhen.entity.pojo.ActivityConfig", 2501),
    ACTIVITY_EMAIL("activity_email", "cn.youai.xiuzhen.entity.pojo.ConfigEmail", 2502),

    /**
     * =========================门派功法套装配置====================================
     */
    GONG_METHOD_TYPE("gong_method_type", "cn.youai.xiuzhen.entity.pojo.GongMethodType", 2601),
    GONG_METHOD_SECT("gong_method_sect", "cn.youai.xiuzhen.entity.pojo.GongMethodSect", 2602),

    /**
     * ========================= 技能 & BUFF ====================================
     */
    SKILL("skill", "cn.youai.xiuzhen.entity.conf.ConfSkill", 2701),
    SKILL_EFFECT("skill_effect", "cn.youai.xiuzhen.entity.conf.ConfSkillEffect", 2702),
    BUFF("buff", "cn.youai.xiuzhen.entity.conf.ConfBuff", 2703),

    /**
     * ========================= 灵兽相关 ====================================
     */
    PET("pet", "cn.youai.xiuzhen.entity.conf.ConfPet", 2801),
    PET_EQUIP("pet_equip", "cn.youai.xiuzhen.entity.conf.ConfPetEquip", 2802),
    PET_EQUIP_EXP("pet_equip_exp", "cn.youai.xiuzhen.entity.conf.ConfPetEquipExp", 2803),
    PET_EQUIP_POSITION("pet_equip_position", "cn.youai.xiuzhen.entity.conf.ConfPetEquipPosition", 2804),
    PET_EQUIP_STAR_EXP("pet_equip_star_exp", "cn.youai.xiuzhen.entity.conf.ConfPetEquipStarExp", 2805),
    PET_EQUIP_STAR_UPGRADE("pet_equip_star_upgrade", "cn.youai.xiuzhen.entity.conf.ConfPetEquipStarUpgrade", 2806),
    PET_EQUIP_UPGRADE("pet_equip_upgrade", "cn.youai.xiuzhen.entity.conf.ConfPetEquipUpgrade", 2807),
    PET_EXP("pet_exp", "cn.youai.xiuzhen.entity.conf.ConfPetExp", 2808),
    PET_STAR_EXP("pet_star_exp", "cn.youai.xiuzhen.entity.conf.ConfPetStarExp", 2809),
    PET_STAR_UPGRADE("pet_star_upgrade", "cn.youai.xiuzhen.entity.conf.ConfPetStarUpgrade", 2810),
    PET_UPGRADE("pet_upgrade", "cn.youai.xiuzhen.entity.conf.ConfPetUpgrade", 2811),

    /**
     * ===================================灵兽boss相关================================
     */
    BOSS_PET_UPGRADE("boss_pet_upgrade", "cn.youai.xiuzhen.entity.bosspet.BossPetUpgrade", 2901),
    BOSS_PET_GROUP("boss_pet_group", "cn.youai.xiuzhen.entity.bosspet.BossPetGroup", 2902),
    BOSS_PET_REWARD("boss_pet_reward", "cn.youai.xiuzhen.entity.pojo.Reward", 2903),
    BOSS_PET_ITEM("boss_pet_item", "cn.youai.xiuzhen.entity.pojo.FallItem", 2904),
    BOSS_PET_REWARD_LIMIT("boss_pet_reward_limit", "cn.youai.xiuzhen.entity.bosspet.BossPetRewardLimit", 2905),
    CHECKPOINT_MONSTER_BOSS_PET("checkpoint_monster_bosspet", "cn.youai.xiuzhen.entity.pojo.MapCheckpointMonster", 2906),
    BOSS_PET_NUM("boss_pet_num", "cn.youai.xiuzhen.entity.bosspet.BossPetNum", 2907),
    CHECKPOINT_FIGHT_BOSS_PET("checkpoint_fight_bosspet", "cn.youai.xiuzhen.entity.pojo.CheckpointFight", 2908),
    /**
     * 物品不足
     */
    ITEM_USE_ENOUGH("use_resources", "cn.youai.xiuzhen.entity.pojo.ItemNotEnoughAutoUse", 2910),

    /**
     * ===================================灵兽抽卡相关================================
     */
    PET_DRAW_EXP("pet_draw_exp", "cn.youai.xiuzhen.entity.conf.ConfPetDrawExp", 3000),
    PET_DRAW_ITEM("pet_draw_item", "cn.youai.xiuzhen.entity.pojo.FallItem", 3001),
    PET_DRAW_MAJOR("pet_draw_major", "cn.youai.xiuzhen.entity.conf.ConfPetDrawMajor", 3002),
    PET_DRAW_REWARD_LIMIT("pet_draw_reward_limit", "cn.youai.xiuzhen.entity.conf.ConfPetDrawRewardLimit", 3003),
    PET_DRAW_REWARD("pet_draw_reward", "cn.youai.xiuzhen.entity.pojo.Reward", 3004),
    PET_DRAW_TYPE_REWARD("pet_draw_type_reward", "cn.youai.xiuzhen.entity.conf.ConfPetDrawTypeReward", 3005),
    PET_DRAW_UPGRADE("pet_draw_upgrade", "cn.youai.xiuzhen.entity.conf.ConfLevelUpgrade", 3006),

    /**
     * =================================== 推演 ================================
     */
    INFER_SECTS("infer_sects", "cn.youai.xiuzhen.entity.conf.ConfInferSects", 3100),
    INFER_GROUP("infer_group", "cn.youai.xiuzhen.entity.conf.ConfInferGroup", 3101),
    INFER_ITEM("infer_item", "cn.youai.xiuzhen.entity.conf.ConfInferItem", 3102),

    /**
     * ====================================== 本命法宝 ==========================================
     */
    FATE_EQUIP("fate_equip", "cn.youai.xiuzhen.entity.fateequip.FateEquip", 3201),
    FATE_EQUIP_EXP("fate_equip_exp", "cn.youai.xiuzhen.entity.fateequip.FateEquipExp", 3202),
    FATE_EQUIP_MEDICINAL("fate_equip_medicinal", "cn.youai.xiuzhen.entity.fateequip.FateEquipMedicinal", 3203),
    FATE_EQUIP_STAR_EXP("fate_equip_star_exp", "cn.youai.xiuzhen.entity.fateequip.FateEquipStarExp", 3204),
    FATE_EQUIP_STAR_UPGRADE("fate_equip_star_upgrade", "cn.youai.xiuzhen.entity.fateequip.FateEquipStarUpgrade", 3205),
    FATE_EQUIP_UPGRADE("fate_equip_upgrade", "cn.youai.xiuzhen.entity.fateequip.FateEquipUpgrade", 3206),

    /**
     * ====================================== 合成 ==========================================
     */
    SYNTHESIS("synthesis", "cn.youai.xiuzhen.entity.conf.ConfSynthesis", 3301),
    SYNTHESIS_TYPE("synthesis_type", "cn.youai.xiuzhen.entity.conf.ConfSynthesisType", 3302),

    /**
     * ====================================== 仙职 ==========================================
     */
    IMMORTAL_POST("immortal_post", "cn.youai.xiuzhen.entity.conf.ConfImmortalPost", 3401),
    IMMORTAL_CONTENT("immortal_content", "cn.youai.xiuzhen.entity.conf.ConfImmortalContent", 3402),

    /**
     * ===================================本命法宝抽奖================================
     */
    FATE_DRAW_EXP("fate_equip_draw_exp", "cn.youai.xiuzhen.entity.conf.ConfPetDrawExp", 3500),
    FATE_DRAW_ITEM("fate_equip_draw_item", "cn.youai.xiuzhen.entity.pojo.FallItem", 3501),
    FATE_DRAW_MAJOR("fate_equip_draw_major", "cn.youai.xiuzhen.entity.conf.ConfPetDrawMajor", 3502),
    FATE_DRAW_REWARD_LIMIT("fate_equip_draw_reward_limit", "cn.youai.xiuzhen.entity.conf.ConfPetDrawRewardLimit", 3503),
    FATE_DRAW_REWARD("fate_equip_draw_reward", "cn.youai.xiuzhen.entity.pojo.Reward", 3504),
    FATE_DRAW_TYPE_REWARD("fate_equip_draw_type_reward", "cn.youai.xiuzhen.entity.conf.ConfPetDrawTypeReward", 3505),
    FATE_DRAW_UPGRADE("fate_equip_draw_upgrade", "cn.youai.xiuzhen.entity.conf.ConfLevelUpgrade", 3506),
    FATE_DRAW_EXCHANGE("gamble_exchange", "cn.youai.xiuzhen.entity.fateequip.FateGambleExchange", 3507),

    /**
     * ===================================首充================================
     */
    FIRST_CHARGE("first_charge", "cn.youai.xiuzhen.entity.conf.ConfFirstCharge", 3600),
    FIRST_CHARGE_ITEM("first_charge_item", "cn.youai.xiuzhen.entity.pojo.FallItem", 3601),
    FIRST_CHARGE_REWARD("first_charge_reward", "cn.youai.xiuzhen.entity.pojo.Reward", 3602),

    /**
     * =============================资源跳转===================================
     */
    JUMP_CONNECTION("jump_connection", "cn.youai.xiuzhen.entity.pojo.JumpConnection", 3701),

    /**
     * =============================充值===================================
     */
    RECHARGE_GOODS("recharge_goods", "cn.youai.xiuzhen.entity.conf.ConfRechargeGoods", 3800),
    RECHARGE_POSITION("recharge_position", "cn.youai.xiuzhen.entity.conf.ConfRechargePosition", 3801),
    // 每日充值
    DAILY_RECHARGE("daily_recharge", "cn.youai.xiuzhen.entity.conf.ConfDailyRecharge", 3802),
    DAILY_RECHARGE_ITEM("daily_recharge_item", "cn.youai.xiuzhen.entity.pojo.FallItem", 3803),
    DAILY_RECHARGE_REWARD("daily_recharge_reward", "cn.youai.xiuzhen.entity.pojo.Reward", 3804),

    /**
     * =============================节日===================================
     */
    FESTIVAL_TYPE("festival_type", "cn.youai.xiuzhen.entity.conf.ConfFestivalType", 3900),
    FESTIVAL_SIGN("festival_sign", "cn.youai.xiuzhen.entity.conf.ConfFestivalSign", 3901),
    FESTIVAL_RECHARGE("festival_recharge", "cn.youai.xiuzhen.entity.conf.ConfFestivalRecharge", 3902),
    FESTIVAL_EXCHANGE("festival_exchange", "cn.youai.xiuzhen.entity.conf.ConfFestivalExchange", 3903),
    FESTIVAL_ITEM("festival_item", "cn.youai.xiuzhen.entity.pojo.FallItem", 3904),
    FESTIVAL_REWARD("festival_reward", "cn.youai.xiuzhen.entity.pojo.Reward", 3905),

    /**
     * =============================七天巡礼===================================
     */
    SEVEN_GIFT("seven_gift_activity", "cn.youai.xiuzhen.entity.activity.SevenGift", 4001),
    SEVEN_GIFT_TASK("seven_gift_tasks", "cn.youai.xiuzhen.entity.activity.SevenGiftTasks", 4002),

    /**
     * =============================在线奖励（品茗）===================================
     */
    ONLINE("online", "cn.youai.xiuzhen.entity.conf.ConfOnline", 4101),
    ONLINE_ITEM("online_item", "cn.youai.xiuzhen.entity.pojo.FallItem", 4102),
    ONLINE_REWARD("online_reward", "cn.youai.xiuzhen.entity.pojo.Reward", 4103),

    /**
     * =============================登录奖励（签到）===================================
     */
    SIGN("sign", "cn.youai.xiuzhen.entity.conf.ConfSign", 4201),
    TOTAL_SIGN("total_sign", "cn.youai.xiuzhen.entity.conf.ConfTotalSign", 4202),
    SIGN_ITEM("sign_item", "cn.youai.xiuzhen.entity.pojo.FallItem", 4203),
    SIGN_REWARD("sign_reward", "cn.youai.xiuzhen.entity.pojo.Reward", 4204),

    /**
     * 背包飘字
     */
    FLOAT_WORD("float_word", "cn.youai.xiuzhen.entity.conf.FloatWord", 4301),

    /**
     * 天书录
     */
    ACHIEVEMENT_MAIN("achievement_main", "cn.youai.xiuzhen.entity.achievement.AchievementMain", 4401),
    ACHIEVEMENT_TYPE("achievement_type", "cn.youai.xiuzhen.entity.achievement.AchievementType", 4402),
    ACHIEVEMENT_REWARD("achievement_reward", "cn.youai.xiuzhen.entity.pojo.Reward", 4403),
    ACHIEVEMENT_ITEM("achievement_item", "cn.youai.xiuzhen.entity.pojo.FallItem", 4404),

    /**
     * 黑市
     */
    BLACK_SHOP_GOODS("black_shop_goods", "cn.youai.xiuzhen.entity.conf.ConfBackShopGoods", 4501),

    /**
     * 主线
     */
    MAIN_STORY("main_story", "cn.youai.xiuzhen.entity.conf.ConfMainStory", 4600),
    PRIME_MAIN_STORY("prime_main_story", "cn.youai.xiuzhen.entity.conf.ConfPrimeMainStory", 4601),

    /**
     * 剧情(4700-4749)
     */
    STORY("story", "cn.youai.xiuzhen.entity.conf.ConfStory", 4700),
    STORY_FIGHT("checkpoint_fight_story", "cn.youai.xiuzhen.entity.pojo.CheckpointFight", 4701),
    STORY_MONSTER("checkpoint_monster_story", "cn.youai.xiuzhen.entity.pojo.MapCheckpointMonster", 4702),
    STORY_ITEM("checkpoint_item_story", "cn.youai.xiuzhen.entity.pojo.FallItem", 4703),
    STORY_REWARD("checkpoint_reward_story", "cn.youai.xiuzhen.entity.pojo.Reward", 4704),
    // 剧情boss(4750-4799)
    BOSS_STORY("boss_story", "cn.youai.xiuzhen.entity.conf.ConfBossStory", 4750),
    BOSS_STORY_NUM("boss_story_num", "cn.youai.xiuzhen.entity.conf.ConfBossStoryNum", 4751),
    BOSS_STORY_REWARD_LIMIT("boss_story_reward_limit", "cn.youai.xiuzhen.entity.conf.ConfBossStoryRewardLimit", 4752),
    BOSS_STORY_FIGHT("checkpoint_fight_bossstory", "cn.youai.xiuzhen.entity.pojo.CheckpointFight", 4753),
    BOSS_STORY_MONSTER("checkpoint_monster_bossstory", "cn.youai.xiuzhen.entity.pojo.MapCheckpointMonster", 4754),
    BOSS_STORY_ITEM("checkpoint_item_bossstory", "cn.youai.xiuzhen.entity.pojo.FallItem", 4755),
    BOSS_STORY_REWARD("checkpoint_reward_bossstory", "cn.youai.xiuzhen.entity.pojo.Reward", 4756),

    /**
     * 境界福利
     */
    LEVEL_GIFT("grade", "cn.youai.xiuzhen.entity.conf.ConfLevelGift", 4801),
    LEVEL_GIFT_ITEM("grade_item", "cn.youai.xiuzhen.entity.pojo.FallItem", 4802),
    LEVEL_GIFT_REWARD("grade_reward", "cn.youai.xiuzhen.entity.pojo.Reward", 4803),

    /**
     * 法宝
     */
    WEAPON("weapon", "cn.youai.xiuzhen.entity.weapon.ConfWeapon", 4901),
    WEAPON_STAR("weapon_star", "cn.youai.xiuzhen.entity.weapon.ConfWeaponStar", 4902),
    WEAPON_POSITION_EXP("weapon_position_exp", "cn.youai.xiuzhen.entity.weapon.ConfWeaponPositionExp", 4903),
    WEAPON_POSITION_UPGRADE("weapon_position_upgrade", "cn.youai.xiuzhen.entity.weapon.ConfWeaponPositionUpgrade", 4904),
    WEAPON_POSITION_STAR_EXP("weapon_position_star_exp", "cn.youai.xiuzhen.entity.weapon.ConfWeaponPositionExp", 4905),
    WEAPON_POSITION_STAR_UPGRADE("weapon_position_star_upgrade", "cn.youai.xiuzhen.entity.weapon.ConfWeaponPositionUpgrade", 4906),
    WEAPON_MEDICINAL("weapon_use", "cn.youai.xiuzhen.entity.fateequip.FateEquipMedicinal", 4907),
    WEAPON_POSITION("weapon_position", "cn.youai.xiuzhen.entity.conf.ConfPetEquipPosition", 4908),

    /**
     * 修炼奖励
     */
    TRAINING_TIME("training_time", "cn.youai.xiuzhen.entity.conf.ConfTrainingTime", 5000),
    TRAINING_TIME_ITEM("training_time_item", "cn.youai.xiuzhen.entity.pojo.FallItem", 5001),
    TRAINING_TIME_REWARD("training_time_reward", "cn.youai.xiuzhen.entity.pojo.Reward", 5002),

    /**
     * 红点显示
     */
    RED_POINT("red_point", "cn.youai.xiuzhen.entity.conf.ConfRedPoint", 5100),
	
	/**
	 * 引导
	 */
	GUIDE_STEP("guide_step", "cn.youai.xiuzhen.entity.conf.ConfGuideStep", 5200),
	GUIDE_ITEM("guide_item", "cn.youai.xiuzhen.entity.pojo.FallItem", 5201),
	GUIDE_REWARD("guide_reward", "cn.youai.xiuzhen.entity.pojo.Reward", 5202),
	
//    end
    ;


    @Getter
    private String tableName;

    @Getter
    private String entityName;

    @Getter
    private int modelCode;

    ConfigDataEnum(String tableName, String entityName, int modelCode) {
        this.tableName = tableName;
        this.entityName = entityName;
        this.modelCode = modelCode;
    }

    public static int getModuleCode(String tableName) {
        // 去掉扩展名，为了同时使用 csv 和 xls(x) 文件
        tableName = FilenameUtils.getBaseName(tableName);
        for (ConfigDataEnum e : ConfigDataEnum.values()) {
            if (e.getTableName().equals(tableName)) {
                return e.getModelCode();
            }
        }
        return 0;
    }

    public static String getEntityName(int moduleCode) {
        for (ConfigDataEnum e : ConfigDataEnum.values()) {
            if (e.getModelCode() == moduleCode) {
                return e.getEntityName();
            }
        }
        return null;
    }

    public static String getTableName(int moduleCode) {
        for (ConfigDataEnum e : ConfigDataEnum.values()) {
            if (e.getModelCode() == moduleCode) {
                return e.getTableName();
            }
        }
        return null;
    }


    public static ConfigDataEnum getByTableName(String tableName) {
        for (ConfigDataEnum e : ConfigDataEnum.values()) {
            if (StringUtils.equals(tableName, e.getTableName())) {
                return e;
            }
        }
        return null;
    }
}

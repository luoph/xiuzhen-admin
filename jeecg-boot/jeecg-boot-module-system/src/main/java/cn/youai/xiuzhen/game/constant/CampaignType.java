package cn.youai.xiuzhen.game.constant;

import cn.youai.xiuzhen.game.entity.GameCampaignTypeBase;
import cn.youai.xiuzhen.game.service.*;
import com.baomidou.mybatisplus.extension.service.IService;
import lombok.Getter;
import org.jeecg.common.util.SpringContextUtils;

/**
 * 节日活动类型
 *
 * @author luopeihuan
 */
@Getter
@SuppressWarnings("rawtypes")
public enum CampaignType {

    /**
     * 节日活动类型
     */
    LOGIN(1, "登录礼包", IGameCampaignTypeLoginService.class),
    RECHARGE(2, "累计充值", IGameCampaignTypeRechargeService.class),
    EXCHANGE(3, "节日兑换", IGameCampaignTypeExchangeService.class),
    TASK(4, "节日任务", IGameCampaignTypeTaskService.class),
    BUFF_PRACTICE(5, "修为加成", IGameCampaignTypeBuffService.class),
    BUFF_ANIMA(6, "灵气加成", IGameCampaignTypeBuffService.class),
    FALL(7, "节日掉落", IGameCampaignTypeFallService.class, IGameCampaignTypeFallRewardService.class),
    FIREWORK(8, "节日烟花", IGameCampaignTypeFireworkService.class),
    REDUCE_RANK(9, "消耗排行", IGameCampaignTypeReduceService.class),
    LIMIT_TIME_SWORD(10, "限时仙剑", IGameCampaignTypeSwordService.class),
    THROWING_EGGS(11, "砸蛋", IGameCampaignTypeThrowingEggsService.class),
    THROWING_EGGS_RANK(12, "砸蛋榜单", IGameCampaignTypeThrowingEggsRankService.class),
    THROWING_EGGS_GIFT(13, "砸蛋礼包", IGameCampaignTypeThrowingEggsGiftService.class),
    PARTY(14, "节日派对", IGameCampaignTypePartyTaskService.class, IGameCampaignTypePartyProgressService.class),
    DIRECT_PURCHASE(15, "直购礼包", IGameCampaignDirectPurchaseService.class),
    REBATE_RECHARGE(16, "返利狂欢", IGameCampaignTypeRebateRechargeService.class),
    MARRY_RANK_WINE(17, "赠酒排行榜", IGameCampaignTypeMarryRankService.class, IGameCampaignTypeMarryRankRewardService.class),
    MARRY_RANK_CHARM(18, "魅力值排行榜", IGameCampaignTypeMarryRankService.class, IGameCampaignTypeMarryRankRewardService.class),
    //    MARRY_RANK_GIFT(19, "结义排行榜-结义礼包", null, null),
    SELECT_DISCOUNT_ITEM(20, "自选特惠", IGameCampaignTypeSelectDiscountItemService.class, IGameCampaignTypeSelectDiscountMessageService.class),
    RECHARGE_RANK(21, "累充排行", IGameCampaignTypeMarryRankService.class, IGameCampaignTypeMarryRankRewardService.class),
    EMAIL_CAMPAIGN(22, "邮件活动", IGameCampaignTypeEmailItemService.class),
    // 夺宝奇兵
    RELIC_LOTTERY(23, "遗迹夺宝", IGameCampaignTypeRelicLotteryService.class, IGameCampaignTypeRelicLotteryMessageService.class),
    STAGE_TASK(24, "阶段任务", null),
    LOTTERY_TOKEN(25, "夺宝战令", null),
    // end
    ;

    private final int type;
    private final String name;
    private final Class<? extends IService> serviceClass;
    private final Class<? extends IService> subServiceClass;

    CampaignType(int type, String name, Class<? extends IService> serviceClass) {
        this(type, name, serviceClass, null);
    }

    CampaignType(int type, String name, Class<? extends IService> serviceClass, Class<? extends IService> subServiceClass) {
        this.type = type;
        this.name = name;
        this.serviceClass = serviceClass;
        this.subServiceClass = subServiceClass;
    }

    public IService getBean() {
        return SpringContextUtils.getBean(serviceClass);
    }

    public IService getSubBean() {
        return null != subServiceClass ? SpringContextUtils.getBean(subServiceClass) : null;
    }

    public Class<? extends GameCampaignTypeBase> getEntityClass() {
        return getBean().getEntityClass();
    }

    public Class<? extends GameCampaignTypeBase> getSubEntityClass() {
        return null != subServiceClass ? getSubBean().getEntityClass() : null;
    }

    public static CampaignType valueOfServiceClass(Class<? extends IService> serviceClass) {
        for (CampaignType e : CampaignType.values()) {
            if (e.serviceClass.isAssignableFrom(serviceClass)) {
                return e;
            }
        }
        return null;
    }

    public static CampaignType valueOfSubServiceClass(Class<? extends IService> subServiceClass) {
        for (CampaignType e : CampaignType.values()) {
            if (e.subServiceClass.isAssignableFrom(subServiceClass)) {
                return e;
            }
        }
        return null;
    }

    public static CampaignType valueOf(int type) {
        for (CampaignType e : CampaignType.values()) {
            if (e.type == type) {
                return e;
            }
        }
        return null;
    }
}

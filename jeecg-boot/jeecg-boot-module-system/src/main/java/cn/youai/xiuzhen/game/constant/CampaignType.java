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
    LOGIN("登录礼包", 1, IGameCampaignTypeLoginService.class),
    RECHARGE("累计充值", 2, IGameCampaignTypeRechargeService.class),
    EXCHANGE("节日兑换", 3, IGameCampaignTypeExchangeService.class),
    TASK("节日任务", 4, IGameCampaignTypeTaskService.class),
    BUFF_PRACTICE("修为加成", 5, IGameCampaignTypeBuffService.class),
    BUFF_ANIMA("灵气加成", 6, IGameCampaignTypeBuffService.class),
    FALL("节日掉落", 7, IGameCampaignTypeFallService.class, IGameCampaignTypeFallRewardService.class),
    FIREWORK("节日烟花", 8, IGameCampaignTypeFireworkService.class),
    REDUCE_RANK("消耗排行", 9, IGameCampaignTypeReduceService.class),
    LIMIT_TIME_SWORD("限时仙剑", 10, IGameCampaignTypeSwordService.class),
    THROWING_EGGS("砸蛋", 11, IGameCampaignTypeThrowingEggsService.class),
    THROWING_EGGS_RANK("砸蛋榜单", 12, IGameCampaignTypeThrowingEggsRankService.class),
    THROWING_EGGS_GIFT("砸蛋礼包", 13, IGameCampaignTypeThrowingEggsGiftService.class),
    PARTY("节日派对", 14, IGameCampaignTypePartyTaskService.class, IGameCampaignTypePartyProgressService.class),
    DIRECT_PURCHASE("直购礼包", 15, IGameCampaignDirectPurchaseService.class),
    REBATE_RECHARGE("返利狂欢", 16, IGameCampaignTypeRebateRechargeService.class),
    MARRY_RANK_WINE("赠酒排行榜", 17, IGameCampaignTypeMarryRankService.class, IGameCampaignTypeMarryRankRewardService.class),
    MARRY_RANK_CHARM("魅力值排行榜", 18, IGameCampaignTypeMarryRankService.class, IGameCampaignTypeMarryRankRewardService.class),
    //    MARRY_RANK_GIFT("结义排行榜-结义礼包", 19, null, null),
    SELECT_DISCOUNT_ITEM("自选特惠", 20, IGameCampaignTypeSelectDiscountItemService.class, IGameCampaignTypeSelectDiscountMessageService.class),
    RECHARGE_RANK("累充排行", 21, IGameCampaignTypeMarryRankService.class, IGameCampaignTypeMarryRankRewardService.class),
    EMAIL_CAMPAIGN("邮件活动", 22, IGameCampaignTypeEmailItemService.class),
    // end
    ;

    private final String name;
    private final int value;
    private final Class<? extends IService> serviceClass;
    private final Class<? extends IService> subServiceClass;

    CampaignType(String name, int value, Class<? extends IService> serviceClass) {
        this(name, value, serviceClass, null);
    }

    CampaignType(String name, int value, Class<? extends IService> serviceClass, Class<? extends IService> subServiceClass) {
        this.name = name;
        this.value = value;
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
            if (e.value == type) {
                return e;
            }
        }
        return null;
    }
}

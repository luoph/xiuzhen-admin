package cn.youai.xiuzhen.game.constant;

import cn.youai.xiuzhen.game.entity.*;
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
    LOGIN("登录礼包", 1, GameCampaignTypeLogin.class, IGameCampaignTypeLoginService.class),
    RECHARGE("累计充值", 2, GameCampaignTypeRecharge.class, IGameCampaignTypeRechargeService.class),
    EXCHANGE("节日兑换", 3, GameCampaignTypeExchange.class, IGameCampaignTypeExchangeService.class),
    TASK("节日任务", 4, GameCampaignTypeTask.class, IGameCampaignTypeTaskService.class),
    BUFF_PRACTICE("修为加成", 5, GameCampaignTypeBuff.class, IGameCampaignTypeBuffService.class),
    BUFF_ANIMA("灵气加成", 6, GameCampaignTypeBuff.class, IGameCampaignTypeBuffService.class),
    FALL("节日掉落", 7, GameCampaignTypeFall.class, IGameCampaignTypeFallService.class),
    FIREWORK("节日烟花", 8, GameCampaignTypeFirework.class, IGameCampaignTypeFireworkService.class),
    REDUCE_RANK("消耗排行", 9, GameCampaignTypeReduce.class, IGameCampaignTypeReduceService.class),
    LIMIT_TIME_SWORD("限时仙剑", 10, GameCampaignTypeSword.class, IGameCampaignTypeSwordService.class),
    THROWING_EGGS("砸蛋", 11, GameCampaignTypeThrowingEggs.class, IGameCampaignTypeThrowingEggsService.class),
    THROWING_EGGS_RANK("砸蛋榜单", 12, GameCampaignTypeThrowingEggsRank.class, IGameCampaignTypeThrowingEggsRankService.class),
    THROWING_EGGS_GIFT("砸蛋礼包", 13, GameCampaignTypeThrowingEggsGift.class, IGameCampaignTypeThrowingEggsGiftService.class),
    PARTY("节日派对", 14, GameCampaignTypePartyTask.class, IGameCampaignTypePartyTaskService.class),
    DIRECT_PURCHASE("直购礼包", 15, GameCampaignDirectPurchase.class, IGameCampaignDirectPurchaseService.class),
    REBATE_RECHARGE("返利狂欢", 16, GameCampaignTypeRebateRecharge.class, IGameCampaignTypeRebateRechargeService.class),
    MARRY_RANK_WINE("赠酒排行榜", 17, GameCampaignTypeMarryRank.class, IGameCampaignTypeMarryRankService.class),
    MARRY_RANK_CHARM("魅力值排行榜", 18, GameCampaignTypeMarryRank.class, IGameCampaignTypeMarryRankService.class),
    //    MARRY_RANK_GIFT("结义排行榜-结义礼包", 19, null, null),
    SELECT_DISCOUNT_ITEM("自选特惠", 20, GameCampaignTypeSelectDiscountItem.class, IGameCampaignTypeSelectDiscountItemService.class),
    RECHARGE_RANK("累充排行", 21, GameCampaignTypeMarryRank.class, IGameCampaignTypeMarryRankService.class),
    EMAIL_CAMPAIGN("邮件活动", 22, GameCampaignTypeEmailItem.class, IGameCampaignTypeEmailItemService.class),
    // end
    ;

    private final String name;
    private final int value;
    private final Class<? extends GameCampaignTypeBase> tableClass;
    private final Class<? extends IService> serviceClass;

    CampaignType(String name, int value, Class<? extends GameCampaignTypeBase> tableClass, Class<? extends IService> serviceClass) {
        this.name = name;
        this.value = value;
        this.tableClass = tableClass;
        this.serviceClass = serviceClass;
    }

    public IService getBean() {
        return SpringContextUtils.getBean(serviceClass);
    }

    public Class<? extends GameCampaignTypeBase> getEntityClass() {
        return getBean().getEntityClass();
    }

    public static CampaignType valueOf(int type) {
        for (CampaignType e : CampaignType.values()) {
            if (e.value == type) {
                return e;
            }
        }
        return null;
    }

    public static CampaignType valueOf(Class<? extends IService> serviceClass) {
        for (CampaignType e : CampaignType.values()) {
            if (e.serviceClass.isAssignableFrom(serviceClass)) {
                return e;
            }
        }
        return null;
    }
}

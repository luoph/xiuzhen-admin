package cn.youai.xiuzhen.game.constant;

import cn.youai.xiuzhen.game.entity.OpenServiceCampaignDetail;
import cn.youai.xiuzhen.game.service.*;
import com.baomidou.mybatisplus.extension.service.IService;
import lombok.Getter;
import org.jeecg.common.util.SpringContextUtils;

/**
 * 开服活动项类型(1.开服排行，2.开服礼包，3.单笔充值，4.寻宝，5.道具消耗)
 *
 * @author luopeihuan
 */
@Getter
@SuppressWarnings("rawtypes")
public enum OpenServiceType {

    /**
     * 活动状态
     */
    RANK("开服排行", 1, IOpenServiceCampaignRankDetailService.class),
    GIFT("开服礼包", 2, IOpenServiceCampaignGiftDetailService.class),
    RECHARGE("单笔充值", 3, IOpenServiceCampaignSingleGiftDetailService.class),
    LOTTERY("寻宝", 4, IOpenServiceCampaignLotteryDetailService.class),
    CONSUME("道具消耗", 5, IOpenServiceCampaignConsumeDetailService.class),
    ;

    private final String name;
    private final int value;
    private final Class<? extends IService> serviceClass;

    OpenServiceType(String name, int value, Class<? extends IService> serviceClass) {
        this.name = name;
        this.value = value;
        this.serviceClass = serviceClass;
    }

    public IService getBean() {
        return SpringContextUtils.getBean(serviceClass);
    }

    public Class<? extends OpenServiceCampaignDetail> getEntityClass() {
        return getBean().getEntityClass();
    }

    public static OpenServiceType valueOfServiceClass(Class<? extends IService> serviceClass) {
        for (OpenServiceType e : OpenServiceType.values()) {
            if (e.serviceClass.isAssignableFrom(serviceClass)) {
                return e;
            }
        }
        return null;
    }

    public static OpenServiceType valueOf(int type) {
        for (OpenServiceType e : OpenServiceType.values()) {
            if (e.getValue() == type) {
                return e;
            }
        }
        return null;
    }
}

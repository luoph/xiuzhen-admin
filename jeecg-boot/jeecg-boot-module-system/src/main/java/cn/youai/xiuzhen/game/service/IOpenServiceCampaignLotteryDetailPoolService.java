package cn.youai.xiuzhen.game.service;

import cn.youai.xiuzhen.game.entity.OpenServiceCampaignLotteryDetailPool;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 开服夺宝奖池
 * @date 2020-12-25
 */
public interface IOpenServiceCampaignLotteryDetailPoolService extends IService<OpenServiceCampaignLotteryDetailPool> {

    void duplicate(OpenServiceCampaignLotteryDetailPool other, long detailId, long typeId, long campaignId);

    void duplicate(List<OpenServiceCampaignLotteryDetailPool> others, long detailId, long typeId, long campaignId);
}

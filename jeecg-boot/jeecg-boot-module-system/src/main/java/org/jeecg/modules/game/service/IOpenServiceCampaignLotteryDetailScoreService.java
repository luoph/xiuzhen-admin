package org.jeecg.modules.game.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.game.entity.OpenServiceCampaignLotteryDetailScore;

import java.util.List;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 开服夺宝积分
 * @date 2020-12-25
 */
public interface IOpenServiceCampaignLotteryDetailScoreService extends IService<OpenServiceCampaignLotteryDetailScore> {

    void duplicate(OpenServiceCampaignLotteryDetailScore other, long detailId, long typeId, long campaignId);

    void duplicate(List<OpenServiceCampaignLotteryDetailScore> others, long detailId, long typeId, long campaignId);
}

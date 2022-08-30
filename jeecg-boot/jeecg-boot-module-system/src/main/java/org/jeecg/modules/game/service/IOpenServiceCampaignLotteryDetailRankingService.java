package org.jeecg.modules.game.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.game.entity.OpenServiceCampaignLotteryDetailRanking;

import java.util.List;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 开服夺宝榜单
 * @date 2020-12-25
 */
public interface IOpenServiceCampaignLotteryDetailRankingService extends IService<OpenServiceCampaignLotteryDetailRanking> {

    void duplicate(OpenServiceCampaignLotteryDetailRanking other, long detailId, long typeId, long campaignId);

    void duplicate(List<OpenServiceCampaignLotteryDetailRanking> others, long detailId, long typeId, long campaignId);
}

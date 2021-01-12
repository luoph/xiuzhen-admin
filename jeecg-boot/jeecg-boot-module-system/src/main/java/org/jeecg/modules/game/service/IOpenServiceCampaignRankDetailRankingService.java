package org.jeecg.modules.game.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.game.entity.OpenServiceCampaignRankDetailMessage;
import org.jeecg.modules.game.entity.OpenServiceCampaignRankDetailRanking;

import java.util.List;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 开服活动-开服排行-活动明细-排行上榜、奖励
 * @date 2020-12-21
 */
public interface IOpenServiceCampaignRankDetailRankingService extends IService<OpenServiceCampaignRankDetailRanking> {

    void duplicate(OpenServiceCampaignRankDetailRanking other, long detailId, long typeId, Long campaignId);

    void duplicate(List<OpenServiceCampaignRankDetailRanking> others, long detailId, long typeId, long campaignId);
}

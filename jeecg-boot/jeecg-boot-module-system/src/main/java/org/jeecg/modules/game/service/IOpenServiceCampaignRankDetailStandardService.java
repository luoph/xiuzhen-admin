package org.jeecg.modules.game.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.game.entity.OpenServiceCampaignRankDetailStandard;

import java.util.List;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 开服活动-开服排行-活动明细-达标奖励
 * @date 2020-12-21
 */
public interface IOpenServiceCampaignRankDetailStandardService extends IService<OpenServiceCampaignRankDetailStandard> {

    void duplicate(OpenServiceCampaignRankDetailStandard other, long detailId, long typeId, long campaignId);

    void duplicate(List<OpenServiceCampaignRankDetailStandard> others, long detailId, long typeId, long campaignId);
}

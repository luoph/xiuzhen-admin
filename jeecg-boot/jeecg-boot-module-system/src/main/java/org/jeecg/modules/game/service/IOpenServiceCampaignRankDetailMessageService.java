package org.jeecg.modules.game.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.game.entity.OpenServiceCampaign;
import org.jeecg.modules.game.entity.OpenServiceCampaignGiftDetail;
import org.jeecg.modules.game.entity.OpenServiceCampaignRankDetailMessage;

import java.util.List;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 开服活动-开服排行-活动明细-传闻
 * @date 2020-12-21
 */
public interface IOpenServiceCampaignRankDetailMessageService extends IService<OpenServiceCampaignRankDetailMessage> {

    void duplicate(OpenServiceCampaignRankDetailMessage other, long detailId, long typeId, long campaignId);

    void duplicate(List<OpenServiceCampaignRankDetailMessage> others, long detailId, long typeId, long campaignId);

}

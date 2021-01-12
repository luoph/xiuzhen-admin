package org.jeecg.modules.game.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.game.entity.OpenServiceCampaignGiftDetailItem;

import java.util.List;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 开服活动-开服开服礼包-礼包明细
 * @date 2020-12-21
 */
public interface IOpenServiceCampaignGiftDetailItemService extends IService<OpenServiceCampaignGiftDetailItem> {

    void duplicate(OpenServiceCampaignGiftDetailItem other, long detailId, long typeId, long campaignId);

    void duplicate(List<OpenServiceCampaignGiftDetailItem> others, long detailId, long typeId, long campaignId);
}

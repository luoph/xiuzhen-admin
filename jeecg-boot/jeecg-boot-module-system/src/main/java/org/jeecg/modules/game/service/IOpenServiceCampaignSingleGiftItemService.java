package org.jeecg.modules.game.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.game.entity.OpenServiceCampaignSingleGiftItem;

import java.util.List;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 开服活动-单比好礼-任务明细
 * @date 2020-12-23
 */
public interface IOpenServiceCampaignSingleGiftItemService extends IService<OpenServiceCampaignSingleGiftItem> {

    void duplicate(OpenServiceCampaignSingleGiftItem other, long detailId, long typeId, long campaignId);

    void duplicate(List<OpenServiceCampaignSingleGiftItem> others, long detailId, long typeId, long campaignId);
}

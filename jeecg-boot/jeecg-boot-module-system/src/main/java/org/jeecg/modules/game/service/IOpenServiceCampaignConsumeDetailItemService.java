package org.jeecg.modules.game.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.game.entity.OpenServiceCampaignConsumeDetailItem;

import java.util.List;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 开服活动消耗道具
 * @date 2020-12-28
 */
public interface IOpenServiceCampaignConsumeDetailItemService extends IService<OpenServiceCampaignConsumeDetailItem> {

    void duplicate(OpenServiceCampaignConsumeDetailItem other, long detailId, long typeId, long campaignId);

    void duplicate(List<OpenServiceCampaignConsumeDetailItem> others, long detailId, long typeId, long campaignId);
}

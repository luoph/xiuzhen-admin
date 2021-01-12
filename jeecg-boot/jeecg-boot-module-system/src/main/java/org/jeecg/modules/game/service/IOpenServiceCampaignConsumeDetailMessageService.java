package org.jeecg.modules.game.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.game.entity.OpenServiceCampaignConsumeDetailMessage;

import java.util.List;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 开服活动消耗传闻
 * @date 2020-12-28
 */
public interface IOpenServiceCampaignConsumeDetailMessageService extends IService<OpenServiceCampaignConsumeDetailMessage> {

    void duplicate(OpenServiceCampaignConsumeDetailMessage other, long detailId, long typeId, long campaignId);

    void duplicate(List<OpenServiceCampaignConsumeDetailMessage> others, long detailId, long typeId, long campaignId);
}

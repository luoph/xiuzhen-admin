package org.jeecg.modules.game.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.game.entity.OpenServiceCampaignGiftDetail;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 开服活动-开服排行-活动明细(3级)
 * @date 2020-12-21
 */
public interface IOpenServiceCampaignGiftDetailService extends IService<OpenServiceCampaignGiftDetail> {

    void fillDetail(OpenServiceCampaignGiftDetail detail);

    void duplicate(OpenServiceCampaignGiftDetail entity, Long id, Long campaignId);
}

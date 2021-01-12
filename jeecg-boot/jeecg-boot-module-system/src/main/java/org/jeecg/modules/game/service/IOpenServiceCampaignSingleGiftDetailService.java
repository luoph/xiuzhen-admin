package org.jeecg.modules.game.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.game.entity.OpenServiceCampaignSingleGiftDetail;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 开服活动-单笔好礼活动参数
 * @date 2020-12-23
 */
public interface IOpenServiceCampaignSingleGiftDetailService extends IService<OpenServiceCampaignSingleGiftDetail> {

    void fillDetail(OpenServiceCampaignSingleGiftDetail detail);

    void duplicate(OpenServiceCampaignSingleGiftDetail entity, Long id, Long campaignId);
}

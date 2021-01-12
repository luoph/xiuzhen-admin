package org.jeecg.modules.game.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.game.entity.OpenServiceCampaignConsumeDetailMessage;
import org.jeecg.modules.game.entity.OpenServiceCampaignGiftDetailItem;
import org.jeecg.modules.game.mapper.GameOpenServiceCampaignGiftDetailItemMapper;
import org.jeecg.modules.game.service.IOpenServiceCampaignGiftDetailItemService;
import org.springframework.stereotype.Service;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 开服活动-开服开服礼包-礼包明细
 * @date 2020-12-21
 */
@Service
public class OpenServiceCampaignGiftDetailItemServiceImpl extends ServiceImpl<GameOpenServiceCampaignGiftDetailItemMapper, OpenServiceCampaignGiftDetailItem> implements IOpenServiceCampaignGiftDetailItemService {

    @Override
    public void duplicate(OpenServiceCampaignGiftDetailItem other, long detailId, long typeId, long campaignId) {
        OpenServiceCampaignGiftDetailItem copy = new OpenServiceCampaignGiftDetailItem(other);
        copy.setGiftDetailId(detailId);
        copy.setCampaignTypeId(typeId);
        copy.setCampaignId(campaignId);
        save(copy);
    }
}

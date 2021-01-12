package org.jeecg.modules.game.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.game.entity.OpenServiceCampaignGiftDetail;
import org.jeecg.modules.game.entity.OpenServiceCampaignGiftDetailItem;
import org.jeecg.modules.game.mapper.GameOpenServiceCampaignGiftDetailMapper;
import org.jeecg.modules.game.service.IOpenServiceCampaignGiftDetailItemService;
import org.jeecg.modules.game.service.IOpenServiceCampaignGiftDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 开服活动-开服排行-活动明细(3级)
 * @date 2020-12-21
 */
@Service
public class GameOpenServiceCampaignGiftDetailServiceImpl extends ServiceImpl<GameOpenServiceCampaignGiftDetailMapper, OpenServiceCampaignGiftDetail> implements IOpenServiceCampaignGiftDetailService {

    @Autowired
    private IOpenServiceCampaignGiftDetailItemService giftDetailItemService;

    @Override
    public void fillDetail(OpenServiceCampaignGiftDetail detail) {
        Wrapper<OpenServiceCampaignGiftDetailItem> query = Wrappers.<OpenServiceCampaignGiftDetailItem>lambdaQuery()
                .eq(OpenServiceCampaignGiftDetailItem::getCampaignId, detail.getCampaignId())
                .eq(OpenServiceCampaignGiftDetailItem::getCampaignTypeId, detail.getCampaignTypeId())
                .eq(OpenServiceCampaignGiftDetailItem::getGiftDetailId, detail.getId())
                .orderByAsc(OpenServiceCampaignGiftDetailItem::getSort);
        detail.setDetailItemList(giftDetailItemService.list(query));
    }

    @Override
    public void duplicate(OpenServiceCampaignGiftDetail entity, Long id, Long campaignId) {

    }
}

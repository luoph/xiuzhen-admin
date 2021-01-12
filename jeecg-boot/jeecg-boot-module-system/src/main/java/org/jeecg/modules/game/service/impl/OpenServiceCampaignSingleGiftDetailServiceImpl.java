package org.jeecg.modules.game.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.game.entity.OpenServiceCampaignSingleGiftDetail;
import org.jeecg.modules.game.entity.OpenServiceCampaignSingleGiftItem;
import org.jeecg.modules.game.entity.OpenServiceCampaignSingleGiftNotice;
import org.jeecg.modules.game.mapper.OpenServiceCampaignSingleGiftDetailMapper;
import org.jeecg.modules.game.service.IOpenServiceCampaignSingleGiftDetailService;
import org.jeecg.modules.game.service.IOpenServiceCampaignSingleGiftItemService;
import org.jeecg.modules.game.service.IOpenServiceCampaignSingleGiftNoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 开服活动-单笔好礼活动参数
 * @date 2020-12-23
 */
@Service
public class OpenServiceCampaignSingleGiftDetailServiceImpl extends ServiceImpl<OpenServiceCampaignSingleGiftDetailMapper, OpenServiceCampaignSingleGiftDetail> implements IOpenServiceCampaignSingleGiftDetailService {

    @Autowired
    private IOpenServiceCampaignSingleGiftItemService singleGiftItemService;
    @Autowired
    private IOpenServiceCampaignSingleGiftNoticeService singleGiftNoticeService;

    @Override
    public void duplicate(OpenServiceCampaignSingleGiftDetail other, long typeId, long campaignId) {
        OpenServiceCampaignSingleGiftDetail copy = new OpenServiceCampaignSingleGiftDetail(other);
        copy.setCampaignTypeId(typeId);
        copy.setCampaignId(campaignId);
        save(copy);

        if (CollUtil.isNotEmpty(other.getDetailItemList())) {
            singleGiftItemService.duplicate(other.getDetailItemList(), copy.getId(), typeId, campaignId);
        }

        if (CollUtil.isNotEmpty(other.getMessageList())) {
            singleGiftNoticeService.duplicate(other.getMessageList(), copy.getId(), typeId, campaignId);
        }
    }

    @Override
    public void fillDetail(OpenServiceCampaignSingleGiftDetail detail) {
        Wrapper<OpenServiceCampaignSingleGiftItem> itemQuery = Wrappers.<OpenServiceCampaignSingleGiftItem>lambdaQuery()
                .eq(OpenServiceCampaignSingleGiftItem::getCampaignId, detail.getCampaignId())
                .eq(OpenServiceCampaignSingleGiftItem::getCampaignTypeId, detail.getCampaignTypeId())
                .eq(OpenServiceCampaignSingleGiftItem::getGiftDetailId, detail.getId());
        detail.setDetailItemList(singleGiftItemService.list(itemQuery));

        Wrapper<OpenServiceCampaignSingleGiftNotice> noticeQuery = Wrappers.<OpenServiceCampaignSingleGiftNotice>lambdaQuery()
                .eq(OpenServiceCampaignSingleGiftNotice::getCampaignId, detail.getCampaignId())
                .eq(OpenServiceCampaignSingleGiftNotice::getCampaignTypeId, detail.getCampaignTypeId())
                .eq(OpenServiceCampaignSingleGiftNotice::getGiftDetailId, detail.getId());
        detail.setMessageList(singleGiftNoticeService.list(noticeQuery));
    }


}

package org.jeecg.modules.game.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.game.entity.OpenServiceCampaignSingleGiftNotice;
import org.jeecg.modules.game.mapper.OpenServiceCampaignSingleGiftNoticeMapper;
import org.jeecg.modules.game.service.IOpenServiceCampaignSingleGiftNoticeService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 开服活动-单比好礼-传闻消息
 * @date 2020-12-23
 */
@Service
public class OpenServiceCampaignSingleGiftNoticeServiceImpl extends ServiceImpl<OpenServiceCampaignSingleGiftNoticeMapper, OpenServiceCampaignSingleGiftNotice> implements IOpenServiceCampaignSingleGiftNoticeService {

    @Override
    public void duplicate(OpenServiceCampaignSingleGiftNotice other, long detailId, long typeId, long campaignId) {
        OpenServiceCampaignSingleGiftNotice copy = new OpenServiceCampaignSingleGiftNotice(other);
        copy.setGiftDetailId(detailId);
        copy.setCampaignTypeId(typeId);
        copy.setCampaignId(campaignId);
        save(copy);
    }

    @Override
    public void duplicate(List<OpenServiceCampaignSingleGiftNotice> others, long detailId, long typeId, long campaignId) {
        List<OpenServiceCampaignSingleGiftNotice> addList = new ArrayList<>();
        for (OpenServiceCampaignSingleGiftNotice other : others) {
            OpenServiceCampaignSingleGiftNotice copy = new OpenServiceCampaignSingleGiftNotice(other);
            copy.setGiftDetailId(detailId);
            copy.setCampaignTypeId(typeId);
            copy.setCampaignId(campaignId);
            addList.add(copy);
        }

        if (CollUtil.isNotEmpty(addList)) {
            saveBatch(addList);
        }
    }
}

package org.jeecg.modules.game.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.game.entity.OpenServiceCampaignGiftDetailItem;
import org.jeecg.modules.game.mapper.GameOpenServiceCampaignGiftDetailItemMapper;
import org.jeecg.modules.game.service.IOpenServiceCampaignGiftDetailItemService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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

    @Override
    public void duplicate(List<OpenServiceCampaignGiftDetailItem> others, long detailId, long typeId, long campaignId) {
        List<OpenServiceCampaignGiftDetailItem> addList = new ArrayList<>();
        for (OpenServiceCampaignGiftDetailItem other : others) {
            OpenServiceCampaignGiftDetailItem copy = new OpenServiceCampaignGiftDetailItem(other);
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

package org.jeecg.modules.game.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.game.entity.OpenServiceCampaignConsumeDetailItem;
import org.jeecg.modules.game.mapper.OpenServiceCampaignConsumeDetailItemMapper;
import org.jeecg.modules.game.service.IOpenServiceCampaignConsumeDetailItemService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 开服活动消耗道具
 * @date 2020-12-28
 */
@Service
public class OpenServiceCampaignConsumeDetailItemServiceImpl extends ServiceImpl<OpenServiceCampaignConsumeDetailItemMapper, OpenServiceCampaignConsumeDetailItem> implements IOpenServiceCampaignConsumeDetailItemService {

    @Override
    public void duplicate(OpenServiceCampaignConsumeDetailItem other, long detailId, long typeId, long campaignId) {
        OpenServiceCampaignConsumeDetailItem copy = new OpenServiceCampaignConsumeDetailItem(other);
        copy.setConsumeDetailId(detailId);
        copy.setCampaignTypeId(typeId);
        copy.setCampaignId(campaignId);
        save(copy);
    }

    @Override
    public void duplicate(List<OpenServiceCampaignConsumeDetailItem> others, long detailId, long typeId, long campaignId) {
        List<OpenServiceCampaignConsumeDetailItem> addList = new ArrayList<>();
        for (OpenServiceCampaignConsumeDetailItem other : others) {
            OpenServiceCampaignConsumeDetailItem copy = new OpenServiceCampaignConsumeDetailItem(other);
            copy.setConsumeDetailId(detailId);
            copy.setCampaignTypeId(typeId);
            copy.setCampaignId(campaignId);
            addList.add(copy);
        }

        if (CollUtil.isNotEmpty(addList)) {
            saveBatch(addList);
        }
    }
}

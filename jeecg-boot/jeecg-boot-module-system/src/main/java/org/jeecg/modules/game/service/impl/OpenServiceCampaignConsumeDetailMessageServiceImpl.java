package org.jeecg.modules.game.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.game.entity.OpenServiceCampaignConsumeDetailMessage;
import org.jeecg.modules.game.mapper.OpenServiceCampaignConsumeDetailMessageMapper;
import org.jeecg.modules.game.service.IOpenServiceCampaignConsumeDetailMessageService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 开服活动消耗传闻
 * @date 2020-12-28
 */
@Service
public class OpenServiceCampaignConsumeDetailMessageServiceImpl extends ServiceImpl<OpenServiceCampaignConsumeDetailMessageMapper, OpenServiceCampaignConsumeDetailMessage> implements IOpenServiceCampaignConsumeDetailMessageService {

    @Override
    public void duplicate(OpenServiceCampaignConsumeDetailMessage other, long detailId, long typeId, long campaignId) {
        OpenServiceCampaignConsumeDetailMessage copy = new OpenServiceCampaignConsumeDetailMessage(other);
        copy.setConsumeDetailId(detailId);
        copy.setCampaignTypeId(typeId);
        copy.setCampaignId(campaignId);
        save(copy);
    }

    @Override
    public void duplicate(List<OpenServiceCampaignConsumeDetailMessage> others, long detailId, long typeId, long campaignId) {
        List<OpenServiceCampaignConsumeDetailMessage> addList = new ArrayList<>();
        for (OpenServiceCampaignConsumeDetailMessage other : others) {
            OpenServiceCampaignConsumeDetailMessage copy = new OpenServiceCampaignConsumeDetailMessage(other);
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

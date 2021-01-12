package org.jeecg.modules.game.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.game.entity.OpenServiceCampaignConsumeDetail;
import org.jeecg.modules.game.entity.OpenServiceCampaignConsumeDetailItem;
import org.jeecg.modules.game.entity.OpenServiceCampaignConsumeDetailMessage;
import org.jeecg.modules.game.mapper.OpenServiceCampaignConsumeDetailMapper;
import org.jeecg.modules.game.service.IOpenServiceCampaignConsumeDetailItemService;
import org.jeecg.modules.game.service.IOpenServiceCampaignConsumeDetailMessageService;
import org.jeecg.modules.game.service.IOpenServiceCampaignConsumeDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 开服互动消耗配置
 * @date 2020-12-28
 */
@Service
public class OpenServiceCampaignConsumeDetailServiceImpl extends ServiceImpl<OpenServiceCampaignConsumeDetailMapper, OpenServiceCampaignConsumeDetail> implements IOpenServiceCampaignConsumeDetailService {

    @Autowired
    private IOpenServiceCampaignConsumeDetailItemService detailItemService;
    @Autowired
    private IOpenServiceCampaignConsumeDetailMessageService detailMessageService;

    @Override
    public void fillDetail(OpenServiceCampaignConsumeDetail detail) {
        detail.setConsumeList(getOpenServiceCampaignConsumeDetailItemList(detail));
        detail.setMessageList(getOpenServiceCampaignConsumeDetailMessageList(detail));
    }

    @Override
    public void duplicate(OpenServiceCampaignConsumeDetail entity, Long id, Long campaignId) {

    }

    private List<OpenServiceCampaignConsumeDetailItem> getOpenServiceCampaignConsumeDetailItemList(OpenServiceCampaignConsumeDetail detail) {
        Wrapper<OpenServiceCampaignConsumeDetailItem> query = Wrappers.<OpenServiceCampaignConsumeDetailItem>lambdaQuery()
                .eq(OpenServiceCampaignConsumeDetailItem::getCampaignId, detail.getCampaignId())
                .eq(OpenServiceCampaignConsumeDetailItem::getCampaignTypeId, detail.getCampaignTypeId())
                .eq(OpenServiceCampaignConsumeDetailItem::getConsumeDetailId, detail.getId())
                .orderByAsc(OpenServiceCampaignConsumeDetailItem::getSort);
        return detailItemService.list(query);
    }

    private List<OpenServiceCampaignConsumeDetailMessage> getOpenServiceCampaignConsumeDetailMessageList(OpenServiceCampaignConsumeDetail detail) {
        Wrapper<OpenServiceCampaignConsumeDetailMessage> query = Wrappers.<OpenServiceCampaignConsumeDetailMessage>lambdaQuery()
                .eq(OpenServiceCampaignConsumeDetailMessage::getCampaignId, detail.getCampaignId())
                .eq(OpenServiceCampaignConsumeDetailMessage::getCampaignTypeId, detail.getCampaignTypeId())
                .eq(OpenServiceCampaignConsumeDetailMessage::getConsumeDetailId, detail.getId());
        return detailMessageService.list(query);
    }
}

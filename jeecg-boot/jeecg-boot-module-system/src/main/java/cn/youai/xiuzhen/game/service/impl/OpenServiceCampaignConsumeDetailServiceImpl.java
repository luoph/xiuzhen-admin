package cn.youai.xiuzhen.game.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.youai.xiuzhen.game.entity.OpenServiceCampaignConsumeDetail;
import cn.youai.xiuzhen.game.entity.OpenServiceCampaignConsumeDetailItem;
import cn.youai.xiuzhen.game.entity.OpenServiceCampaignConsumeDetailMessage;
import cn.youai.xiuzhen.game.mapper.OpenServiceCampaignConsumeDetailMapper;
import cn.youai.xiuzhen.game.service.IOpenServiceCampaignConsumeDetailItemService;
import cn.youai.xiuzhen.game.service.IOpenServiceCampaignConsumeDetailMessageService;
import cn.youai.xiuzhen.game.service.IOpenServiceCampaignConsumeDetailService;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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
@DS("master")
public class OpenServiceCampaignConsumeDetailServiceImpl extends ServiceImpl<OpenServiceCampaignConsumeDetailMapper, OpenServiceCampaignConsumeDetail> implements IOpenServiceCampaignConsumeDetailService {

    @Autowired
    private IOpenServiceCampaignConsumeDetailItemService detailItemService;
    @Autowired
    private IOpenServiceCampaignConsumeDetailMessageService detailMessageService;

    @Override
    public void duplicate(OpenServiceCampaignConsumeDetail other, long typeId, long campaignId) {
        OpenServiceCampaignConsumeDetail copy = new OpenServiceCampaignConsumeDetail(other);
        copy.setCampaignTypeId(typeId);
        copy.setCampaignId(campaignId);
        save(copy);

        if (CollUtil.isNotEmpty(other.getConsumeList())) {
            detailItemService.duplicate(other.getConsumeList(), copy.getId(), typeId, campaignId);
        }

        if (CollUtil.isNotEmpty(other.getMessageList())) {
            detailMessageService.duplicate(other.getMessageList(), copy.getId(), typeId, campaignId);
        }
    }

    @Override
    public void fillDetail(OpenServiceCampaignConsumeDetail detail) {
        detail.setConsumeList(getOpenServiceCampaignConsumeDetailItemList(detail));
        detail.setMessageList(getOpenServiceCampaignConsumeDetailMessageList(detail));
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

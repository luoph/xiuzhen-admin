package cn.youai.xiuzhen.game.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.youai.xiuzhen.game.entity.OpenServiceCampaignGiftDetail;
import cn.youai.xiuzhen.game.entity.OpenServiceCampaignGiftDetailItem;
import cn.youai.xiuzhen.game.mapper.GameOpenServiceCampaignGiftDetailMapper;
import cn.youai.xiuzhen.game.service.IOpenServiceCampaignGiftDetailItemService;
import cn.youai.xiuzhen.game.service.IOpenServiceCampaignGiftDetailService;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 开服活动-开服排行-活动明细(3级)
 * @date 2020-12-21
 */
@Service
@DS("master")
public class OpenServiceCampaignGiftDetailServiceImpl extends ServiceImpl<GameOpenServiceCampaignGiftDetailMapper, OpenServiceCampaignGiftDetail> implements IOpenServiceCampaignGiftDetailService {

    @Autowired
    private IOpenServiceCampaignGiftDetailItemService giftDetailItemService;

    @Override
    public void duplicate(OpenServiceCampaignGiftDetail other, long typeId, long campaignId) {
        OpenServiceCampaignGiftDetail copy = new OpenServiceCampaignGiftDetail(other);
        copy.setCampaignTypeId(typeId);
        copy.setCampaignId(campaignId);
        save(copy);

        if (CollUtil.isNotEmpty(other.getDetailItemList())) {
            giftDetailItemService.duplicate(other.getDetailItemList(), copy.getId(), typeId, campaignId);
        }
    }

    @Override
    public void fillDetail(OpenServiceCampaignGiftDetail detail) {
        Wrapper<OpenServiceCampaignGiftDetailItem> query = Wrappers.<OpenServiceCampaignGiftDetailItem>lambdaQuery()
                .eq(OpenServiceCampaignGiftDetailItem::getCampaignId, detail.getCampaignId())
                .eq(OpenServiceCampaignGiftDetailItem::getCampaignTypeId, detail.getCampaignTypeId())
                .eq(OpenServiceCampaignGiftDetailItem::getGiftDetailId, detail.getId())
                .orderByAsc(OpenServiceCampaignGiftDetailItem::getSort);
        detail.setDetailItemList(giftDetailItemService.list(query));
    }

}

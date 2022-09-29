package cn.youai.xiuzhen.game.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.youai.xiuzhen.game.entity.OpenServiceCampaignSingleGiftItem;
import cn.youai.xiuzhen.game.mapper.OpenServiceCampaignSingleGiftItemMapper;
import cn.youai.xiuzhen.game.service.IOpenServiceCampaignSingleGiftItemService;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 开服活动-单笔好礼-任务明细
 * @date 2020-12-23
 */
@Service
@DS("master")
public class OpenServiceCampaignSingleGiftItemServiceImpl extends ServiceImpl<OpenServiceCampaignSingleGiftItemMapper, OpenServiceCampaignSingleGiftItem> implements IOpenServiceCampaignSingleGiftItemService {

    @Override
    public void duplicate(OpenServiceCampaignSingleGiftItem other, long detailId, long typeId, long campaignId) {
        OpenServiceCampaignSingleGiftItem copy = new OpenServiceCampaignSingleGiftItem(other);
        copy.setGiftDetailId(detailId);
        copy.setCampaignTypeId(typeId);
        copy.setCampaignId(campaignId);
        save(copy);
    }

    @Override
    public void duplicate(List<OpenServiceCampaignSingleGiftItem> others, long detailId, long typeId, long campaignId) {
        List<OpenServiceCampaignSingleGiftItem> addList = new ArrayList<>();
        for (OpenServiceCampaignSingleGiftItem other : others) {
            OpenServiceCampaignSingleGiftItem copy = new OpenServiceCampaignSingleGiftItem(other);
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

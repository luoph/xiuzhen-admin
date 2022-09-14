package cn.youai.xiuzhen.game.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.youai.xiuzhen.game.entity.OpenServiceCampaignConsumeDetailItem;
import cn.youai.xiuzhen.game.mapper.OpenServiceCampaignConsumeDetailItemMapper;
import cn.youai.xiuzhen.game.service.IOpenServiceCampaignConsumeDetailItemService;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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
@DS("master")
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

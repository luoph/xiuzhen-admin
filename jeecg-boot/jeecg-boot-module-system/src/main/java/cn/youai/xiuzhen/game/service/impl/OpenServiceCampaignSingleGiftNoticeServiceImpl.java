package cn.youai.xiuzhen.game.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.youai.xiuzhen.game.entity.OpenServiceCampaignSingleGiftNotice;
import cn.youai.xiuzhen.game.mapper.OpenServiceCampaignSingleGiftNoticeMapper;
import cn.youai.xiuzhen.game.service.IOpenServiceCampaignSingleGiftNoticeService;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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
@DS("master")
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

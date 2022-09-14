package cn.youai.xiuzhen.game.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.youai.xiuzhen.game.entity.OpenServiceCampaignRankDetailMessage;
import cn.youai.xiuzhen.game.mapper.GameOpenServiceCampaignRankDetailMessageMapper;
import cn.youai.xiuzhen.game.service.IOpenServiceCampaignRankDetailMessageService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 开服活动-开服排行-活动明细-传闻
 * @date 2020-12-21
 */
@Service
public class OpenServiceCampaignRankDetailMessageServiceImpl extends ServiceImpl<GameOpenServiceCampaignRankDetailMessageMapper, OpenServiceCampaignRankDetailMessage> implements IOpenServiceCampaignRankDetailMessageService {

    @Override
    public void duplicate(OpenServiceCampaignRankDetailMessage other, long detailId, long typeId, long campaignId) {
        OpenServiceCampaignRankDetailMessage copy = new OpenServiceCampaignRankDetailMessage(other);
        copy.setRankDetailId(detailId);
        copy.setCampaignTypeId(typeId);
        copy.setCampaignId(campaignId);
        save(copy);
    }

    @Override
    public void duplicate(List<OpenServiceCampaignRankDetailMessage> others, long detailId, long typeId, long campaignId) {
        List<OpenServiceCampaignRankDetailMessage> addList = new ArrayList<>();
        for (OpenServiceCampaignRankDetailMessage other : others) {
            OpenServiceCampaignRankDetailMessage copy = new OpenServiceCampaignRankDetailMessage(other);
            copy.setRankDetailId(detailId);
            copy.setCampaignTypeId(typeId);
            copy.setCampaignId(campaignId);
            addList.add(copy);
        }
        if (CollUtil.isNotEmpty(addList)) {
            saveBatch(addList);
        }
    }
}

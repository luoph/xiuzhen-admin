package org.jeecg.modules.game.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.game.entity.OpenServiceCampaignRankDetailScore;
import org.jeecg.modules.game.mapper.GameOpenServiceCampaignRankDetailScoreMapper;
import org.jeecg.modules.game.service.IOpenServiceCampaignRankDetailScoreService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 开服活动-开服排行-活动明细-消耗道具分数
 * @date 2020-12-21
 */
@Service
public class OpenServiceCampaignRankDetailScoreServiceImpl extends ServiceImpl<GameOpenServiceCampaignRankDetailScoreMapper, OpenServiceCampaignRankDetailScore> implements IOpenServiceCampaignRankDetailScoreService {

    @Override
    public void duplicate(OpenServiceCampaignRankDetailScore other, long detailId, long typeId, Long campaignId) {
        OpenServiceCampaignRankDetailScore copy = new OpenServiceCampaignRankDetailScore(other);
        copy.setRankDetailId(detailId);
        copy.setCampaignTypeId(typeId);
        copy.setCampaignId(campaignId);
        save(copy);
    }

    @Override
    public void duplicate(List<OpenServiceCampaignRankDetailScore> others, long detailId, long typeId, long campaignId) {
        List<OpenServiceCampaignRankDetailScore> addList = new ArrayList<>();
        for (OpenServiceCampaignRankDetailScore other : others) {
            OpenServiceCampaignRankDetailScore copy = new OpenServiceCampaignRankDetailScore(other);
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

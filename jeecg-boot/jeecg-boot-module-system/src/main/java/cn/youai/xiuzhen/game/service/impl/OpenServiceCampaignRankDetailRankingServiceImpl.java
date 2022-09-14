package cn.youai.xiuzhen.game.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.youai.xiuzhen.game.entity.OpenServiceCampaignRankDetailRanking;
import cn.youai.xiuzhen.game.mapper.GameOpenServiceCampaignRankDetailRankingMapper;
import cn.youai.xiuzhen.game.service.IOpenServiceCampaignRankDetailRankingService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 开服活动-开服排行-活动明细-排行上榜、奖励
 * @date 2020-12-21
 */
@Service
public class OpenServiceCampaignRankDetailRankingServiceImpl extends ServiceImpl<GameOpenServiceCampaignRankDetailRankingMapper, OpenServiceCampaignRankDetailRanking> implements IOpenServiceCampaignRankDetailRankingService {

    @Override
    public void duplicate(OpenServiceCampaignRankDetailRanking other, long detailId, long typeId, Long campaignId) {
        OpenServiceCampaignRankDetailRanking copy = new OpenServiceCampaignRankDetailRanking(other);
        copy.setRankDetailId(detailId);
        copy.setCampaignTypeId(typeId);
        copy.setCampaignId(campaignId);
        save(copy);
    }

    @Override
    public void duplicate(List<OpenServiceCampaignRankDetailRanking> others, long detailId, long typeId, long campaignId) {
        List<OpenServiceCampaignRankDetailRanking> addList = new ArrayList<>();
        for (OpenServiceCampaignRankDetailRanking other : others) {
            OpenServiceCampaignRankDetailRanking copy = new OpenServiceCampaignRankDetailRanking(other);
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

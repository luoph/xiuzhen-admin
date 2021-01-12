package org.jeecg.modules.game.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.game.entity.OpenServiceCampaignLotteryDetail;
import org.jeecg.modules.game.entity.OpenServiceCampaignLotteryDetailPool;
import org.jeecg.modules.game.entity.OpenServiceCampaignLotteryDetailRanking;
import org.jeecg.modules.game.entity.OpenServiceCampaignLotteryDetailScore;
import org.jeecg.modules.game.mapper.OpenServiceCampaignLotteryDetailMapper;
import org.jeecg.modules.game.service.IOpenServiceCampaignLotteryDetailPoolService;
import org.jeecg.modules.game.service.IOpenServiceCampaignLotteryDetailRankingService;
import org.jeecg.modules.game.service.IOpenServiceCampaignLotteryDetailScoreService;
import org.jeecg.modules.game.service.IOpenServiceCampaignLotteryDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 开服夺宝详情
 * @date 2020-12-25
 */
@Service
public class OpenServiceCampaignLotteryDetailServiceImpl extends ServiceImpl<OpenServiceCampaignLotteryDetailMapper, OpenServiceCampaignLotteryDetail> implements IOpenServiceCampaignLotteryDetailService {

    @Autowired
    private IOpenServiceCampaignLotteryDetailPoolService detailPoolService;
    @Autowired
    private IOpenServiceCampaignLotteryDetailScoreService detailScoreService;
    @Autowired
    private IOpenServiceCampaignLotteryDetailRankingService detailRankingService;

    @Override
    public void fillDetail(OpenServiceCampaignLotteryDetail detail) {
        detail.setPoolList(getOpenServiceCampaignLotteryDetailPoolList(detail));
        detail.setScoreList(getOpenServiceCampaignLotteryDetailScoreList(detail));
        detail.setRankingList(getOpenServiceCampaignLotteryDetailRankingList(detail));
    }

    @Override
    public void duplicate(OpenServiceCampaignLotteryDetail entity, Long id, Long campaignId) {

    }

    private List<OpenServiceCampaignLotteryDetailPool> getOpenServiceCampaignLotteryDetailPoolList(OpenServiceCampaignLotteryDetail detail) {
        Wrapper<OpenServiceCampaignLotteryDetailPool> query = Wrappers.<OpenServiceCampaignLotteryDetailPool>lambdaQuery()
                .eq(OpenServiceCampaignLotteryDetailPool::getCampaignId, detail.getCampaignId())
                .eq(OpenServiceCampaignLotteryDetailPool::getCampaignTypeId, detail.getCampaignTypeId())
                .eq(OpenServiceCampaignLotteryDetailPool::getLotteryDetailId, detail.getId());
        return detailPoolService.list(query);
    }

    private List<OpenServiceCampaignLotteryDetailRanking> getOpenServiceCampaignLotteryDetailRankingList(OpenServiceCampaignLotteryDetail detail) {
        Wrapper<OpenServiceCampaignLotteryDetailRanking> query = Wrappers.<OpenServiceCampaignLotteryDetailRanking>lambdaQuery()
                .eq(OpenServiceCampaignLotteryDetailRanking::getCampaignId, detail.getCampaignId())
                .eq(OpenServiceCampaignLotteryDetailRanking::getCampaignTypeId, detail.getCampaignTypeId())
                .eq(OpenServiceCampaignLotteryDetailRanking::getLotteryDetailId, detail.getId());
        return detailRankingService.list(query);
    }

    private List<OpenServiceCampaignLotteryDetailScore> getOpenServiceCampaignLotteryDetailScoreList(OpenServiceCampaignLotteryDetail detail) {
        Wrapper<OpenServiceCampaignLotteryDetailScore> query = Wrappers.<OpenServiceCampaignLotteryDetailScore>lambdaQuery()
                .eq(OpenServiceCampaignLotteryDetailScore::getCampaignId, detail.getCampaignId())
                .eq(OpenServiceCampaignLotteryDetailScore::getCampaignTypeId, detail.getCampaignTypeId())
                .eq(OpenServiceCampaignLotteryDetailScore::getLotteryDetailId, detail.getId());
        return detailScoreService.list(query);
    }
}

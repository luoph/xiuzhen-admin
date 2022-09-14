package cn.youai.xiuzhen.game.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.youai.xiuzhen.game.entity.OpenServiceCampaignLotteryDetail;
import cn.youai.xiuzhen.game.entity.OpenServiceCampaignLotteryDetailPool;
import cn.youai.xiuzhen.game.entity.OpenServiceCampaignLotteryDetailRanking;
import cn.youai.xiuzhen.game.entity.OpenServiceCampaignLotteryDetailScore;
import cn.youai.xiuzhen.game.mapper.OpenServiceCampaignLotteryDetailMapper;
import cn.youai.xiuzhen.game.service.IOpenServiceCampaignLotteryDetailPoolService;
import cn.youai.xiuzhen.game.service.IOpenServiceCampaignLotteryDetailRankingService;
import cn.youai.xiuzhen.game.service.IOpenServiceCampaignLotteryDetailScoreService;
import cn.youai.xiuzhen.game.service.IOpenServiceCampaignLotteryDetailService;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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
    public void duplicate(OpenServiceCampaignLotteryDetail other, long typeId, long campaignId) {
        OpenServiceCampaignLotteryDetail copy = new OpenServiceCampaignLotteryDetail(other);
        copy.setCampaignTypeId(typeId);
        copy.setCampaignId(campaignId);
        save(copy);

        if (CollUtil.isNotEmpty(other.getPoolList())) {
            detailPoolService.duplicate(other.getPoolList(), copy.getId(), typeId, campaignId);
        }

        if (CollUtil.isNotEmpty(other.getRankingList())) {
            detailRankingService.duplicate(other.getRankingList(), copy.getId(), typeId, campaignId);
        }

        if (CollUtil.isNotEmpty(other.getScoreList())) {
            detailScoreService.duplicate(other.getScoreList(), copy.getId(), typeId, campaignId);
        }
    }

    @Override
    public void fillDetail(OpenServiceCampaignLotteryDetail detail) {
        detail.setPoolList(getOpenServiceCampaignLotteryDetailPoolList(detail));
        detail.setScoreList(getOpenServiceCampaignLotteryDetailScoreList(detail));
        detail.setRankingList(getOpenServiceCampaignLotteryDetailRankingList(detail));
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

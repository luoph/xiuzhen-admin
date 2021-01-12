package org.jeecg.modules.game.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.game.entity.*;
import org.jeecg.modules.game.mapper.GameOpenServiceCampaignRankDetailMapper;
import org.jeecg.modules.game.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 开服活动-开服排行-活动明细(3级)
 * @date 2020-12-21
 */
@Service
public class OpenServiceCampaignRankDetailServiceImpl extends ServiceImpl<GameOpenServiceCampaignRankDetailMapper, OpenServiceCampaignRankDetail> implements IOpenServiceCampaignRankDetailService {

    @Autowired
    private IOpenServiceCampaignRankDetailMessageService detailMessageService;
    @Autowired
    private IOpenServiceCampaignRankDetailRankingService detailRankingService;
    @Autowired
    private IOpenServiceCampaignRankDetailScoreService detailScoreService;
    @Autowired
    private IOpenServiceCampaignRankDetailStandardService detailStandardService;

    @Override
    public void duplicate(OpenServiceCampaignRankDetail other, long typeId, long campaignId) {
        OpenServiceCampaignRankDetail copy = new OpenServiceCampaignRankDetail(other);
        save(copy);
        copy.setCampaignTypeId(typeId);
        copy.setCampaignId(campaignId);

        if (CollUtil.isNotEmpty(other.getMessageList())) {
            for (OpenServiceCampaignRankDetailMessage entity : other.getMessageList()) {
            }
        }

        if (CollUtil.isNotEmpty(other.getRankingList())) {
            for (OpenServiceCampaignRankDetailRanking entity : other.getRankingList()) {
            }
        }

        if (CollUtil.isNotEmpty(other.getScoreList())) {
            for (OpenServiceCampaignRankDetailScore entity : other.getScoreList()) {
            }
        }

        if (CollUtil.isNotEmpty(other.getStandardList())) {
            for (OpenServiceCampaignRankDetailStandard entity : other.getStandardList()) {
            }
        }
    }

    @Override
    public void fillDetail(OpenServiceCampaignRankDetail detail) {
        detail.setMessageList(getOpenServiceCampaignRankDetailMessageList(detail));
        detail.setRankingList(getOpenServiceCampaignRankDetailRankingList(detail));
        detail.setScoreList(getOpenServiceCampaignRankDetailScoreList(detail));
        detail.setStandardList(getOpenServiceCampaignRankDetailStandardList(detail));
    }

    private List<OpenServiceCampaignRankDetailMessage> getOpenServiceCampaignRankDetailMessageList(OpenServiceCampaignRankDetail detail) {
        Wrapper<OpenServiceCampaignRankDetailMessage> query = Wrappers.<OpenServiceCampaignRankDetailMessage>lambdaQuery()
                .eq(OpenServiceCampaignRankDetailMessage::getCampaignId, detail.getCampaignId())
                .eq(OpenServiceCampaignRankDetailMessage::getCampaignTypeId, detail.getCampaignTypeId())
                .eq(OpenServiceCampaignRankDetailMessage::getRankDetailId, detail.getId());
        return detailMessageService.list(query);
    }

    private List<OpenServiceCampaignRankDetailRanking> getOpenServiceCampaignRankDetailRankingList(OpenServiceCampaignRankDetail detail) {
        Wrapper<OpenServiceCampaignRankDetailRanking> query = Wrappers.<OpenServiceCampaignRankDetailRanking>lambdaQuery()
                .eq(OpenServiceCampaignRankDetailRanking::getCampaignId, detail.getCampaignId())
                .eq(OpenServiceCampaignRankDetailRanking::getCampaignTypeId, detail.getCampaignTypeId())
                .eq(OpenServiceCampaignRankDetailRanking::getRankDetailId, detail.getId());
        return detailRankingService.list(query);
    }

    private List<OpenServiceCampaignRankDetailScore> getOpenServiceCampaignRankDetailScoreList(OpenServiceCampaignRankDetail detail) {
        Wrapper<OpenServiceCampaignRankDetailScore> query = Wrappers.<OpenServiceCampaignRankDetailScore>lambdaQuery()
                .eq(OpenServiceCampaignRankDetailScore::getCampaignId, detail.getCampaignId())
                .eq(OpenServiceCampaignRankDetailScore::getCampaignTypeId, detail.getCampaignTypeId())
                .eq(OpenServiceCampaignRankDetailScore::getRankDetailId, detail.getId());
        return detailScoreService.list(query);
    }

    private List<OpenServiceCampaignRankDetailStandard> getOpenServiceCampaignRankDetailStandardList(OpenServiceCampaignRankDetail detail) {
        Wrapper<OpenServiceCampaignRankDetailStandard> query = Wrappers.<OpenServiceCampaignRankDetailStandard>lambdaQuery()
                .eq(OpenServiceCampaignRankDetailStandard::getCampaignId, detail.getCampaignId())
                .eq(OpenServiceCampaignRankDetailStandard::getCampaignTypeId, detail.getCampaignTypeId())
                .eq(OpenServiceCampaignRankDetailStandard::getRankDetailId, detail.getId());
        return detailStandardService.list(query);
    }
}

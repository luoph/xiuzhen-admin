package cn.youai.xiuzhen.game.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.youai.xiuzhen.game.entity.OpenServiceCampaignLotteryDetailRanking;
import cn.youai.xiuzhen.game.mapper.OpenServiceCampaignLotteryDetailRankingMapper;
import cn.youai.xiuzhen.game.service.IOpenServiceCampaignLotteryDetailRankingService;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 开服夺宝榜单
 * @date 2020-12-25
 */
@Service
@DS("master")
public class OpenServiceCampaignLotteryDetailRankingServiceImpl extends ServiceImpl<OpenServiceCampaignLotteryDetailRankingMapper, OpenServiceCampaignLotteryDetailRanking> implements IOpenServiceCampaignLotteryDetailRankingService {

    @Override
    public void duplicate(OpenServiceCampaignLotteryDetailRanking other, long detailId, long typeId, long campaignId) {
        OpenServiceCampaignLotteryDetailRanking copy = new OpenServiceCampaignLotteryDetailRanking(other);
        copy.setLotteryDetailId(detailId);
        copy.setCampaignTypeId(typeId);
        copy.setCampaignId(campaignId);
        save(copy);
    }

    @Override
    public void duplicate(List<OpenServiceCampaignLotteryDetailRanking> others, long detailId, long typeId, long campaignId) {
        List<OpenServiceCampaignLotteryDetailRanking> addList = new ArrayList<>();
        for (OpenServiceCampaignLotteryDetailRanking other : others) {
            OpenServiceCampaignLotteryDetailRanking copy = new OpenServiceCampaignLotteryDetailRanking(other);
            copy.setLotteryDetailId(detailId);
            copy.setCampaignTypeId(typeId);
            copy.setCampaignId(campaignId);
            addList.add(copy);
        }

        if (CollUtil.isNotEmpty(addList)) {
            saveBatch(addList);
        }
    }
}

package cn.youai.xiuzhen.game.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.youai.xiuzhen.game.entity.OpenServiceCampaignLotteryDetailScore;
import cn.youai.xiuzhen.game.mapper.OpenServiceCampaignLotteryDetailScoreMapper;
import cn.youai.xiuzhen.game.service.IOpenServiceCampaignLotteryDetailScoreService;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 开服夺宝积分
 * @date 2020-12-25
 */
@Service
@DS("master")
public class OpenServiceCampaignLotteryDetailScoreServiceImpl extends ServiceImpl<OpenServiceCampaignLotteryDetailScoreMapper, OpenServiceCampaignLotteryDetailScore> implements IOpenServiceCampaignLotteryDetailScoreService {

    @Override
    public void duplicate(OpenServiceCampaignLotteryDetailScore other, long detailId, long typeId, long campaignId) {
        OpenServiceCampaignLotteryDetailScore copy = new OpenServiceCampaignLotteryDetailScore(other);
        copy.setLotteryDetailId(detailId);
        copy.setCampaignTypeId(typeId);
        copy.setCampaignId(campaignId);
        save(copy);
    }

    @Override
    public void duplicate(List<OpenServiceCampaignLotteryDetailScore> others, long detailId, long typeId, long campaignId) {
        List<OpenServiceCampaignLotteryDetailScore> addList = new ArrayList<>();
        for (OpenServiceCampaignLotteryDetailScore other : others) {
            OpenServiceCampaignLotteryDetailScore copy = new OpenServiceCampaignLotteryDetailScore(other);
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

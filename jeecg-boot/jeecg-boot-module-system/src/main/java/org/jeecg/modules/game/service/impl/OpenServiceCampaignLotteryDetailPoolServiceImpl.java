package org.jeecg.modules.game.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.game.entity.OpenServiceCampaignLotteryDetailPool;
import org.jeecg.modules.game.mapper.OpenServiceCampaignLotteryDetailPoolMapper;
import org.jeecg.modules.game.service.IOpenServiceCampaignLotteryDetailPoolService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 开服夺宝奖池
 * @date 2020-12-25
 */
@Service
public class OpenServiceCampaignLotteryDetailPoolServiceImpl extends ServiceImpl<OpenServiceCampaignLotteryDetailPoolMapper, OpenServiceCampaignLotteryDetailPool> implements IOpenServiceCampaignLotteryDetailPoolService {

    @Override
    public void duplicate(OpenServiceCampaignLotteryDetailPool other, long detailId, long typeId, long campaignId) {
        OpenServiceCampaignLotteryDetailPool copy = new OpenServiceCampaignLotteryDetailPool(other);
        copy.setLotteryDetailId(detailId);
        copy.setCampaignTypeId(typeId);
        copy.setCampaignId(campaignId);
        save(copy);
    }

    @Override
    public void duplicate(List<OpenServiceCampaignLotteryDetailPool> others, long detailId, long typeId, long campaignId) {
        List<OpenServiceCampaignLotteryDetailPool> addList = new ArrayList<>();
        for (OpenServiceCampaignLotteryDetailPool other : others) {
            OpenServiceCampaignLotteryDetailPool copy = new OpenServiceCampaignLotteryDetailPool(other);
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

package cn.youai.xiuzhen.game.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.youai.xiuzhen.game.entity.OpenServiceCampaignRankDetailStandard;
import cn.youai.xiuzhen.game.mapper.GameOpenServiceCampaignRankDetailStandardMapper;
import cn.youai.xiuzhen.game.service.IOpenServiceCampaignRankDetailStandardService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 开服活动-开服排行-活动明细-达标奖励
 * @date 2020-12-21
 */
@Service
public class OpenServiceCampaignRankDetailStandardServiceImpl extends ServiceImpl<GameOpenServiceCampaignRankDetailStandardMapper, OpenServiceCampaignRankDetailStandard> implements IOpenServiceCampaignRankDetailStandardService {

    @Override
    public void duplicate(OpenServiceCampaignRankDetailStandard other, long detailId, long typeId, long campaignId) {
        OpenServiceCampaignRankDetailStandard copy = new OpenServiceCampaignRankDetailStandard(other);
        copy.setRankDetailId(detailId);
        copy.setCampaignTypeId(typeId);
        copy.setCampaignId(campaignId);
        save(copy);
    }

    @Override
    public void duplicate(List<OpenServiceCampaignRankDetailStandard> others, long detailId, long typeId, long campaignId) {
        List<OpenServiceCampaignRankDetailStandard> addList = new ArrayList<>();
        for (OpenServiceCampaignRankDetailStandard other : others) {
            OpenServiceCampaignRankDetailStandard copy = new OpenServiceCampaignRankDetailStandard(other);
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

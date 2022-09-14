package cn.youai.xiuzhen.game.service;

import cn.youai.xiuzhen.game.entity.OpenServiceCampaignRankDetailScore;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 开服活动-开服排行-活动明细-消耗道具分数
 * @date 2020-12-21
 */
public interface IOpenServiceCampaignRankDetailScoreService extends IService<OpenServiceCampaignRankDetailScore> {

    void duplicate(OpenServiceCampaignRankDetailScore other, long detailId, long typeId, Long campaignId);

    void duplicate(List<OpenServiceCampaignRankDetailScore> others, long detailId, long typeId, long campaignId);
}

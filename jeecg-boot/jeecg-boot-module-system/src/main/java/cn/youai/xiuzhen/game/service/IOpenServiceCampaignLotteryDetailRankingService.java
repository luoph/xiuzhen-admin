package cn.youai.xiuzhen.game.service;

import cn.youai.xiuzhen.game.entity.OpenServiceCampaignLotteryDetailRanking;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 开服夺宝榜单
 * @date 2020-12-25
 */
public interface IOpenServiceCampaignLotteryDetailRankingService extends IService<OpenServiceCampaignLotteryDetailRanking> {

    void duplicate(OpenServiceCampaignLotteryDetailRanking other, long detailId, long typeId, long campaignId);

    void duplicate(List<OpenServiceCampaignLotteryDetailRanking> others, long detailId, long typeId, long campaignId);
}

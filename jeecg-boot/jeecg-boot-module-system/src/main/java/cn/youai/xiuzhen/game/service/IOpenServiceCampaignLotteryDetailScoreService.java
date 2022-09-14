package cn.youai.xiuzhen.game.service;

import cn.youai.xiuzhen.game.entity.OpenServiceCampaignLotteryDetailScore;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 开服夺宝积分
 * @date 2020-12-25
 */
public interface IOpenServiceCampaignLotteryDetailScoreService extends IService<OpenServiceCampaignLotteryDetailScore> {

    void duplicate(OpenServiceCampaignLotteryDetailScore other, long detailId, long typeId, long campaignId);

    void duplicate(List<OpenServiceCampaignLotteryDetailScore> others, long detailId, long typeId, long campaignId);
}

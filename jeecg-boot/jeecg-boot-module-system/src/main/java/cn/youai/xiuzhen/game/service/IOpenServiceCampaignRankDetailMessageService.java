package cn.youai.xiuzhen.game.service;

import cn.youai.xiuzhen.game.entity.OpenServiceCampaignRankDetailMessage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 开服活动-开服排行-活动明细-传闻
 * @date 2020-12-21
 */
public interface IOpenServiceCampaignRankDetailMessageService extends IService<OpenServiceCampaignRankDetailMessage> {

    void duplicate(OpenServiceCampaignRankDetailMessage other, long detailId, long typeId, long campaignId);

    void duplicate(List<OpenServiceCampaignRankDetailMessage> others, long detailId, long typeId, long campaignId);

}

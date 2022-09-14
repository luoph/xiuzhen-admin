package cn.youai.xiuzhen.game.service;

import cn.youai.xiuzhen.game.entity.OpenServiceCampaignRankDetail;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 开服活动-开服排行-活动明细(3级)
 * @date 2020-12-21
 */
public interface IOpenServiceCampaignRankDetailService extends IService<OpenServiceCampaignRankDetail> {

    void duplicate(OpenServiceCampaignRankDetail other, long typeId, long campaignId);

    void fillDetail(OpenServiceCampaignRankDetail detail);

}

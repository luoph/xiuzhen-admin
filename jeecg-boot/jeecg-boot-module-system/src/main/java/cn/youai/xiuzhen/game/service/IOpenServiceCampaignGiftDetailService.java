package cn.youai.xiuzhen.game.service;

import cn.youai.xiuzhen.game.entity.OpenServiceCampaignGiftDetail;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 开服活动-开服排行-活动明细(3级)
 * @date 2020-12-21
 */
public interface IOpenServiceCampaignGiftDetailService extends IService<OpenServiceCampaignGiftDetail> {

    void duplicate(OpenServiceCampaignGiftDetail other, long typeId, long campaignId);

    void fillDetail(OpenServiceCampaignGiftDetail detail);

}

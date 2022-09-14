package cn.youai.xiuzhen.game.service;

import cn.youai.xiuzhen.game.entity.OpenServiceCampaignSingleGiftDetail;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 开服活动-单笔好礼活动参数
 * @date 2020-12-23
 */
public interface IOpenServiceCampaignSingleGiftDetailService extends IService<OpenServiceCampaignSingleGiftDetail> {

    void duplicate(OpenServiceCampaignSingleGiftDetail other, long typeId, long campaignId);

    void fillDetail(OpenServiceCampaignSingleGiftDetail detail);

}

package cn.youai.xiuzhen.game.service;

import cn.youai.xiuzhen.game.entity.OpenServiceCampaignSingleGiftNotice;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 开服活动-单笔好礼-传闻消息
 * @date 2020-12-23
 */
public interface IOpenServiceCampaignSingleGiftNoticeService extends IService<OpenServiceCampaignSingleGiftNotice> {

    void duplicate(OpenServiceCampaignSingleGiftNotice other, long detailId, long typeId, long campaignId);

    void duplicate(List<OpenServiceCampaignSingleGiftNotice> others, long detailId, long typeId, long campaignId);
}

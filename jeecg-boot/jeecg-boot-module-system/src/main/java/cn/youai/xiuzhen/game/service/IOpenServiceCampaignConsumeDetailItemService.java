package cn.youai.xiuzhen.game.service;

import cn.youai.xiuzhen.game.entity.OpenServiceCampaignConsumeDetailItem;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 开服活动消耗道具
 * @date 2020-12-28
 */
public interface IOpenServiceCampaignConsumeDetailItemService extends IService<OpenServiceCampaignConsumeDetailItem> {

    void duplicate(OpenServiceCampaignConsumeDetailItem other, long detailId, long typeId, long campaignId);

    void duplicate(List<OpenServiceCampaignConsumeDetailItem> others, long detailId, long typeId, long campaignId);
}

package cn.youai.xiuzhen.game.service;

import cn.youai.xiuzhen.game.entity.OpenServiceCampaignConsumeDetailMessage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 开服活动消耗传闻
 * @date 2020-12-28
 */
public interface IOpenServiceCampaignConsumeDetailMessageService extends IService<OpenServiceCampaignConsumeDetailMessage> {

    void duplicate(OpenServiceCampaignConsumeDetailMessage other, long detailId, long typeId, long campaignId);

    void duplicate(List<OpenServiceCampaignConsumeDetailMessage> others, long detailId, long typeId, long campaignId);
}

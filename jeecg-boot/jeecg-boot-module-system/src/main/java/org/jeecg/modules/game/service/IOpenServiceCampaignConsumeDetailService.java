package org.jeecg.modules.game.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.game.entity.OpenServiceCampaignConsumeDetail;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 开服互动消耗配置
 * @date 2020-12-28
 */
public interface IOpenServiceCampaignConsumeDetailService extends IService<OpenServiceCampaignConsumeDetail> {

    void duplicate(OpenServiceCampaignConsumeDetail other, long typeId, long campaignId);

    void fillDetail(OpenServiceCampaignConsumeDetail detail);

}

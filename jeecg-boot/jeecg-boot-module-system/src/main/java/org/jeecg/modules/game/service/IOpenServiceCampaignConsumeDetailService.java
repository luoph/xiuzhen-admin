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

    void fillDetail(OpenServiceCampaignConsumeDetail detail);

    void duplicate(OpenServiceCampaignConsumeDetail entity, Long id, Long campaignId);
}

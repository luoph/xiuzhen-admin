package org.jeecg.modules.game.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.game.entity.OpenServiceCampaignType;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 开服活动-类型(2级)
 * @date 2020-12-21
 */
public interface IOpenServiceCampaignTypeService extends IService<OpenServiceCampaignType> {
    /**
     * 填充具体的活动配置信息
     *
     * @param model {@linkplain OpenServiceCampaignType}
     * @param merge 是否合并子活动信息到父容器（例如 buff 类型）
     */
    void fillTabDetail(OpenServiceCampaignType model, boolean merge);

    /**
     * 更新活动具体配置信息
     *
     * @param model {@linkplain OpenServiceCampaignType}
     */
    void updateTabDetail(OpenServiceCampaignType model);
}

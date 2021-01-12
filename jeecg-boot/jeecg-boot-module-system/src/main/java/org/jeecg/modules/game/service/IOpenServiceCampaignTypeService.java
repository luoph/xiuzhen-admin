package org.jeecg.modules.game.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.game.entity.OpenServiceCampaignType;

import java.util.List;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 开服活动-类型(2级)
 * @date 2020-12-21
 */
public interface IOpenServiceCampaignTypeService extends IService<OpenServiceCampaignType> {

    /**
     * 复制
     *
     * @param other      复制的数据源
     * @param campaignId 新的活动id
     */
    void duplicate(OpenServiceCampaignType other, Long campaignId);

    /**
     * 填充具体的活动配置信息
     *
     * @param model {@linkplain OpenServiceCampaignType}
     */
    void fillTabDetail(OpenServiceCampaignType model);

    /**
     * 查询活动id的一级类型列表
     *
     * @param campaignId 活动id
     * @return {@linkplain OpenServiceCampaignType}
     */
    List<OpenServiceCampaignType> selectTypeListByCampaignId(long campaignId);

}

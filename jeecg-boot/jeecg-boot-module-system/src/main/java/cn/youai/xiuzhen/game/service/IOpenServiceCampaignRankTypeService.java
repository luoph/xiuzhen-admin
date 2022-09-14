package cn.youai.xiuzhen.game.service;

import cn.youai.xiuzhen.game.entity.OpenServiceCampaignRankType;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 开服活动-开服排行-类型库
 * @date 2020-12-21
 */
public interface IOpenServiceCampaignRankTypeService extends IService<OpenServiceCampaignRankType> {

    void duplicate(OpenServiceCampaignRankType other, long campaignId);

}

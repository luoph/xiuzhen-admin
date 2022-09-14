package cn.youai.xiuzhen.game.service;

import cn.youai.xiuzhen.game.entity.OpenServiceCampaignConsumeDetail;
import com.baomidou.mybatisplus.extension.service.IService;

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

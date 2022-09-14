package cn.youai.xiuzhen.game.service;

import cn.youai.xiuzhen.game.entity.OpenServiceCampaignLotteryDetail;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 开服夺宝详情
 * @date 2020-12-25
 */
public interface IOpenServiceCampaignLotteryDetailService extends IService<OpenServiceCampaignLotteryDetail> {

    void duplicate(OpenServiceCampaignLotteryDetail other, long typeId, long campaignId);

    void fillDetail(OpenServiceCampaignLotteryDetail detail);

}

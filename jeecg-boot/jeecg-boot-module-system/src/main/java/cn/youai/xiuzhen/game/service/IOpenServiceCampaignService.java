package cn.youai.xiuzhen.game.service;

import cn.youai.xiuzhen.game.entity.OpenServiceCampaign;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 开服活动(1级)
 * @date 2020-12-21
 */
public interface IOpenServiceCampaignService extends IService<OpenServiceCampaign> {

    void duplicate(OpenServiceCampaign other);

}

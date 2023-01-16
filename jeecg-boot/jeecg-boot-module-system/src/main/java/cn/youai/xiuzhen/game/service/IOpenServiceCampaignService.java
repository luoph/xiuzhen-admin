package cn.youai.xiuzhen.game.service;

import cn.youai.xiuzhen.game.entity.OpenServiceCampaign;
import cn.youai.xiuzhen.game.entity.OpenServiceCampaignType;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.common.api.vo.Result;

import java.util.List;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 开服活动(1级)
 * @date 2020-12-21
 */
public interface IOpenServiceCampaignService extends IService<OpenServiceCampaign> {

    void duplicate(OpenServiceCampaign other);

    List<OpenServiceCampaignType> getCampaignTypeList(OpenServiceCampaign openServiceCampaign, boolean isLoadDetailExt);

    Result<?> removeCompletedServer(OpenServiceCampaign campaign);
    void syncCampaign(OpenServiceCampaign campaign);
}

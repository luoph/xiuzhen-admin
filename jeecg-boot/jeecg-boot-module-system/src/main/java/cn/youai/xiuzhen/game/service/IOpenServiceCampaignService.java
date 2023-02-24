package cn.youai.xiuzhen.game.service;

import cn.youai.xiuzhen.game.entity.GameChannelServer;
import cn.youai.xiuzhen.game.entity.OpenServiceCampaign;
import cn.youai.xiuzhen.game.entity.OpenServiceCampaignDetail;
import cn.youai.xiuzhen.game.entity.OpenServiceCampaignType;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.common.api.vo.Result;
import org.jooq.Collation;

import java.util.Collection;
import java.util.List;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 开服活动(1级)
 * @date 2020-12-21
 */
public interface IOpenServiceCampaignService extends IService<OpenServiceCampaign> {

    List<OpenServiceCampaign> queryCampaignList(Collection<Long> ids);

    List<OpenServiceCampaignDetail> queryCampaignDetailsFastly(int timeType);

    void duplicate(OpenServiceCampaign other);

    List<OpenServiceCampaignType> getCampaignTypeList(OpenServiceCampaign openServiceCampaign, boolean isLoadDetailExt);

    Result<?> removeCompletedServer(OpenServiceCampaign campaign);

    /**
     * 自动添加区服id，活动类型（开服第N天）
     *
     * @param channelServers channelServers
     */
    void addCampaignServerIds(List<GameChannelServer> channelServers);

    void syncCampaign(OpenServiceCampaign campaign);
}

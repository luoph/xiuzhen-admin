package cn.youai.xiuzhen.game.service;

import cn.youai.xiuzhen.game.constant.TimeType;
import cn.youai.xiuzhen.game.entity.GameCampaign;
import cn.youai.xiuzhen.game.entity.GameCampaignServer;
import cn.youai.xiuzhen.game.entity.GameCampaignType;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.common.api.vo.Result;

import java.util.List;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 活动配置
 * @date 2020-10-15
 */
public interface IGameCampaignService extends IService<GameCampaign> {

    List<GameCampaignType> getGameCampaignTypeList(GameCampaign gameCampaign);

    List<GameCampaign> queryCampaignListByTimeType(TimeType timeType);

    /**
     * 自动添加区服id，活动类型（开服第N天）
     * @param serverIds 区服id
     */
    void addCampaignServerIds(List<Integer> serverIds);

    void updateCampaign(GameCampaign gameCampaign);

    void batchSwitch(GameCampaignServer model);

    /**
     * 活动子页签对应的服务器列表
     *
     * @param page       分页
     * @param campaignId 活动 id
     * @param typeId     类型
     * @param server     服务器 id 或者名称
     * @return List of {@linkplain GameCampaignServer}
     */
    IPage<GameCampaignServer> serverList(Page<?> page, long campaignId, long typeId, String server);

    /**
     * 同步到区服
     *
     * @param campaign 活动
     */
    void syncCampaign(GameCampaign campaign);

    Result<?> removeCompletedServer(GameCampaign gameCampaign);
}

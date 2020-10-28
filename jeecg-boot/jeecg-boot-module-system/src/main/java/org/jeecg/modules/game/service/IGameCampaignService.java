package org.jeecg.modules.game.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.game.entity.GameCampaign;
import org.jeecg.modules.game.entity.GameCampaignServer;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 活动配置
 * @date 2020-10-15
 */
public interface IGameCampaignService extends IService<GameCampaign> {

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
}

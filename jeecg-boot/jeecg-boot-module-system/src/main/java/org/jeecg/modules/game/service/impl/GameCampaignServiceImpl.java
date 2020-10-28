package org.jeecg.modules.game.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.game.entity.GameCampaign;
import org.jeecg.modules.game.entity.GameCampaignServer;
import org.jeecg.modules.game.mapper.GameCampaignMapper;
import org.jeecg.modules.game.service.IGameCampaignService;
import org.springframework.stereotype.Service;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 活动配置
 * @date 2020-10-15
 */
@Service
public class GameCampaignServiceImpl extends ServiceImpl<GameCampaignMapper, GameCampaign> implements IGameCampaignService {

    @Override
    public IPage<GameCampaignServer> serverList(Page<?> page, long campaignId, long typeId, String server) {
        return getBaseMapper().serverList(page, campaignId, typeId, server);
    }
}

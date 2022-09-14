package cn.youai.xiuzhen.game.service.impl;

import cn.youai.xiuzhen.game.entity.GameCampaign;
import cn.youai.xiuzhen.game.entity.GameCampaignServer;
import cn.youai.xiuzhen.game.mapper.GameCampaignMapper;
import cn.youai.xiuzhen.game.service.IGameCampaignService;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 活动配置
 * @date 2020-10-15
 */
@Service
@DS("master")
public class GameCampaignServiceImpl extends ServiceImpl<GameCampaignMapper, GameCampaign> implements IGameCampaignService {

    @Override
    public IPage<GameCampaignServer> serverList(Page<?> page, long campaignId, long typeId, String server) {
        return getBaseMapper().serverList(page, campaignId, typeId, server);
    }
}

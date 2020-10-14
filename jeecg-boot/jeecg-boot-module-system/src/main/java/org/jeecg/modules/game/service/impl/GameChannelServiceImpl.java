package org.jeecg.modules.game.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.game.entity.GameChannel;
import org.jeecg.modules.game.entity.GameServer;
import org.jeecg.modules.game.mapper.GameChannelMapper;
import org.jeecg.modules.game.service.IGameChannelService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 游戏渠道
 * @date 2019-12-11
 */
@Service
public class GameChannelServiceImpl extends ServiceImpl<GameChannelMapper, GameChannel> implements IGameChannelService {

    @Resource
    private GameChannelMapper gameChannelMapper;

    @Override
    public List<GameServer> getServerListChannelId(Long channelId) {
        return gameChannelMapper.getServerListChannelId(channelId);
    }

    @Override
    public String queryChannelNameById(Integer channelId) {
        return gameChannelMapper.queryChannelNameById(channelId);
    }
}

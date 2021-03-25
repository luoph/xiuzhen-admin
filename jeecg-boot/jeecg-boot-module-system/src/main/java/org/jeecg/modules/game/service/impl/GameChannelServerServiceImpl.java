package org.jeecg.modules.game.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.game.entity.GameChannelServer;
import org.jeecg.modules.game.entity.GameServer;
import org.jeecg.modules.game.mapper.GameChannelServerMapper;
import org.jeecg.modules.game.service.IGameChannelServerService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 游戏渠道服配置
 * @date 2019-12-11
 */
@Service
public class GameChannelServerServiceImpl extends ServiceImpl<GameChannelServerMapper, GameChannelServer> implements IGameChannelServerService {

    @Override
    public List<GameServer> gameServerByChannelId(int channelId) {
        return getBaseMapper().gameServerByChannelId(channelId);
    }

    @Override
    public boolean isValidChannelWithServer(int channelId, int serverId) {
        LambdaQueryWrapper<GameChannelServer> queryWrapper = Wrappers.<GameChannelServer>lambdaQuery()
                .eq(GameChannelServer::getChannelId, channelId)
                .eq(GameChannelServer::getServerId, serverId);
        GameChannelServer channelServer = getOne(queryWrapper);
        // 绑定关系正常
        return channelServer != null && channelServer.getDelFlag() == 0 && channelServer.getNoNeedCount() == 0;
    }
}

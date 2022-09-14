package cn.youai.xiuzhen.game.service.impl;

import cn.youai.xiuzhen.game.entity.GameChannelServer;
import cn.youai.xiuzhen.game.entity.GameServerVO;
import cn.youai.xiuzhen.game.mapper.GameChannelServerMapper;
import cn.youai.xiuzhen.game.service.IGameChannelServerService;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 游戏渠道服配置
 * @date 2019-12-11
 */
@Service
@DS("master")
public class GameChannelServerServiceImpl extends ServiceImpl<GameChannelServerMapper, GameChannelServer> implements IGameChannelServerService {

    @Override
    public List<GameServerVO> selectServerListByChannelId(int channelId) {
        return getBaseMapper().selectServerListByChannelId(channelId);
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

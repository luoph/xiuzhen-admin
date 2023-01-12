package cn.youai.xiuzhen.game.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.youai.basics.utils.StringUtils;
import cn.youai.xiuzhen.game.entity.GameChannel;
import cn.youai.xiuzhen.game.entity.GameChannelServer;
import cn.youai.xiuzhen.game.entity.GameServerVO;
import cn.youai.xiuzhen.game.mapper.GameChannelServerMapper;
import cn.youai.xiuzhen.game.service.IGameChannelServerService;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
    public List<GameServerVO> selectServerList(String channel) {
        if (StringUtils.isBlank(channel)) {
            return getBaseMapper().selectServerList();
        }
        return getBaseMapper().selectChannelServerList(channel);
    }

    @Override
    public List<GameChannel> selectChannelList() {
        return getBaseMapper().selectChannelList();
    }

    @Override
    public List<GameServerVO> filterServerList(String channel, List<Integer> serverIds) {
        if (CollUtil.isEmpty(serverIds)) {
            return new ArrayList<>();
        }
        return getBaseMapper().filterServerList(channel, serverIds);
    }

}
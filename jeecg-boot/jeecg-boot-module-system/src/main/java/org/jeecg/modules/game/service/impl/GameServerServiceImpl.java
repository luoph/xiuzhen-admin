package org.jeecg.modules.game.service.impl;

import org.jeecg.modules.game.entity.GameServer;
import org.jeecg.modules.game.mapper.GameServerMapper;
import org.jeecg.modules.game.service.IGameServerService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 游戏服配置
 * @Author: jeecg-boot
 * @Date:   2019-12-05
 * @Version: V1.0
 */
@Service
public class GameServerServiceImpl extends ServiceImpl<GameServerMapper, GameServer> implements IGameServerService {

}

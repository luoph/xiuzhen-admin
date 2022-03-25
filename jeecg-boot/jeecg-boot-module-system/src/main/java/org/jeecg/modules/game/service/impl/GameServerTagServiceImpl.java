package org.jeecg.modules.game.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.game.entity.GameServerTag;
import org.jeecg.modules.game.mapper.GameServerTagMapper;
import org.jeecg.modules.game.service.IGameServerTagService;
import org.springframework.stereotype.Service;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 游戏服标签
 * @date 2022-03-25
 */
@Service
public class GameServerTagServiceImpl extends ServiceImpl<GameServerTagMapper, GameServerTag> implements IGameServerTagService {

}

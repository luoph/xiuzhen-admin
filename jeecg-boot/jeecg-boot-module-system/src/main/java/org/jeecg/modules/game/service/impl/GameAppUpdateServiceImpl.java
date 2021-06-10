package org.jeecg.modules.game.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.game.entity.GameAppUpdate;
import org.jeecg.modules.game.mapper.GameAppUpdateMapper;
import org.jeecg.modules.game.service.IGameAppUpdateService;
import org.springframework.stereotype.Service;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 客户端更新配置
 * @date 2021-06-10
 */
@Service
public class GameAppUpdateServiceImpl extends ServiceImpl<GameAppUpdateMapper, GameAppUpdate> implements IGameAppUpdateService {

}

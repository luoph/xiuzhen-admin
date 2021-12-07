/*
 * create by mybatis-plus-generator  https://github.com/xiweile
 */
package org.jeecg.modules.game.service.impl;

import cn.youai.entities.GamePlayer;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.game.mapper.GamePlayerMapper;
import org.jeecg.modules.game.service.IGamePlayerService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 玩家表 服务实现类
 * </p>
 *
 * @author buliangliang
 * @since 2021-12-07
 */
@Service
public class GamePlayerServiceImpl extends ServiceImpl<GamePlayerMapper, GamePlayer> implements IGamePlayerService {

}

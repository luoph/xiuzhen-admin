package org.jeecg.modules.game.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.game.entity.GameInfo;
import org.jeecg.modules.game.mapper.GameInfoMapper;
import org.jeecg.modules.game.service.IGameInfoService;
import org.springframework.stereotype.Service;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 游戏信息
 * @date 2019-12-11
 */
@Service
public class GameInfoServiceImpl extends ServiceImpl<GameInfoMapper, GameInfo> implements IGameInfoService {

}

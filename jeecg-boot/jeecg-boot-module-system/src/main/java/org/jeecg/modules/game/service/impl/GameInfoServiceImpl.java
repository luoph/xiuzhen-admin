package org.jeecg.modules.game.service.impl;

import org.jeecg.modules.game.entity.GameInfo;
import org.jeecg.modules.game.mapper.GameInfoMapper;
import org.jeecg.modules.game.service.IGameInfoService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @description 游戏信息
 * @author jeecg-boot
 * @date   2019-12-11
 * @version V1.0
 */
@Service
public class GameInfoServiceImpl extends ServiceImpl<GameInfoMapper, GameInfo> implements IGameInfoService {

}

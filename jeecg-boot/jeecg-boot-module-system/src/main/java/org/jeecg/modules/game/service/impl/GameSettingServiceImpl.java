package org.jeecg.modules.game.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.game.entity.GameSetting;
import org.jeecg.modules.game.mapper.GameSettingMapper;
import org.jeecg.modules.game.service.IGameSettingService;
import org.springframework.stereotype.Service;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 游戏设置
 * @date 2020-01-04
 */
@Service
public class GameSettingServiceImpl extends ServiceImpl<GameSettingMapper, GameSetting> implements IGameSettingService {

}

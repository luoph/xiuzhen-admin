package cn.youai.xiuzhen.game.service.impl;

import cn.youai.xiuzhen.game.entity.GameSetting;
import cn.youai.xiuzhen.game.mapper.GameSettingMapper;
import cn.youai.xiuzhen.game.service.IGameSettingService;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 游戏设置
 * @date 2020-01-04
 */
@Service
@DS("master")
public class GameSettingServiceImpl extends ServiceImpl<GameSettingMapper, GameSetting> implements IGameSettingService {

}

package org.jeecg.modules.game.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.game.service.IPlayerBanInfoService;
import org.jeecg.modules.game.entity.GamePlayerBanInfo;
import org.jeecg.modules.game.mapper.GamePlayerBanInfoMapper;
import org.springframework.stereotype.Service;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 封禁管理
 * @date 2020-10-22
 */
@Service
public class PlayerBanInfoServiceImpl extends ServiceImpl<GamePlayerBanInfoMapper, GamePlayerBanInfo> implements IPlayerBanInfoService {

}

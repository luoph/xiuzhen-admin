package org.jeecg.modules.player.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.player.entity.PlayerBanInfo;
import org.jeecg.modules.player.mapper.PlayerBanInfoMapper;
import org.jeecg.modules.player.service.IPlayerBanInfoService;
import org.springframework.stereotype.Service;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 封禁管理
 * @date 2020-10-22
 */
@Service
public class PlayerBanInfoServiceImpl extends ServiceImpl<PlayerBanInfoMapper, PlayerBanInfo> implements IPlayerBanInfoService {

}

package org.jeecg.modules.player.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.player.entity.PlayerRegisterInfo;
import org.jeecg.modules.player.mapper.PlayerRegisterInfoMapper;
import org.jeecg.modules.player.service.IPlayerRegisterInfoService;
import org.springframework.stereotype.Service;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 玩家注册信息
 * @date 2020-01-04
 */
@Service
public class PlayerRegisterInfoServiceImpl extends ServiceImpl<PlayerRegisterInfoMapper, PlayerRegisterInfo> implements IPlayerRegisterInfoService {

}

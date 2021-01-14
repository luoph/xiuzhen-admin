package org.jeecg.modules.game.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.game.entity.LogPlayer;
import org.jeecg.modules.game.mapper.LogPlayerMapper;
import org.jeecg.modules.game.service.ILogPlayerService;
import org.springframework.stereotype.Service;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description log_player
 * @date 2021-01-14
 */
@Service
public class LogPlayerServiceImpl extends ServiceImpl<LogPlayerMapper, LogPlayer> implements ILogPlayerService {

}

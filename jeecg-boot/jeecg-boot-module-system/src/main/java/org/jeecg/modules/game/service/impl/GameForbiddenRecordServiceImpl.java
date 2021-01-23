package org.jeecg.modules.game.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.game.entity.GameForbiddenRecord;
import org.jeecg.modules.game.mapper.GameForbiddenRecordMapper;
import org.jeecg.modules.game.service.IGameForbiddenRecordService;
import org.springframework.stereotype.Service;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 封禁记录
 * @date 2021-01-23
 */
@Service
public class GameForbiddenRecordServiceImpl extends ServiceImpl<GameForbiddenRecordMapper, GameForbiddenRecord> implements IGameForbiddenRecordService {

}

package org.jeecg.modules.game.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.game.entity.LogPlayer;
import org.jeecg.modules.game.mapper.LogPlayerMapper;
import org.jeecg.modules.game.service.ILogPlayerService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * TODO 优化统计查询
 *
 * @author jeecg-boot
 * @version V1.0
 * @description log_player
 * @date 2021-01-14
 */
@Service
@DS("shardingSphere")
public class LogPlayerServiceImpl extends ServiceImpl<LogPlayerMapper, LogPlayer> implements ILogPlayerService {

    @Override
    public List<Map> selectMilitaryStrengVoAll(int serverId, String createDateBegin, String createDateEnd) {
        return getBaseMapper().selectMilitaryStrengVoAll(serverId, createDateBegin, createDateEnd);
    }

    @Override
    public List<Map> selectMilitaryStrengVoAllByPlayerId(int serverId, String playerId, String createDateBegin, String createDateEnd) {
        return getBaseMapper().selectMilitaryStrengVoAllByPlayerId(serverId, playerId, createDateBegin, createDateEnd);
    }
}

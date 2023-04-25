package cn.youai.xiuzhen.stat.service.impl;

import cn.youai.xiuzhen.stat.entity.CombatPowerLog;
import cn.youai.xiuzhen.stat.entity.LogPlayer;
import cn.youai.xiuzhen.stat.mapper.LogPlayerMapper;
import cn.youai.xiuzhen.stat.service.ILogPlayerService;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.common.system.query.QueryGenerator;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description log_player
 * @date 2021-01-14
 */
@Service
@DS("shardingSphere")
public class LogPlayerServiceImpl extends ServiceImpl<LogPlayerMapper, LogPlayer> implements ILogPlayerService {

    @Override
    public IPage<CombatPowerLog> selectCombatPowerLogList(Page<?> page, String channel, Integer serverId, Long playerId, Date start, Date end) {
        String configAuth = QueryGenerator.getAllConfigAuth();
        return getBaseMapper().selectCombatPowerLogList(page, channel, serverId, playerId, start, end, configAuth);
    }

}

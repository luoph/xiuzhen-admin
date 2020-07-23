package org.jeecg.modules.player.service.impl;

import cn.youai.commons.model.ResponseCode;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.database.DataSourceHelper;
import org.jeecg.modules.player.entity.BackpackLog;
import org.jeecg.modules.player.entity.PlayerItemLog;
import org.jeecg.modules.player.mapper.BackpackLogMapper;
import org.jeecg.modules.player.service.BackpackLogService;
import org.jeecg.modules.player.service.IPlayerItemLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @ClassName BackpackLogServiceImpl
 * @Author buliangliang
 * @Description 背包日志统计
 * @Date 2019/11/27 20:10
 * @Version 1.0
 */
@Service
@Slf4j
public class BackpackLogServiceImpl extends ServiceImpl<BackpackLogMapper, BackpackLog> implements BackpackLogService {

    @Autowired
    private IPlayerItemLogService playerItemLogService;

    @Override
    public ResponseCode syncBackpackLog(BackpackLog model, int serverId, Map<String, String[]> paramMap, String syncTimeBegin, String syncTimeEnd) {
        try {
            // 切换数据源设置查询器的初始查询条件
            DataSourceHelper.useServerDatabase(serverId);
            QueryWrapper<BackpackLog> queryWrapper = QueryGenerator.initQueryWrapper(model, paramMap);
            queryWrapper.between("create_date", syncTimeBegin, syncTimeEnd);

            // 根据条件查询列表
            List<BackpackLog> list = list(queryWrapper);
            if (list != null && !list.isEmpty()) {
                List<PlayerItemLog> playerItemLogs = new ArrayList<>(list.size());
                for (BackpackLog log : list) {
                    PlayerItemLog playerItemLog = playerItemLogService.writePlayerItemLog(serverId, log);
                    playerItemLogs.add(playerItemLog);
                }
                playerItemLogService.saveBatchLog(playerItemLogs);
                return ResponseCode.SUCCESS;
            }
            return new ResponseCode(303, "没有可同步的数据！");
        } catch (Exception e) {
            log.error("query error:" + e.getMessage());
        } finally {
            DataSourceHelper.useDefaultDatabase();
        }
        return ResponseCode.SYS_ERROR;
    }

}

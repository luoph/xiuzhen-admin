package org.jeecg.modules.player.service.impl;

import cn.youai.commons.model.ResponseCode;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.database.DataSourceHelper;
import org.jeecg.modules.player.entity.BackpackLog;
import org.jeecg.modules.player.entity.PlayerInfo;
import org.jeecg.modules.player.entity.PlayerItemLog;
import org.jeecg.modules.player.mapper.BackpackLogMapper;
import org.jeecg.modules.player.service.BackpackLogService;
import org.jeecg.modules.player.service.IPlayerInfoService;
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

    @Autowired
    private IPlayerInfoService playerInfoService;

    @Override
    public ResponseCode syncBackpackLog(BackpackLog model, long playerId, int serverId, Map<String, String[]> paramMap, String syncTimeBegin, String syncTimeEnd) {
        try {
            // 切换数据源设置查询器的初始查询条件
            DataSourceHelper.useServerDatabase(serverId);
            QueryWrapper<BackpackLog> queryWrapper = QueryGenerator.initQueryWrapper(model, paramMap);
            queryWrapper.between("create_date", syncTimeBegin, syncTimeEnd);
            if (playerId > 0) {
                // 手动同步防止数据量过大而超时 指定同步具体玩家的数据
                queryWrapper.eq("player_id", playerId);

                PlayerInfo playerInfo = playerInfoService.getById(playerId);
                if (playerInfo == null) {
                    return ResponseCode.SYS_PLAYER_EXIST;
                }
            }
            // 根据条件查询列表
            List<BackpackLog> list = list(queryWrapper);
            if (list != null && !list.isEmpty()) {
                List<PlayerItemLog> playerItemLogs = new ArrayList<>(list.size());
                for (BackpackLog log : list) {
                    PlayerItemLog playerItemLog = playerItemLogService.writePlayerItemLog(serverId, log);
                    playerItemLogs.add(playerItemLog);
                }

//                Map<PlayerItemLog, PlayerItemLog> map = new HashMap<>(1);
//                for (PlayerItemLog playerItemLog : playerItemLogs) {
//                    PlayerItemLog log = map.get(playerItemLog);
//                    if (log == null) {
//                        map.put(playerItemLog, playerItemLog);
//                    } else {
//                        log.setNum(log.getNum() + playerItemLog.getNum());
//                    }
//                }
//                List<PlayerItemLog> logArrayList = new ArrayList<>(map.values());

                playerItemLogService.saveBatchLog(playerItemLogs);
                return ResponseCode.SUCCESS;
            } else {
                return new ResponseCode(303, "没有可同步的数据！");
            }
        } catch (Exception e) {
            log.error("query error:" + e.getMessage());
            return ResponseCode.SYS_ERROR;
        } finally {
            DataSourceHelper.useDefaultDatabase();
        }
    }

}

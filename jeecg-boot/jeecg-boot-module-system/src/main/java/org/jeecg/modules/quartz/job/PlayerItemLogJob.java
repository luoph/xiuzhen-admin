package org.jeecg.modules.quartz.job;

import cn.youai.xiuzhen.utils.DateUtils;
import org.jeecg.common.util.SpringContextUtils;
import org.jeecg.modules.game.entity.GameServer;
import org.jeecg.modules.game.service.IGameServerService;
import org.jeecg.modules.player.entity.BackpackLog;
import org.jeecg.modules.player.service.BackpackLogService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName PlayerItemLogJob
 * @Description 玩家道具日志同步定时器
 * @Author buliangliang
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2020-07-23 10:36
 */
public class PlayerItemLogJob implements Job {

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        IGameServerService gameServerService = SpringContextUtils.getBean(IGameServerService.class);
        if (gameServerService != null) {
            List<GameServer> gameServers = gameServerService.list();
            if (gameServers != null && !gameServers.isEmpty()) {
                BackpackLogService backpackLogService = SpringContextUtils.getBean(BackpackLogService.class);
                if (backpackLogService != null) {
                    BackpackLog backpackLog = new BackpackLog();
                    Map<String, String[]> paramMap = new HashMap<>(2);
                    String syncStarDate = DateUtils.formatDateTimeStr(DateUtils.gameStartTimeOfDate(DateUtils.now()));
                    String syncEndDate = DateUtils.formatDateTimeStr(DateUtils.gameEndTimeOfDate(DateUtils.now()));
                    paramMap.put("syncTimeBegin", new String[]{syncStarDate});
                    paramMap.put("syncTimeEnd", new String[]{syncEndDate});
                    for (GameServer gameServer : gameServers) {
                        backpackLogService.syncBackpackLog(backpackLog, gameServer.getId(), paramMap, syncStarDate, syncEndDate);
                    }
                }
            }
        }

    }
}

package org.jeecg.modules.quartz.job;

import cn.youai.xiuzhen.utils.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.SpringContextUtils;
import org.jeecg.modules.game.entity.GameServer;
import org.jeecg.modules.game.service.IGameServerService;
import org.jeecg.modules.player.entity.BackpackLog;
import org.jeecg.modules.player.service.BackpackLogService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;

import java.util.Date;
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
@Slf4j
public class GamePlayerItemLogJob implements Job {

    @Override
    public void execute(JobExecutionContext context) {
        IGameServerService gameServerService = SpringContextUtils.getBean(IGameServerService.class);
        List<GameServer> gameServers = gameServerService.list();
        if (gameServers != null && !gameServers.isEmpty()) {
            BackpackLogService backpackLogService = SpringContextUtils.getBean(BackpackLogService.class);
            BackpackLog backpackLog = new BackpackLog();
            Map<String, String[]> paramMap = new HashMap<>(2);
            // 同步游戏日期的前一天
            Date gameDate = DateUtils.gameDate(DateUtils.addDays(DateUtils.now(), -1));
            String syncStarDate = DateUtils.formatDateTimeStr(DateUtils.startTimeOfDate(gameDate));
            String syncEndDate = DateUtils.formatDateTimeStr(DateUtils.endTimeOfDate(gameDate));

            paramMap.put("syncTimeBegin", new String[]{syncStarDate});
            paramMap.put("syncTimeEnd", new String[]{syncEndDate});

            for (GameServer gameServer : gameServers) {
                backpackLogService.syncBackpackLog(backpackLog, gameServer.getId(), 0, paramMap, syncStarDate, syncEndDate);
            }
        }

    }
}
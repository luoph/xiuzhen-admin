package cn.youai.xiuzhen.job;

import cn.hutool.core.collection.CollUtil;
import cn.youai.server.utils.DateUtils;
import cn.youai.xiuzhen.game.service.IGameServerService;
import cn.youai.xiuzhen.stat.service.ILogCmdService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * @author luopeihuan
 */
@Slf4j
@Component
public class DataStatJob {

    @Value("${app.stat.cmd-cost-time:500}")
    private int costTime;

    @Autowired
    private ILogCmdService logCmdService;

    @Autowired
    private IGameServerService gameServerService;

    @Async
    // 秒 分 时 日 月 星期 年份
    @Scheduled(cron = "0 30 0/1 * * ?")
    public void cmdTodayReport() {
        log.info("start cmdTodayReport");
        long time = System.currentTimeMillis();
        Date todayDate = DateUtils.todayDate();
        List<Integer> serverIds = gameServerService.selectRunningServerIds(todayDate);
        if (CollUtil.isNotEmpty(serverIds)) {
            for (Integer sid : serverIds) {
                logCmdService.genReport(sid, todayDate, costTime);
            }
        }
        log.info("finish cmdTodayReport, cost:{}ms", (System.currentTimeMillis() - time));
    }

    @Async
    // 秒 分 时 日 月 星期 年份
    @Scheduled(cron = "0 0 3 * * ?")
    public void cmdYesterdayReport() {
        log.info("start cmdYesterdayReport");
        long time = System.currentTimeMillis();
        Date yesterday = DateUtils.addDays(DateUtils.todayDate(), -1);
        List<Integer> serverIds = gameServerService.selectRunningServerIds(yesterday);
        if (CollUtil.isNotEmpty(serverIds)) {
            for (Integer sid : serverIds) {
                logCmdService.genReport(sid, yesterday, costTime);
            }
        }
        log.info("finish cmdYesterdayReport, cost:{}ms", (System.currentTimeMillis() - time));
    }

}

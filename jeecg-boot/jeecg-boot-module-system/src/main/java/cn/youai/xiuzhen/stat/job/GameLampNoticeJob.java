package cn.youai.xiuzhen.stat.job;

import cn.youai.server.utils.DateUtils;
import cn.youai.xiuzhen.game.entity.GameLampNotice;
import cn.youai.xiuzhen.game.service.IGameLampNoticeService;
import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

/**
 * QuartzJobList 配置的定时任务
 * @ClassName GameLampNoticeJob
 * @Description 跑马灯消息定时广播
 * @Author buliangliang
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2020-08-10 15:57
 */
@Slf4j
public class GameLampNoticeJob implements Job {

    @Autowired
    private IGameLampNoticeService gameLampNoticeService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        List<GameLampNotice> gameLampNotices = gameLampNoticeService.list();
        if (gameLampNotices != null && !gameLampNotices.isEmpty()) {
            for (GameLampNotice gameLampNotice : gameLampNotices) {
                boolean validDateTimeCheck = isValidDateTimeCheck(gameLampNotice);
                if (!validDateTimeCheck) {
                    continue;
                }
                boolean validSendTime = isValidSendTime(gameLampNotice);
                if (!validSendTime) {
                    continue;
                }
                // 判定消息状态 1-开启 0-关闭
                int status = gameLampNotice.getStatus();
                if (status != 1) {
                    continue;
                }
                String gameServerList = gameLampNotice.getGameServerList();
                if (StringUtils.isNoneBlank(gameServerList)) {
                    List<Integer> serverIds = JSON.parseArray(gameServerList, Integer.class);
                    gameLampNoticeService.sendLampNoticeToGameServer(serverIds, gameLampNotice);
                }
            }
        }
    }

    private boolean isValidDateTimeCheck(GameLampNotice gameLampNotice) {
        Date now = DateUtils.now();
        Date beginTime = gameLampNotice.getBeginTime();
        Date endTime = gameLampNotice.getEndTime();
        if (beginTime != null && endTime != null) {
            return now.after(beginTime) && now.before(endTime);
        }
        return endTime == null;
    }

    private boolean isValidSendTime(GameLampNotice gameLampNotice) {
        Date now = DateUtils.now();
        Date lastSendTime = gameLampNotice.getLastSendTime();
        if (lastSendTime != null) {
            return DateUtils.secondsBetween(lastSendTime, now) > gameLampNotice.getFrequency();
        }
        return true;
    }
}

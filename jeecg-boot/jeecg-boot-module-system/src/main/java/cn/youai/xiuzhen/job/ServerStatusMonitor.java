package cn.youai.xiuzhen.job;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.youai.server.springboot.component.OkHttpHelper;
import cn.youai.xiuzhen.game.entity.GameServer;
import cn.youai.xiuzhen.game.model.ServerStatusWarningData;
import cn.youai.xiuzhen.game.service.IGameServerGroupService;
import cn.youai.xiuzhen.game.service.IGameServerService;
import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author luopeihuan
 */
@Slf4j
@Component
public class ServerStatusMonitor {

    @Value("${app.monitor.server-status:true}")
    private boolean enable;

    @Value("${app.monitor.retry-times:3}")
    private int retryTimes;

    @Value("${app.monitor.retry-interval:2000}")
    private int retryInterval;

    @Value("${app.server-status-url:/game/status}")
    private String gameServerStatusUrl;

    @Value("${app.cross-server-status-url:/gm/status}")
    private String crossServerStatusUrl;

    @Value("${app.monitor.game-server-job:}")
    private String gameServerJobUrl;

    @Value("${app.monitor.cross-server-job:}")
    private String crossServerJobUrl;

    @Value("${app.monitor.profile:}")
    private String profile;

    @Value("${app.url.workflow:}")
    private String workflowUrl;

    @Autowired
    private IGameServerService serverService;

    @Autowired
    private IGameServerGroupService serverGroupService;

    @Async
    // 秒 分 时 日 月 星期 年份
    @Scheduled(cron = "0 0/5 * * * ?")
    public void monitorServer() {
        if (!enable) {
            return;
        }

        log.info("start monitor server");
        List<GameServer> serverList = serverService.selectOnlineGameServerList().stream().filter(t -> !t.skipCheck()).collect(Collectors.toList());

        Set<Integer> serverIds = serverList.stream().map(GameServer::getId).collect(Collectors.toSet());
        Set<Integer> groupIds = serverList.stream().map(GameServer::getGroupId).collect(Collectors.toSet());

        List<Integer> failedServerIds = new ArrayList<>(serverIds);
        List<Integer> failedGroupIds = new ArrayList<>(groupIds);

        int times = 0;
        while (times++ < retryTimes && (failedServerIds.size() > 0 || failedGroupIds.size() > 0)) {
            log.info("monitor check times:{}", times);
            failedServerIds = checkGameServerStatus(failedServerIds);
            failedGroupIds = checkCrossServerStatus(failedGroupIds);

            try {
                Thread.sleep(retryInterval);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException(e);
            }
        }

        if (StrUtil.isNotEmpty(workflowUrl) && (CollUtil.isNotEmpty(failedServerIds) || CollUtil.isNotEmpty(failedGroupIds))) {
            String url = workflowUrl + "/lark/serverStatus";
            ServerStatusWarningData warningData = new ServerStatusWarningData().setProfile(profile)
                    .setServerIds(StrUtil.join(",", failedServerIds))
                    .setGroupIds(StrUtil.join(",", failedGroupIds))
                    .setGameServerJobUrl(gameServerJobUrl).setCrossServerJobUrl(crossServerJobUrl);
            String response = OkHttpHelper.post(url, warningData);
            log.info("warning request url:{}, request:{}, response:{}", url, JSON.toJSONString(warningData), response);
        }

        log.info("finish monitor server, failed serverIds:{}, groupIds:{}", failedServerIds, failedGroupIds);
    }

    @SuppressWarnings("DuplicatedCode")
    private List<Integer> checkGameServerStatus(List<Integer> serverIds) {
        if (CollUtil.isEmpty(serverIds)) {
            return serverIds;
        }

        Map<Integer, String> statusMap = serverService.getUrl(serverIds, gameServerStatusUrl, String.class);
        return serverIds.stream().filter(t -> statusMap.get(t) == null).sorted().collect(Collectors.toList());
    }

    @SuppressWarnings("DuplicatedCode")
    private List<Integer> checkCrossServerStatus(List<Integer> serverIds) {
        if (CollUtil.isEmpty(serverIds)) {
            return serverIds;
        }

        Map<Integer, String> statusMap = serverGroupService.getUrl(serverIds, crossServerStatusUrl, String.class);
        return serverIds.stream().filter(t -> statusMap.get(t) == null).sorted().collect(Collectors.toList());
    }

}

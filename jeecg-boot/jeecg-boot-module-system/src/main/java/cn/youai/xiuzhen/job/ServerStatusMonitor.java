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

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author luopeihuan
 */
@Slf4j
@Component
public class ServerStatusMonitor {

    @Value("${app.monitor.server-status:true}")
    private boolean enable;

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
        Map<Integer, String> serverStatusMap = serverService.getUrl(serverIds, gameServerStatusUrl, String.class);

        Set<Integer> groupIds = serverList.stream().map(GameServer::getGroupId).collect(Collectors.toSet());
        Map<Integer, String> groupStatusMap = serverGroupService.getUrl(groupIds, crossServerStatusUrl, String.class);

        List<Integer> breakdownServerIds = serverIds.stream().filter(t -> serverStatusMap.get(t) == null).sorted().collect(Collectors.toList());
        List<Integer> breakdownGroupIds = groupIds.stream().filter(t -> groupStatusMap.get(t) == null).sorted().collect(Collectors.toList());

        if (StrUtil.isNotEmpty(workflowUrl) && (CollUtil.isNotEmpty(breakdownServerIds) || CollUtil.isNotEmpty(breakdownGroupIds))) {
            String url = workflowUrl + "/lark/serverStatus";
            ServerStatusWarningData warningData = new ServerStatusWarningData().setProfile(profile)
                    .setServerIds(StrUtil.join(",", breakdownServerIds))
                    .setGroupIds(StrUtil.join(",", breakdownGroupIds))
                    .setGameServerJobUrl(gameServerJobUrl).setCrossServerJobUrl(crossServerJobUrl);
            String response = OkHttpHelper.post(url, warningData);
            log.info("warning request url:{}, request:{}, response:{}", url, JSON.toJSONString(warningData), response);
        }

        log.info("finish monitor server, failed serverIds:{}, groupIds:{}", breakdownServerIds, breakdownGroupIds);
    }

}

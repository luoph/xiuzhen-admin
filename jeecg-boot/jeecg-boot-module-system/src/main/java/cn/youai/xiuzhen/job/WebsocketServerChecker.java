package cn.youai.xiuzhen.job;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.youai.basics.model.DataResponse;
import cn.youai.entities.GameServerStatus;
import cn.youai.server.springboot.component.OkHttpHelper;
import cn.youai.xiuzhen.game.entity.GameServer;
import cn.youai.xiuzhen.game.model.ServerWarningData;
import cn.youai.xiuzhen.game.model.WebsocketCheckResult;
import cn.youai.xiuzhen.game.service.IGameServerService;
import cn.youai.xiuzhen.game.service.WebsocketService;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author luopeihuan
 */
@Slf4j
@Component
public class WebsocketServerChecker {
    private static final Type RESPONSE_SERVER_STATUS = new TypeReference<DataResponse<GameServerStatus>>() {
    }.getType();

    @Value("${app.websocket.test-enable:true}")
    private boolean enable;

    @Value("${app.websocket.test-times:5}")
    private int testTimes;

    @Value("${app.websocket.warning-times:4}")
    private int warningTimes;

    @Value("${app.server-status-url:/game/status}")
    private String serverStatusUrl;

    @Value("${app.url.lark-forward:}")
    private String larkForwardUrl;

    @Value("${app.game-server.profile:}")
    private String profile;

    @Value("${app.game-server.jenkins-job:}")
    private String jenkinsJobUrl;

    @Value("${app.websocket.ignore-servers:}")
    private Set<Integer> ignoreServers;

    @Autowired
    private IGameServerService serverService;

    @Autowired
    private WebsocketService websocketService;

    @Async
    // 秒 分 时 日 月 星期 年份
    @Scheduled(cron = "0 0/10 * * * ?")
    public void checkServer() {
        if (!enable) {
            return;
        }

        log.info("start checkServer");
        List<GameServer> serverList = serverService.selectGameServerList();
        Map<Integer, WebsocketCheckResult> checkResultMap = new HashMap<>();
        for (GameServer server : serverList) {
            if (!server.skipCheck()) {
                WebsocketCheckResult result = websocketService.checkServer(server, testTimes);
                checkResultMap.put(server.getId(), result);
            }
        }

        Set<Integer> failedList = checkResultMap.values().stream()
                .filter(t -> t.getFailed() >= warningTimes)
                .map(WebsocketCheckResult::getServerId).collect(Collectors.toSet());

        // 忽略某些服务器id
        if (CollUtil.isNotEmpty(ignoreServers)) {
            ignoreServers.forEach(failedList::remove);
        }

        List<GameServerStatus> serverStatusList = new ArrayList<>();
        List<WebsocketCheckResult> resultList = new ArrayList<>();
        if (CollUtil.isNotEmpty(failedList)) {
            Map<Integer, String> responseMap = serverService.gameServerGet(failedList, serverStatusUrl, String.class);
            responseMap.forEach((key, value) -> {
                DataResponse<GameServerStatus> response = JSON.parseObject(value, RESPONSE_SERVER_STATUS);
                if (response != null) {
                    GameServerStatus serverStatus = response.getData();
                    if (serverStatus != null) {
                        serverStatus.setServerId(key);
                        serverStatusList.add(serverStatus);
                    }
                }
                resultList.add(checkResultMap.get(key));
            });

            ServerWarningData warningData = new ServerWarningData()
                    .setProfile(profile).setJenkinsJobUrl(jenkinsJobUrl)
                    .setServerIds(StrUtil.join(",", failedList))
                    .setResultList(resultList).setServerStatusList(serverStatusList);

            if (StrUtil.isNotEmpty(larkForwardUrl)) {
                String url = larkForwardUrl + "/lark/gameServer";
                String response = OkHttpHelper.post(url, warningData);
                log.info("request url:{}, response:{}", url, response);
            }
        }

        log.info("finish checkServer, failed:{}", failedList.size());
    }
}

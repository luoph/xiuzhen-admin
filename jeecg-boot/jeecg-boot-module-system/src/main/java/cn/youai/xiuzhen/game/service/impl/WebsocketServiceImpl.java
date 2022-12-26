package cn.youai.xiuzhen.game.service.impl;

import cn.youai.xiuzhen.game.entity.GameServer;
import cn.youai.xiuzhen.game.model.WebsocketCheckResult;
import cn.youai.xiuzhen.game.service.WebsocketService;
import lombok.extern.slf4j.Slf4j;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class WebsocketServiceImpl implements WebsocketService {

    @Value("${app.websocket.checkUrl-timeout:5}")
    private Integer timeout;

    @Value("${app.websocket.enable-ssl:false}")
    private boolean enableSSL;

    @Override
    public WebsocketCheckResult checkUrl(Integer serverId, String url, int times) {
        int failedTimes = 0;
        for (int i = 0; i < times; i++) {
            boolean success = checkWebsocket(url);
            if (!success) {
                failedTimes++;
            }
        }
        return new WebsocketCheckResult().setServerId(serverId).setUrl(url).setFailed(failedTimes).setSuccess(times - failedTimes);
    }

    @Override
    public WebsocketCheckResult checkServer(GameServer server, int times) {
        String url = (enableSSL ? "wss://" : "ws://") + server.getLoginUrl() + "/server";
        return checkUrl(server.getId(), url, times);
    }

    private boolean checkWebsocket(String url) {
        try {
            WebSocketClient client = new WebSocketClient(new URI(url)) {
                @Override
                public void onOpen(ServerHandshake handshakedata) {
                    log.info("onOpen {}", handshakedata);
                }

                @Override
                public void onMessage(String message) {
                    log.info("onMessage {}", message);
                }

                @Override
                public void onClose(int code, String reason, boolean remote) {
                    log.info("onClose code:{}, reason:{}, remote:{}", code, reason, remote);
                }

                @Override
                public void onError(Exception ex) {
                    log.error("onError error", ex);
                }
            };

            try {
                client.connectBlocking(timeout, TimeUnit.SECONDS);
                boolean isOpen = client.isOpen();
                client.close();
                return isOpen;
            } catch (InterruptedException e) {
                return false;
            }
        } catch (URISyntaxException e) {
            return false;
        }
    }

}

package cn.youai.xiuzhen.game.service;

import cn.youai.xiuzhen.game.entity.GameServer;
import cn.youai.xiuzhen.game.model.WebsocketCheckResult;

public interface WebsocketService {

    WebsocketCheckResult checkUrl(Integer serverId, String url, int times);

    WebsocketCheckResult checkServer(GameServer server, int times);

}

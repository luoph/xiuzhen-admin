package org.jeecg.modules.game.service.impl;

import cn.youai.commons.model.Response;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.common.okhttp.OkHttpHelper;
import org.jeecg.modules.game.entity.GameServer;
import org.jeecg.modules.game.mapper.GameServerMapper;
import org.jeecg.modules.game.service.IGameServerService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 游戏服配置
 * @date 2019-12-10
 */
@Service
public class GameServerServiceImpl extends ServiceImpl<GameServerMapper, GameServer> implements IGameServerService {

    @Resource
    private GameServerMapper gameServerMapper;

    @Override
    public Map<Long, Response> gameServerRequest(long[] serverIds, String requestUrl) {
        Map<Long, Response> responseMap = new HashMap<>();
        for (long serverId : serverIds) {
            GameServer gameServer = getById(serverId);
            try {
                Response response = JSON.parseObject(OkHttpHelper.get(gameServer.getGmUrl() + requestUrl), Response.class);
                responseMap.put(serverId, response);
            } catch (Exception e) {
                log.error("gameServerRequest error, serverId:" + serverId, e);
            }
        }
        return responseMap;
    }

}

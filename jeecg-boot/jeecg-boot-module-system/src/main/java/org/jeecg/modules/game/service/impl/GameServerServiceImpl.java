package org.jeecg.modules.game.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.youai.commons.model.Response;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.primitives.Ints;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.okhttp.OkHttpHelper;
import org.jeecg.modules.game.entity.GameServer;
import org.jeecg.modules.game.mapper.GameServerMapper;
import org.jeecg.modules.game.service.IGameServerService;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 游戏服配置
 * @date 2019-12-10
 */
@Service
public class GameServerServiceImpl extends ServiceImpl<GameServerMapper, GameServer> implements IGameServerService {

    @Override
    public Map<Integer, Response> gameServerGet(Collection<Integer> serverIds, String path) {
        Map<Integer, Response> responseMap = new HashMap<>(serverIds.size());
        for (Integer serverId : serverIds) {
            GameServer gameServer = getById(serverId);
            if (gameServer == null || StringUtils.contains(gameServer.getGmUrl(), "localhost")
                    || StringUtils.contains(gameServer.getGmUrl(), "127.0.0.1")) {
                continue;
            }

            try {
                Response response = JSON.parseObject(OkHttpHelper.get(gameServer.getGmUrl() + path), Response.class);
                responseMap.put(serverId, response);
            } catch (Exception e) {
                log.error("gameServerGet error, serverId:" + serverId + ", path:" + path, e);
            }
        }
        return responseMap;
    }

    @Override
    public Map<Integer, Response> gameServerGet(String serverIds, String path) {
        return gameServerGet(StrUtil.splitToInt(serverIds, ","), path);
    }

    @Override
    public Map<String, Response> gameServerGet(Collection<String> serverIds, String path, Map<String, Object> params) {
        Map<String, Response> responseMap = new HashMap<>(serverIds.size());
        for (String serverId : serverIds) {
            GameServer gameServer = getById(serverId);
            if (StringUtils.contains(gameServer.getGmUrl(), "localhost")
                    || StringUtils.contains(gameServer.getGmUrl(), "127.0.0.1")) {
                continue;
            }

            try {
                Response response = JSON.parseObject(OkHttpHelper.get(gameServer.getGmUrl() + path, params), Response.class);
                responseMap.put(serverId, response);
            } catch (Exception e) {
                log.error("gameServerGet error, serverId:" + serverId + ", path:" + path, e);
            }
        }
        return responseMap;
    }

    @Override
    public Map<Integer, Response> gameServerGet(int[] serverIds, String path) {
        return gameServerGet(Ints.asList(serverIds), path);
    }

    @Override
    public Map<Integer, Response> gameServerPost(Collection<Integer> serverIds, String path, JSONObject data) {
        Map<Integer, Response> responseMap = new HashMap<>(serverIds.size());
        for (Integer serverId : serverIds) {
            GameServer gameServer = getById(serverId);
            if (StringUtils.contains(gameServer.getGmUrl(), "localhost")
                    || StringUtils.contains(gameServer.getGmUrl(), "127.0.0.1")) {
                continue;
            }

            try {
                Response response = JSON.parseObject(OkHttpHelper.post(gameServer.getGmUrl() + path, data), Response.class);
                responseMap.put(serverId, response);
            } catch (Exception e) {
                log.error("gameServerPost error, serverId:" + serverId, e);
            }
        }
        return responseMap;
    }

    @Override
    public Map<Integer, Response> gameServerPost(int[] serverIds, String path, JSONObject data) {
        return gameServerPost(Ints.asList(serverIds), path, data);
    }

    @Override
    public Map<Integer, Response> gameServerPost(String serverIds, String path, JSONObject data) {
        return gameServerPost(StrUtil.splitToInt(serverIds, ","), path, data);
    }

}

package cn.youai.xiuzhen.game.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.youai.basics.model.Response;
import cn.youai.basics.utils.StringUtils;
import cn.youai.server.springboot.component.OkHttpHelper;
import cn.youai.server.utils.DateUtils;
import cn.youai.xiuzhen.game.constant.GameServerUtils;
import cn.youai.xiuzhen.game.entity.GameServer;
import cn.youai.xiuzhen.game.entity.MergeServerVO;
import cn.youai.xiuzhen.game.mapper.GameOrderMapper;
import cn.youai.xiuzhen.game.mapper.GameServerMapper;
import cn.youai.xiuzhen.game.service.IGameServerService;
import cn.youai.xiuzhen.stat.service.ILogAccountService;
import com.alibaba.fastjson2.JSON;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import okhttp3.Call;
import okhttp3.Callback;
import org.jeecg.modules.system.entity.SysUser;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 游戏服配置
 * @date 2019-12-10
 */
@Service
@DS("master")
public class GameServerServiceImpl extends ServiceImpl<GameServerMapper, GameServer> implements IGameServerService {

    @Value("${app.clean-cache-url}")
    private String cleanCacheUrl;

    @Resource
    private GameOrderMapper gameOrderMapper;

    @Autowired
    private ILogAccountService logAccountService;

    @Override
    @SuppressWarnings("DuplicatedCode")
    public void applyChange(GameServer entity) {
        GameServer gameServer = getById(entity.getId());
        if (gameServer == null) {
            return;
        }
        updateById(entity);
        List<GameServer> childList = selectGameServerByPid(CollUtil.newArrayList(entity.getId()));
        if (CollUtil.isEmpty(childList)) {
            return;
        }

        // 同步父节点的修改到子节点
        if (GameServerUtils.notEq(gameServer, entity, GameServer::getHost)) {
            GameServerUtils.apply(childList, entity, GameServer::getHost, GameServer::setHost);
        }
        if (GameServerUtils.notEq(gameServer, entity, GameServer::getLoginUrl)) {
            GameServerUtils.apply(childList, entity, GameServer::getLoginUrl, GameServer::setLoginUrl);
        }
        if (GameServerUtils.notEq(gameServer, entity, GameServer::getGmUrl)) {
            GameServerUtils.apply(childList, entity, GameServer::getGmUrl, GameServer::setGmUrl);
        }
        if (GameServerUtils.notEq(gameServer, entity, GameServer::getClientGm)) {
            GameServerUtils.apply(childList, entity, GameServer::getClientGm, GameServer::setClientGm);
        }
        if (GameServerUtils.notEq(gameServer, entity, GameServer::getRpcUrl)) {
            GameServerUtils.apply(childList, entity, GameServer::getRpcUrl, GameServer::setRpcUrl);
        }
        if (GameServerUtils.notEq(gameServer, entity, GameServer::getMysql)) {
            GameServerUtils.apply(childList, entity, GameServer::getMysql, GameServer::setMysql);
        }
        if (GameServerUtils.notEq(gameServer, entity, GameServer::getMongodb)) {
            GameServerUtils.apply(childList, entity, GameServer::getMongodb, GameServer::setMongodb);
        }
        if (GameServerUtils.notEq(gameServer, entity, GameServer::getIsMaintain)) {
            GameServerUtils.apply(childList, entity, GameServer::getIsMaintain, GameServer::setIsMaintain);
        }
    }

    @Override
    public List<GameServer> selectGameServerList() {
        return getBaseMapper().selectGameServerList(null);
    }

    @Override
    public List<GameServer> selectGameServerList(List<Integer> serverIds) {
        return getBaseMapper().selectGameServerList(serverIds);
    }

    @Override
    public List<GameServer> selectGameServerByPid(List<Integer> pids) {
        return getBaseMapper().selectGameServerByPid(pids);
    }

    @Override
    public List<GameServer> selectOnlineGameServerList() {
        return getBaseMapper().selectOnlineGameServerList();
    }

    @Override
    public List<GameServer> selectChannelServerList(String configAuth) {
        return getBaseMapper().selectChannelServerList(configAuth);
    }

    @Override
    public List<GameServer> selectChannelServerListByUser(SysUser user, String serverIds) {
        return getBaseMapper().selectChannelServerListByUser(user, serverIds);
    }

    @Override
    public Map<Integer, Response> cleanCache(Collection<Integer> serverIds, String cacheName) {
        return gameServerGet(serverIds, cleanCacheUrl + cacheName);
    }

    @Override
    public Map<Integer, Response> gameServerGet(Collection<Integer> serverIds, String path) {
        return gameServerGet(serverIds, path, Response.class);
    }

    @Override
    public <T> Map<Integer, T> gameServerGet(Collection<Integer> serverIds, String path, Class<T> clazz) {
        Map<Integer, T> responseMap = new HashMap<>(serverIds.size());
        CountDownLatch latch = new CountDownLatch(serverIds.size());
        for (Integer serverId : serverIds) {
            GameServer gameServer = getById(serverId);
            if (skipRequest(gameServer)) {
                latch.countDown();
                continue;
            }

            OkHttpHelper.getAsync(gameServer.getGmUrl() + path, new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    log.error("onlineNum onFailure", e);
                    latch.countDown();
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull okhttp3.Response response) throws IOException {
                    if (OkHttpHelper.isSuccess(response) && response.body() != null) {
                        try {
                            T rspObj = JSON.parseObject(response.body().string(), clazz);
                            responseMap.put(serverId, rspObj);
                        } catch (Exception e) {
                            log.error("gameServerGet error, serverId:" + serverId + ", path:" + path, e);
                        } finally {
                            response.body().close();
                        }
                    }
                    latch.countDown();
                }
            });
        }

        try {
            latch.await();
        } catch (InterruptedException e) {
            log.error("gameServerGet error, serverIds:" + serverIds + ", path:" + path, e);
        }
        return responseMap;
    }

    @Override
    public Map<Integer, Response> gameServerGet(String serverIds, String path) {
        return gameServerGet(StringUtils.split2Int(serverIds), path);
    }

    @Override
    public <T> Map<Integer, T> gameServerGet(String serverIds, String path, Class<T> clazz) {
        return gameServerGet(StringUtils.split2Int(serverIds), path, clazz);
    }

    @Override
    public Map<String, Response> gameServerGet(Collection<String> serverIds, String path, Map<String, Object> params) {
        return gameServerGet(serverIds, path, params, Response.class);
    }

    @Override
    public <T> Map<String, T> gameServerGet(Collection<String> serverIds, String path, Map<String, Object> params, Class<T> clazz) {
        Map<String, T> responseMap = new ConcurrentHashMap<>(serverIds.size());
        CountDownLatch latch = new CountDownLatch(serverIds.size());
        for (String serverId : serverIds) {
            GameServer gameServer = getById(serverId);
            if (skipRequest(gameServer)) {
                latch.countDown();
                continue;
            }

            // 异步
            OkHttpHelper.getAsync(gameServer.getGmUrl() + path, params, new Callback() {
                @Override
                public void onFailure(@NotNull Call call, IOException e) {
                    log.error("gameServerGet onFailure, url:" + gameServer.getGmUrl() + path, e);
                    latch.countDown();
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull okhttp3.Response response) throws IOException {
                    if (OkHttpHelper.isSuccess(response) && response.body() != null) {
                        try {
                            responseMap.put(serverId, JSON.parseObject(response.body().string(), clazz));
                        } finally {
                            response.body().close();
                        }
                    }
                    latch.countDown();
                }
            });
        }

        try {
            latch.await();
        } catch (InterruptedException e) {
            log.error("gameServerGet error", e);
        }
        return responseMap;
    }

    @Override
    public Map<Integer, Response> gameServerPost(Collection<Integer> serverIds, String path, Object data) {
        return gameServerPost(serverIds, path, data, Response.class);
    }

    @Override
    public <T> Map<Integer, T> gameServerPost(Collection<Integer> serverIds, String path, Object data, Class<T> clazz) {
        Map<Integer, T> responseMap = new HashMap<>(serverIds.size());
        for (Integer serverId : serverIds) {
            GameServer gameServer = getById(serverId);
            if (skipRequest(gameServer)) {
                continue;
            }

            try {
                T response = JSON.parseObject(OkHttpHelper.post(gameServer.getGmUrl() + path, data), clazz);
                responseMap.put(serverId, response);
            } catch (Exception e) {
                log.error("gameServerPost error, serverId:" + serverId, e);
            }
        }
        return responseMap;
    }

    @Override
    public void updateGameServerMaintain(Collection<GameServer> servers, int isMaintain) {
        for (GameServer server : servers) {
            server.setIsMaintain(isMaintain);
        }
        updateBatchById(servers);
    }

    @Override
    public Set<Integer> getOnlineServerIds() {
        return selectOnlineGameServerList().stream().map(GameServer::getId).collect(Collectors.toSet());
    }

    @Override
    public Set<Integer> getAllServerIds() {
        return list(Wrappers.<GameServer>lambdaQuery().select(GameServer::getId))
                .stream().map(GameServer::getId).collect(Collectors.toSet());
    }

    @Override
    public List<MergeServerVO> getMergeServerList(int days, int minAvgPlayers, double minAvgPayAmount) {
        Date endTime = DateUtils.endTimeOfDate(DateUtils.now());
        Date startTime = DateUtils.startTimeOfDate(DateUtils.addDays(endTime, -days + 1));
        Map<Integer, MergeServerVO> serverLoginNumMap = logAccountService.getServerLoginNum(startTime, endTime)
                .stream().collect(Collectors.toConcurrentMap(MergeServerVO::getServerId, Function.identity(), (key1, key2) -> key2));
        Map<Integer, MergeServerVO> serverPayAmountMap = gameOrderMapper.getGameOrderRangeDate(startTime, endTime)
                .stream().collect(Collectors.toConcurrentMap(MergeServerVO::getServerId, Function.identity(), (key1, key2) -> key2));

        Set<Integer> serverIds = getAllServerIds();
        Map<Integer, MergeServerVO> map = new HashMap<>(serverIds.size());
        serverIds.forEach(serverId -> {
            MergeServerVO serverLoginNum = serverLoginNumMap.get(serverId);
            MergeServerVO serverPayAmount = serverPayAmountMap.get(serverId);
            int avgPlayers = null != serverLoginNum ? serverLoginNum.getNum() / days : 0;
            double avgPayAmount = null != serverPayAmount ? serverPayAmount.getPayAmount().doubleValue() / days : 0;
            if (null == serverLoginNum || avgPlayers < minAvgPlayers || null == serverPayAmount || avgPayAmount < minAvgPayAmount) {
                map.put(serverId, new MergeServerVO().setServerId(serverId).setNum(avgPlayers).setPayAmount(BigDecimal.valueOf(avgPayAmount)));
            }
        });
        return new ArrayList<>(map.values());
    }

    private boolean skipRequest(GameServer gameServer) {
        return gameServer == null || gameServer.skipCheck();
    }
}

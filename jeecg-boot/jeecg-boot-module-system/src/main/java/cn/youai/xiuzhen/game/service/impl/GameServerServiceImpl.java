package cn.youai.xiuzhen.game.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.youai.basics.model.DataResponse;
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
import cn.youai.xiuzhen.utils.RequestUtils;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
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
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.*;
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

    private static final Type RESPONSE_ONLINE_NUM = new TypeReference<DataResponse<Integer>>() {
    }.getType();

    @Value("${app.clean-cache-url}")
    private String cleanCacheUrl;

    @Value("${app.online-num-url:/game/onlineNum}")
    private String onlineNumUrl;

    @Resource
    private GameOrderMapper gameOrderMapper;

    @Autowired
    private ILogAccountService logAccountService;

    @Override
    public List<Integer> selectRunningServerIds(Date date) {
        return getBaseMapper().selectRunningServerIds(date);
    }

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
        return getUrl(serverIds, cleanCacheUrl + cacheName);
    }

    @Override
    public Map<Integer, Response> getUrl(Collection<Integer> serverIds, String path) {
        return getUrl(serverIds, path, Response.class);
    }

    @Override
    public <T> Map<Integer, T> getUrl(Collection<Integer> serverIds, String path, Class<T> clazz) {
        return RequestUtils.batchGet(serverIds, path, this, GameServer::getGmUrl, GameServer::skipCheck, clazz);
    }

    @Override
    public Map<Integer, Response> getUrl(String serverIds, String path) {
        return getUrl(StringUtils.split2Int(serverIds), path);
    }

    @Override
    public <T> Map<Integer, T> getUrl(String serverIds, String path, Class<T> clazz) {
        return getUrl(StringUtils.split2Int(serverIds), path, clazz);
    }

    @Override
    public Map<Integer, Response> getUrl(Collection<Integer> serverIds, String path, Map<String, Object> params) {
        return getUrl(serverIds, path, params, Response.class);
    }

    @Override
    public <T> Map<Integer, T> getUrl(Collection<Integer> serverIds, String path, Map<String, Object> params, Class<T> clazz) {
        return RequestUtils.batchGet(serverIds, path, params, this, GameServer::getGmUrl, GameServer::skipCheck, clazz);
    }

    @Override
    public Map<Integer, Response> postUrl(Collection<Integer> serverIds, String path, Object data) {
        return postUrl(serverIds, path, data, Response.class);
    }

    @Override
    public <T> Map<Integer, T> postUrl(Collection<Integer> serverIds, String path, Object data, Class<T> clazz) {
        return RequestUtils.batchPost(serverIds, path, data, this, GameServer::getGmUrl, GameServer::skipCheck, clazz);
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
        List<GameServer> list = selectOnlineGameServerList();
        return CollUtil.isNotEmpty(list) ? list.stream().map(GameServer::getId).collect(Collectors.toSet()) : Collections.emptySet();
    }

    @Override
    public Set<Integer> getAllServerIds() {
        List<GameServer> list = list(Wrappers.<GameServer>lambdaQuery().select(GameServer::getId));
        return CollUtil.isNotEmpty(list) ? list.stream().map(GameServer::getId).collect(Collectors.toSet()) : Collections.emptySet();
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

    @Override
    public void updateOnlineNum(Collection<GameServer> servers) {
        if (CollUtil.isEmpty(servers)) {
            return;
        }

        CountDownLatch latch = new CountDownLatch(servers.size());
        for (GameServer record : servers) {
            if (GameServer.skipCallGm(record)) {
                record.setOnlineNum(0);
                latch.countDown();
                continue;
            }

            // 已废弃服务器不统计在线人数
            if (record.skipCheck()) {
                latch.countDown();
                continue;
            }

            OkHttpHelper.getAsync(record.getGmUrl() + onlineNumUrl, new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    log.error("onlineNum onFailure", e);
                    latch.countDown();
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull okhttp3.Response response) throws IOException {
                    if (OkHttpHelper.isSuccess(response) && response.body() != null) {
                        try {
                            DataResponse<Integer> rsp = JSON.parseObject(response.body().string(), RESPONSE_ONLINE_NUM);
                            if (rsp != null) {
                                record.setOnlineNum(rsp.getData());
                            }
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
            log.error("onlineNum error", e);
        }
    }

    @Override
    public void setChannelSimpleNameList(List<GameServer> list) {
        if (CollUtil.isEmpty(list)) {
            return;
        }

        List<GameServer> serverChannelNameList = getBaseMapper().selectChannelSimpleName(list.stream().map(GameServer::getId).collect(Collectors.toList()));
        Map<Integer, List<GameServer>> serverId2ChannelNameMap = CollUtil.isNotEmpty(serverChannelNameList) ? serverChannelNameList.stream().collect(Collectors.groupingBy(GameServer::getId)) : Collections.emptyMap();
        list.forEach(t -> {
            List<GameServer> channelSimpleNameList = serverId2ChannelNameMap.get(t.getId());
            if (CollUtil.isEmpty(channelSimpleNameList)) {
                return;
            }
            t.setChannelSimpleNameList(channelSimpleNameList.stream().map(GameServer::getChannel).collect(Collectors.toList()));
        });
    }
}

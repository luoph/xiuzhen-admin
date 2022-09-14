package cn.youai.xiuzhen.game.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.youai.basics.model.Response;
import cn.youai.basics.utils.StringUtils;
import cn.youai.server.springboot.component.OkHttpHelper;
import cn.youai.server.utils.DateUtils;
import cn.youai.xiuzhen.game.entity.GameServer;
import cn.youai.xiuzhen.game.entity.MergeServerVO;
import cn.youai.xiuzhen.game.mapper.GameOrderMapper;
import cn.youai.xiuzhen.game.mapper.GameServerMapper;
import cn.youai.xiuzhen.game.service.IGameServerService;
import cn.youai.xiuzhen.game.service.ILogAccountService;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import okhttp3.Call;
import okhttp3.Callback;
import org.springframework.beans.factory.annotation.Autowired;
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
public class GameServerServiceImpl extends ServiceImpl<GameServerMapper, GameServer> implements IGameServerService {

    @Resource
    private GameOrderMapper gameOrderMapper;

    @Autowired
    private ILogAccountService logAccountService;

    @Override
    public Map<Integer, Response> gameServerGet(Collection<Integer> serverIds, String path) {
        Map<Integer, Response> responseMap = new HashMap<>(serverIds.size());
        for (Integer serverId : serverIds) {
            GameServer gameServer = getById(serverId);
            if (gameServer == null || StrUtil.contains(gameServer.getGmUrl(), "localhost")
                    || StrUtil.contains(gameServer.getGmUrl(), "127.0.0.1")) {
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
        return gameServerGet(StringUtils.split2Int(serverIds), path);
    }

    @Override
    public Map<String, Response> gameServerGet(Collection<String> serverIds, String path, Map<String, Object> params) {
        Map<String, Response> responseMap = new ConcurrentHashMap<>(serverIds.size());
        CountDownLatch latch = new CountDownLatch(serverIds.size());
        for (String serverId : serverIds) {
            GameServer gameServer = getById(serverId);
            if (gameServer == null) {
                latch.countDown();
                continue;
            }
            if (StrUtil.contains(gameServer.getGmUrl(), "localhost")
                    || StrUtil.contains(gameServer.getGmUrl(), "127.0.0.1")) {
                latch.countDown();
                continue;
            }

            // 协程
//            new Fiber<>(() -> {
//                try {
//                    Response response = JSON.parseObject(OkHttpHelper.get(gameServer.getGmUrl() + path, params), Response.class);
//                    responseMap.put(serverId, response);
//                } catch (Exception e) {
//                    log.error("gameServerGet error, serverId:" + serverId + ", path:" + path, e);
//                }
//                latch.countDown();
//            }).start();

            // 异步
            OkHttpHelper.getAsync(gameServer.getGmUrl() + path, params, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    log.error("gameServerGet onFailure", e);
                    latch.countDown();
                }

                @Override
                public void onResponse(Call call, okhttp3.Response response) throws IOException {
                    if (OkHttpHelper.isSuccess(response)) {
                        responseMap.put(serverId, JSON.parseObject(response.body().string(), Response.class));
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
        Map<Integer, Response> responseMap = new HashMap<>(serverIds.size());
        for (Integer serverId : serverIds) {
            GameServer gameServer = getById(serverId);
            if (StrUtil.contains(gameServer.getGmUrl(), "localhost")
                    || StrUtil.contains(gameServer.getGmUrl(), "127.0.0.1")) {
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
    public void updateGameServerMaintain(List<Integer> serverIds, int isMaintain) {
        getBaseMapper().updateGameServerMaintain(serverIds, isMaintain);
    }

    @Override
    public Set<Integer> getServerIds() {
        return list(Wrappers.<GameServer>lambdaQuery().select(GameServer::getId)).stream().map(GameServer::getId).collect(Collectors.toSet());
    }

    @Override
    public List<MergeServerVO> getMergeServerList(int days, int minAvgPlayers, double minAvgPayAmount) {
        Date endTime = DateUtils.endTimeOfDate(DateUtils.now());
        Date startTime = DateUtils.startTimeOfDate(DateUtils.addDays(endTime, -days + 1));
        Map<Integer, MergeServerVO> serverLoginNumMap = logAccountService.getServerLoginNum(startTime, endTime)
                .stream().collect(Collectors.toConcurrentMap(MergeServerVO::getServerId, Function.identity(), (key1, key2) -> key2));
        Map<Integer, MergeServerVO> serverPayAmountMap = gameOrderMapper.getGameOrderRangeDate(startTime, endTime)
                .stream().collect(Collectors.toConcurrentMap(MergeServerVO::getServerId, Function.identity(), (key1, key2) -> key2));

        Set<Integer> serverIds = getServerIds();
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
}

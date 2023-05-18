/*
 * create by mybatis-plus-generator  https://github.com/xiweile
 */
package cn.youai.xiuzhen.game.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.youai.basics.model.Response;
import cn.youai.server.springboot.component.OkHttpHelper;
import cn.youai.xiuzhen.game.entity.GameServerGroup;
import cn.youai.xiuzhen.game.entity.GameServerGroupItem;
import cn.youai.xiuzhen.game.mapper.GameServerGroupMapper;
import cn.youai.xiuzhen.game.service.IGameServerGroupItemService;
import cn.youai.xiuzhen.game.service.IGameServerGroupService;
import com.alibaba.fastjson2.JSON;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import okhttp3.Call;
import okhttp3.Callback;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;

/**
 * <p>
 * 游戏渠道服配置 服务实现类
 * </p>
 *
 * @author luopeihuan
 * @since 2021-08-03
 */
@Service
@DS("master")
public class GameServerGroupServiceImpl extends ServiceImpl<GameServerGroupMapper, GameServerGroup> implements IGameServerGroupService {

    @Autowired
    private IGameServerGroupItemService gameServerGroupItemService;

    @Override
    public List<GameServerGroup> getGameServerGroupList(Collection<Integer> serverIds) {
        if (CollUtil.isEmpty(serverIds)) {
            return Collections.emptyList();
        }

        List<GameServerGroupItem> gameServerGroupItemList = gameServerGroupItemService.list(Wrappers.<GameServerGroupItem>lambdaQuery()
                .in(GameServerGroupItem::getServerId, serverIds).groupBy(GameServerGroupItem::getGroupId));
        return !gameServerGroupItemList.isEmpty() ? list(Wrappers.<GameServerGroup>lambdaQuery()
                .in(GameServerGroup::getId, gameServerGroupItemList.stream().map(GameServerGroupItem::getGroupId).collect(Collectors.toSet()))) : Collections.emptyList();
    }

    @Override
    public Map<Long, Response> gameServerGroupGet(Collection<GameServerGroup> gameServerGroups, String path, Map<String, Object> params) {
        if (CollUtil.isEmpty(gameServerGroups)) {
            return Collections.emptyMap();
        }

        Map<Long, Response> responseMap = new ConcurrentHashMap<>(gameServerGroups.size());
        CountDownLatch latch = new CountDownLatch(gameServerGroups.size());
        for (GameServerGroup gameServerGroup : gameServerGroups) {
            if (null == gameServerGroup || StringUtils.contains(gameServerGroup.getGmUrl(), "localhost") || StringUtils.contains(gameServerGroup.getGmUrl(), "127.0.0.1")) {
                continue;
            }

            // 异步
            OkHttpHelper.getAsync(gameServerGroup.getGmUrl() + path, params, new Callback() {
                @Override
                public void onFailure(@NotNull Call call, IOException e) {
                    log.error("gameServerGroupGet onFailure", e);
                    latch.countDown();
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull okhttp3.Response response) throws IOException {
                    if (OkHttpHelper.isSuccess(response) && response.body() != null) {
                        try {
                            responseMap.put(gameServerGroup.getId(), JSON.parseObject(response.body().string(), Response.class));
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
            log.error("gameServerGroupGet error", e);
        }
        return responseMap;
    }

    @Override
    public Map<Long, Response> gameServerGroupGetByServerIds(Collection<Integer> serverIds, String path, Map<String, Object> params) {
        List<GameServerGroup> gameServerGroupList = getGameServerGroupList(serverIds);
        return gameServerGroupGet(gameServerGroupList, path, params);
    }

}

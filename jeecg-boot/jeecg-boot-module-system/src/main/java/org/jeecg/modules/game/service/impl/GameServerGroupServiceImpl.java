/*
 * create by mybatis-plus-generator  https://github.com/xiweile
 */
package org.jeecg.modules.game.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.youai.commons.model.Response;
import cn.youai.server.springboot.component.OkHttpHelper;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.modules.game.entity.GameServerGroup;
import org.jeecg.modules.game.entity.GameServerGroupItem;
import org.jeecg.modules.game.mapper.GameServerGroupMapper;
import org.jeecg.modules.game.service.IGameServerGroupItemService;
import org.jeecg.modules.game.service.IGameServerGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
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
        Map<Long, Response> responseMap = new HashMap<>(gameServerGroups.size());
        for (GameServerGroup gameServerGroup : gameServerGroups) {
            if (null == gameServerGroup || StringUtils.contains(gameServerGroup.getGmUrl(), "localhost") || StringUtils.contains(gameServerGroup.getGmUrl(), "127.0.0.1")) {
                continue;
            }

            try {
                Response response = JSON.parseObject(OkHttpHelper.get(gameServerGroup.getGmUrl() + path, params), Response.class);
                responseMap.put(gameServerGroup.getId(), response);
            } catch (Exception e) {
                log.error("gameServerGet error, serverId:" + gameServerGroup + ", path:" + path, e);
            }
        }
        return responseMap;
    }

    @Override
    public Map<Long, Response> gameServerGroupGetByServerIds(Collection<Integer> serverIds, String path, Map<String, Object> params) {
        List<GameServerGroup> gameServerGroupList = getGameServerGroupList(serverIds);
        return gameServerGroupGet(gameServerGroupList, path, params);
    }

}

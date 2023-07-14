/*
 * create by mybatis-plus-generator  https://github.com/xiweile
 */
package cn.youai.xiuzhen.game.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.youai.basics.model.Response;
import cn.youai.xiuzhen.game.entity.GameServerGroup;
import cn.youai.xiuzhen.game.mapper.GameServerGroupMapper;
import cn.youai.xiuzhen.game.service.IGameServerGroupService;
import cn.youai.xiuzhen.utils.RequestUtils;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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
@DS("master")
public class GameServerGroupServiceImpl extends ServiceImpl<GameServerGroupMapper, GameServerGroup> implements IGameServerGroupService {

    @Override
    public List<GameServerGroup> selectServerGroupList(Collection<Integer> serverIds) {
        if (CollUtil.isEmpty(serverIds)) {
            return Collections.emptyList();
        }

        return getBaseMapper().selectServerGroupList(serverIds);
    }

    @Override
    public Map<Integer, Response> requestGroupGmByServerIds(Collection<Integer> serverIds, String path, Map<String, Object> params) {
        List<GameServerGroup> list = selectServerGroupList(serverIds);
        Set<Integer> ids = CollUtil.isNotEmpty(list) ? list.stream().map(GameServerGroup::getId).collect(Collectors.toSet()) : CollUtil.newHashSet();
        return getUrl(ids, path, params);
    }

    @Override
    public <T> Map<Integer, T> getUrl(Collection<Integer> serverIds, String path, Class<T> clazz) {
        return RequestUtils.batchGet(serverIds, path, this, GameServerGroup::getGmUrl, GameServerGroup::skipCheck, clazz);
    }

    @Override
    public Map<Integer, Response> getUrl(Collection<Integer> serverIds, String path, Map<String, Object> params) {
        return getUrl(serverIds, path, params, Response.class);
    }

    @Override
    public <T> Map<Integer, T> getUrl(Collection<Integer> serverIds, String path, Map<String, Object> params, Class<T> clazz) {
        return RequestUtils.batchGet(serverIds, path, params, this, GameServerGroup::getGmUrl, GameServerGroup::skipCheck, clazz);
    }

}

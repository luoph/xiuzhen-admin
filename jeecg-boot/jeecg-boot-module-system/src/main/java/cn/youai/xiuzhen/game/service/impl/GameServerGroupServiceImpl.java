/*
 * create by mybatis-plus-generator  https://github.com/xiweile
 */
package cn.youai.xiuzhen.game.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.youai.basics.model.DateRange;
import cn.youai.basics.model.Response;
import cn.youai.xiuzhen.game.entity.GameServerGroup;
import cn.youai.xiuzhen.game.mapper.GameServerGroupMapper;
import cn.youai.xiuzhen.game.service.IGameServerGroupService;
import cn.youai.xiuzhen.utils.RequestUtils;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.*;

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
    public IPage<GameServerGroup> queryList(Page<?> page,
                                            GameServerGroup entity,
                                            DateRange createTimeRange,
                                            DateRange crossSettleTimeRange) {
        return getBaseMapper().queryList(page, entity, createTimeRange, crossSettleTimeRange);
    }

    @Override
    public List<GameServerGroup> selectByServerId(Collection<Integer> serverIds) {
        if (CollUtil.isEmpty(serverIds)) {
            return Collections.emptyList();
        }

        return getBaseMapper().selectByServerId(serverIds);
    }

    @Override
    public Map<Integer, Response> requestGroupGmByServerIds(Collection<Integer> serverIds, String path, Map<String, Object> params) {
        List<GameServerGroup> list = selectByServerId(serverIds);
        Set<Integer> ids = new HashSet<>();
        for (GameServerGroup entity : list) {
            if (entity.getId() == null) {
                log.error("group config error, serverIds:" + entity.getServerIds(), new RuntimeException("requestGroupGmByServerIds error, serverIds:" + serverIds + ", path:" + path));
            } else {
                ids.add(entity.getId());
            }
        }
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

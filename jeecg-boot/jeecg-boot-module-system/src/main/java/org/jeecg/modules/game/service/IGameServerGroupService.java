/*
 * create by mybatis-plus-generator  https://github.com/xiweile
 */
package org.jeecg.modules.game.service;

import cn.youai.basics.model.Response;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.game.entity.GameServerGroup;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 游戏渠道服配置 服务类
 * </p>
 *
 * @author luopeihuan
 * @since 2021-08-03
 */
public interface IGameServerGroupService extends IService<GameServerGroup> {

    List<GameServerGroup> getGameServerGroupList(Collection<Integer> serverIds);

    Map<Long, Response> gameServerGroupGet(Collection<GameServerGroup> gameServerGroups, String path, Map<String, Object> params);

    Map<Long, Response> gameServerGroupGetByServerIds(Collection<Integer> serverIds, String path, Map<String, Object> params);
}

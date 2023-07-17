/*
 * create by mybatis-plus-generator  https://github.com/xiweile
 */
package cn.youai.xiuzhen.game.service;

import cn.youai.basics.model.DateRange;
import cn.youai.basics.model.Response;
import cn.youai.xiuzhen.game.entity.GameServerGroup;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

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

    IPage<GameServerGroup> queryList(Page<?> page,
                                     GameServerGroup entity,
                                     DateRange createTimeRange,
                                     DateRange crossSettleTimeRange);

    List<GameServerGroup> selectByServerId(Collection<Integer> serverIds);

    Map<Integer, Response> requestGroupGmByServerIds(Collection<Integer> serverIds, String path, Map<String, Object> params);

    <T> Map<Integer, T> getUrl(Collection<Integer> serverIds, String path, Class<T> clazz);

    Map<Integer, Response> getUrl(Collection<Integer> serverIds, String path, Map<String, Object> params);

    <T> Map<Integer, T> getUrl(Collection<Integer> serverIds, String path, Map<String, Object> params, Class<T> clazz);

}

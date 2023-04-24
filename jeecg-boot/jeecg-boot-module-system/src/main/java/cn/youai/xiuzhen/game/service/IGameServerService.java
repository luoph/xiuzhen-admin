package cn.youai.xiuzhen.game.service;

import cn.youai.basics.model.Response;
import cn.youai.xiuzhen.game.entity.GameServer;
import cn.youai.xiuzhen.game.entity.MergeServerVO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 游戏服配置
 * @date 2019-12-10
 */
public interface IGameServerService extends IService<GameServer> {

    /**
     * 获取服务器列表
     *
     * @return {@linkplain cn.youai.xiuzhen.game.entity.GameServer}列表
     */
    List<GameServer> selectGameServerList();

    /**
     * 查找已关联渠道的游戏服
     */
    List<GameServer> selectChannelServerList(String configAuth);

    /**
     * 批量请求 清除缓存接口
     *
     * @param serverIds 服务器 id
     * @param cacheName 缓存名字
     * @return 响应列表
     */
    Map<Integer, Response> cleanCache(Collection<Integer> serverIds, String cacheName);

    /**
     * 批量请求 http 接口
     *
     * @param serverIds 服务器 id
     * @param path      请求地址
     * @return 响应列表
     */
    Map<Integer, Response> gameServerGet(Collection<Integer> serverIds, String path);

    <T> Map<Integer, T> gameServerGet(Collection<Integer> serverIds, String path, Class<T> clazz);

    /**
     * 批量请求 http 接口
     *
     * @param serverIds 服务器 id
     * @param path      请求地址
     * @return 响应列表
     */
    Map<Integer, Response> gameServerGet(String serverIds, String path);

    <T> Map<Integer, T> gameServerGet(String serverIds, String path, Class<T> clazz);

    /**
     * 批量请求 http 接口
     *
     * @param serverIds 服务器 id
     * @param path      请求地址
     * @param params    请求参数
     * @return 响应列表
     */
    Map<String, Response> gameServerGet(Collection<String> serverIds, String path, Map<String, Object> params);

    <T> Map<String, T> gameServerGet(Collection<String> serverIds, String path, Map<String, Object> params, Class<T> clazz);

    /**
     * 批量请求 http 接口
     *
     * @param serverIds 服务器 id
     * @param path      请求地址
     * @param data      请求数据
     * @return 响应列表
     */
    Map<Integer, Response> gameServerPost(Collection<Integer> serverIds, String path, Object data);

    <T> Map<Integer, T> gameServerPost(Collection<Integer> serverIds, String path, Object data, Class<T> clazz);

    void updateGameServerMaintain(List<Integer> serverIds, int isMaintain);

    Set<Integer> getAvailableServerIds();

    Set<Integer> getAllServerIds();

    List<MergeServerVO> getMergeServerList(int days, int minAvgPlayers, double minAvgPayAmount);
}

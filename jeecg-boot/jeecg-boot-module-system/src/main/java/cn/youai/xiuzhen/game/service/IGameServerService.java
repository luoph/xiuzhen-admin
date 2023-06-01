package cn.youai.xiuzhen.game.service;

import cn.youai.basics.model.Response;
import cn.youai.xiuzhen.game.entity.GameServer;
import cn.youai.xiuzhen.game.entity.MergeServerVO;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.system.entity.SysUser;

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

    void applyChange(GameServer entity);

    List<GameServer> selectGameServerList(List<Integer> serverIds);

    List<GameServer> selectGameServerByPid(List<Integer> pids);

    List<GameServer> selectOnlineGameServerList();

    /**
     * 查找已关联渠道的游戏服
     */
    List<GameServer> selectChannelServerList(String configAuth);

    List<GameServer> selectChannelServerListByUser(SysUser user, String serverIds);

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

    void updateGameServerMaintain(Collection<GameServer> servers, int isMaintain);

    Set<Integer> getOnlineServerIds();

    Set<Integer> getAllServerIds();

    List<MergeServerVO> getMergeServerList(int days, int minAvgPlayers, double minAvgPayAmount);

    void updateOnlineNum(Collection<GameServer> servers);

    void setChannelSimpleNameList(List<GameServer> gameServerList);
}

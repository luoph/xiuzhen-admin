package org.jeecg.modules.game.service;

import cn.youai.basics.model.Response;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.game.entity.GameServer;
import org.jeecg.modules.player.entity.MergeServerVO;

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
     * 批量请求 http 接口
     *
     * @param serverIds 服务器 id
     * @param path      请求地址
     * @return 响应列表
     */
    Map<Integer, Response> gameServerGet(Collection<Integer> serverIds, String path);

    /**
     * 批量请求 http 接口
     *
     * @param serverIds 服务器 id
     * @param path      请求地址
     * @return 响应列表
     */
    Map<Integer, Response> gameServerGet(String serverIds, String path);

    /**
     * 批量请求 http 接口
     *
     * @param serverIds 服务器 id
     * @param path      请求地址
     * @param params    请求参数
     * @return 响应列表
     */
    Map<String, Response> gameServerGet(Collection<String> serverIds, String path, Map<String, Object> params);

    /**
     * 批量请求 http 接口
     *
     * @param serverIds 服务器 id
     * @param path      请求地址
     * @param data      请求数据
     * @return 响应列表
     */
    Map<Integer, Response> gameServerPost(Collection<Integer> serverIds, String path, Object data);

    void updateGameServerMaintain(List<Integer> serverIds, int isMaintain);

    Set<Integer> getServerIds();

    List<MergeServerVO> getMergeServerList(int days, int minAvgPlayers, double minAvgPayAmount);
}

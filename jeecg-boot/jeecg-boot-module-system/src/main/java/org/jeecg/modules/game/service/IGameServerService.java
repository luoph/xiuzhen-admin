package org.jeecg.modules.game.service;

import cn.youai.commons.model.Response;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.game.entity.GameServer;

import java.util.Map;

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
     * @param serverIds  服务器 id
     * @param requestUrl 请求地址
     * @return 响应列表
     */
    Map<Long, Response> gameServerRequest(long[] serverIds, String requestUrl);
}

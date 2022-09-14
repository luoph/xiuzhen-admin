package cn.youai.xiuzhen.game.service;

import cn.youai.xiuzhen.game.entity.GameChannelServer;
import cn.youai.xiuzhen.game.entity.GameServerVO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 游戏渠道服配置
 * @date 2019-12-11
 */
public interface IGameChannelServerService extends IService<GameChannelServer> {
    /**
     * 通过渠道id查询对应的服务器
     *
     * @param channelId 渠道id
     * @return 区服列表
     */
    List<GameServerVO> selectServerListByChannelId(int channelId);

    /**
     * 渠道和服务器绑定关系验证
     *
     * @param channelId 渠道id
     * @param serverId  服务器id
     */
    boolean isValidChannelWithServer(int channelId, int serverId);
}

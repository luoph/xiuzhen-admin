package cn.youai.xiuzhen.game.service;

import cn.youai.xiuzhen.game.entity.GameChannel;
import cn.youai.xiuzhen.game.entity.GameServerVO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 游戏渠道
 * @date 2019-12-11
 */
public interface IGameChannelService extends IService<GameChannel> {

    GameChannel selectChannel(String channel);

    /**
     * 查询某个渠道的服务器
     *
     * @param channelId 渠道 id
     * @return 服务器信息
     */
    List<GameServerVO> selectChannelServerList(int channelId);

    /**
     * 通过渠道id查询渠道名称
     *
     * @param channelId 渠道 id
     * @return 渠道名称
     */
    String queryChannelNameById(int channelId);

    /**
     * 刷新渠道区服配置
     */
    void updateAllChannelConfig();

    /**
     * 刷新指定渠道的区服配置
     *
     * @param channelId 渠道id
     */
    void updateChannelConfig(int channelId);

    /**
     * 刷新ip白名单
     */
    void updateIpWhiteListConfig();
}

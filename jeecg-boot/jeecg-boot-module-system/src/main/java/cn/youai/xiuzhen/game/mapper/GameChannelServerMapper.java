package cn.youai.xiuzhen.game.mapper;

import cn.youai.xiuzhen.game.entity.*;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 游戏渠道服配置
 * @date 2019-12-11
 */
public interface GameChannelServerMapper extends BaseMapper<GameChannelServer> {

    /**
     * 查询指定渠道的服务器列表
     *
     * @param channelId 渠道id
     * @return 游戏服列表
     */
    List<GameServerVO> selectServerListByChannelId(@Param("channelId") int channelId);

    List<GameChannel> selectChannelList(@Param("configAuth") String configAuth);

    List<GameSdkChannel> selectSdkChannelList(@Param("channels") Collection<String> channels);

    List<GameServer> selectChannelServerList(@Param("channels") Collection<String> channels);

    List<GameServerVO> filterServerList(@Param("channel") String channel, @Param("serverIds") List<Integer> serverIds);

    List<GameServer> selectServerListByChannel(@Param("channel") String channel, @Param("serverId") Integer serverId);

}

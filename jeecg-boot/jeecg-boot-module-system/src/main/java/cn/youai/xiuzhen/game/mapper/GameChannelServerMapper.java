package cn.youai.xiuzhen.game.mapper;

import cn.youai.xiuzhen.game.entity.GameChannelServer;
import cn.youai.xiuzhen.game.entity.GameServerVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

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

    /**
     * 查询指定渠道的服务器列表
     *
     * @param channel 渠道
     * @return 游戏服列表
     */
    List<GameServerVO> selectServerList(@Param("channel") String channel);

}

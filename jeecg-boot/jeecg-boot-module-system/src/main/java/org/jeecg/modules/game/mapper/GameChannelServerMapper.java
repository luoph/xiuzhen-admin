package org.jeecg.modules.game.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.game.entity.GameChannelServer;
import org.jeecg.modules.game.entity.GameServerVO;

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

}

package org.jeecg.modules.game.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.game.entity.GameChannelServer;
import org.jeecg.modules.game.entity.GameServer;

import java.util.List;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 游戏渠道服配置
 * @date 2019-12-11
 */
public interface GameChannelServerMapper extends BaseMapper<GameChannelServer> {
    List<GameServer> gameServerByChannelId(@Param("channelId") int channelId);
}

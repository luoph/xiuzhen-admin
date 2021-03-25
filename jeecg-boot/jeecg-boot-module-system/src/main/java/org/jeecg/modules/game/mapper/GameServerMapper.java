package org.jeecg.modules.game.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.game.entity.GameServer;

import java.util.List;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 游戏服配置
 * @date 2019-12-10
 */
public interface GameServerMapper extends BaseMapper<GameServer> {

    /**
     * 更新游戏服维护状态
     *
     * @param serverIdList 游戏服id
     * @param isMaintain   是否维护
     */
    void updateGameServerMaintain(@Param("serverIdList") List<Integer> serverIdList, @Param("isMaintain") int isMaintain);

}

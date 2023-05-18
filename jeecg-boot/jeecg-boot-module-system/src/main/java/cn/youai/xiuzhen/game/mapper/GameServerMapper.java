package cn.youai.xiuzhen.game.mapper;

import cn.youai.xiuzhen.game.entity.GameServer;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.system.entity.SysUser;

import java.util.List;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 游戏服配置
 * @date 2019-12-10
 */
public interface GameServerMapper extends BaseMapper<GameServer> {

    /**
     * 查询所有区服
     *
     * @return 区服列表
     */
    List<GameServer> selectGameServerList(@Param("configAuth") List<Integer> serverIds);

    /**
     * 查询线上区服（排除已下线、已合并的区服）
     */
    List<GameServer> selectOnlineGameServerList();

    List<GameServer> selectGameServerByPid(@Param("pids") List<Integer> pids);

    /**
     * 查找已关联到渠道的游戏服
     */
    List<GameServer> selectChannelServerList(@Param("configAuth") String configAuth);

    List<GameServer> selectChannelServerListByUser(@Param("user") SysUser user, @Param("serverIds") String serverIds);

}

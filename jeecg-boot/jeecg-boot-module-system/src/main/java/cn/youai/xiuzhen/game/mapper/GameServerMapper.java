package cn.youai.xiuzhen.game.mapper;

import cn.youai.xiuzhen.game.entity.GameServer;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 游戏服配置
 * @date 2019-12-10
 */
public interface GameServerMapper extends BaseMapper<GameServer> {

    /**
     * 查询区服
     *
     * @param id 游戏服id
     * @return GameServer
     */
    GameServer selectGameServerById(@Param("id") int id);

    /**
     * 查询所有区服
     *
     * @return 区服列表
     */
    List<GameServer> selectGameServerList();

    /**
     * 查询指定分组的所有区服
     *
     * @param groupId 分组id
     * @return 区服列表
     */
    List<GameServer> selectGameServerByGroupId(@Param("groupId") long groupId);

    /**
     * 更新游戏服维护状态
     *
     * @param serverIdList 游戏服id
     * @param isMaintain   是否维护
     */
    void updateGameServerMaintain(@Param("serverIdList") List<Integer> serverIdList, @Param("isMaintain") int isMaintain);

}

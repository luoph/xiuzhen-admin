package cn.youai.xiuzhen.game.mapper;

import cn.youai.xiuzhen.game.entity.LogAccount;
import cn.youai.xiuzhen.game.entity.LogPlayer;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * @author huli
 * @Description: PlayMethodsTakePartInMapper
 * @date 2021/1/11 14:37
 */

public interface PlayMethodsTakePartInMapper {

    /**
     * 查询玩家日志
     *
     * @param type            日志记录类型
     * @param createDateBegin 开始时间
     * @param createDateEnd   结束时间
     * @param serverId        服务器id
     * @return List<Map < String, Object>>
     */
    List<LogPlayer> conditionSelectPlayerLog(@Param("type") String type,
                                             @Param("createDateBegin") Date createDateBegin,
                                             @Param("createDateEnd") Date createDateEnd,
                                             @Param("serverId") int serverId);

    /**
     * 查询符合等级的用户登录信息
     *
     * @param grade    等级
     * @param serverId 服务器名称
     * @return List<Map < String, Object>>
     */
    List<LogAccount> selectPlayLoginInfo(@Param("grade") int grade, @Param("serverId") int serverId);
}
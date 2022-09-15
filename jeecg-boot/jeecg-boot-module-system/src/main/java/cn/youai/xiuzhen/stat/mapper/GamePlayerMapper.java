/*
 * create by mybatis-plus-generator  https://github.com/xiweile
 */
package cn.youai.xiuzhen.stat.mapper;

import cn.youai.entities.GamePlayer;
import cn.youai.xiuzhen.stat.entity.PlayerBehavior;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 玩家表 Mapper 接口
 * </p>
 *
 * @author buliangliang
 * @since 2021-12-07
 */
public interface GamePlayerMapper extends BaseMapper<GamePlayer> {

    List<GamePlayer> selectPlayerByName(@Param("nickname") String nickname);

    List<PlayerBehavior> queryPlayerBehavior(@Param("rangeDateBeginTime") Date rangeDateBeginTime,
                                             @Param("rangeDateEndTime") Date rangeDateEndTime,
                                             @Param("playerId") Long playerId,
                                             @Param("logTable") String logTable);
}

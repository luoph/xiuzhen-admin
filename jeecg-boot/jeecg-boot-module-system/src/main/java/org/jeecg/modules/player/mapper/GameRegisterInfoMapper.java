package org.jeecg.modules.player.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.player.entity.GameRegisterInfo;
import org.jeecg.modules.player.entity.PlayerBehavior;

import java.util.Date;
import java.util.List;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 玩家注册信息
 * @date 2020-01-04
 */
public interface GameRegisterInfoMapper extends BaseMapper<GameRegisterInfo> {

    List<GameRegisterInfo> queryLoginList(@Param("rangeDateBeginTime") Date rangeDateBeginTime,
                                          @Param("rangeDateEndTime") Date rangeDateEndTime,
                                          @Param("playerId") Long playerId);


    String queryPlayerIp(@Param("playerId") Long playerId,
                         @Param("createDate") Date createDate,
                         @Param("logTable") String logTable);

    List<PlayerBehavior> selectBehaviorCount(@Param("serverId") Integer serverId, @Param("nickname") String nickname, @Param("playerId") Long playerId, @Param("start") Date start, @Param("end") Date end);
}

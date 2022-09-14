package org.jeecg.modules.game.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.game.entity.GameRegisterInfo;

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

}

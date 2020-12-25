package org.jeecg.modules.game.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.game.entity.GameOnlineNum;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 在线情况
 * @date 2020-10-15
 */
public interface GameOnlineNumMapper extends BaseMapper<GameOnlineNum> {

    List<GameOnlineNum> queryGameOnlineNumByRangDate(@Param("rangeDateBeginTime") Date rangeDateBeginTime,
                                                     @Param("rangeDateEndTime") Date rangeDateEndTime,
                                                     @Param("serverId") Integer serverId,
                                                     @Param("channel") String channel);

    List<GameOnlineNum> queryGameOnlineCollectByRangDate(@Param("rangeDateBeginTime") Date rangeDateBeginTime,
                                                         @Param("rangeDateEndTime") Date rangeDateEndTime,
                                                         @Param("serverId") Integer serverId,
                                                         @Param("channel") String channel);

    BigDecimal queryDau(@Param("getTime") Date getTime,
                        @Param("logTable") String logTable);
}

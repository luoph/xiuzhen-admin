package cn.youai.xiuzhen.game.mapper;

import cn.youai.xiuzhen.game.entity.GameOnlineNum;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

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
}

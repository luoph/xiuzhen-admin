package cn.youai.xiuzhen.game.mapper;

import cn.youai.xiuzhen.game.entity.GameRegisterInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 玩家注册信息
 * @date 2020-01-04
 */
public interface GameRegisterInfoMapper extends BaseMapper<GameRegisterInfo> {

    List<GameRegisterInfo> queryLoginList(@Param("serverId") int serverId,
                                          @Param("playerId") long playerId,
                                          @Param("startTime") Date startTime,
                                          @Param("endTime") Date endTime);

}

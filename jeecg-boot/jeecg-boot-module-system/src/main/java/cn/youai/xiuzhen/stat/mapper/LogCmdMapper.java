package cn.youai.xiuzhen.stat.mapper;

import cn.youai.log.LogCmd;
import cn.youai.xiuzhen.game.entity.GameStatCmd;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * @Description: 接口日志
 * @Author: jeecg-boot
 * @Date: 2022-12-15
 * @Version: V1.0
 */
public interface LogCmdMapper extends BaseMapper<LogCmd> {

    List<GameStatCmd> selectCmdList(@Param("serverId") int serverId,
                                    @Param("date") Date date,
                                    @Param("startTime") Date startTime,
                                    @Param("endTime") Date endTime,
                                    @Param("costTime") int costTime);

}

package cn.youai.xiuzhen.stat.mapper;

import cn.youai.xiuzhen.stat.entity.GameStatArpu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 数据报表
 * @date 2020-10-10
 */
public interface GameStatArpuMapper extends BaseMapper<GameStatArpu> {

    /**
     * 统计ARPU
     */
    GameStatArpu queryServerArpu(@Param("serverId") int serverId,
                                 @Param("countDate") Date countDate);

    GameStatArpu queryChannelArpu(@Param("channel") String channel,
                                  @Param("countDate") Date countDate);

}

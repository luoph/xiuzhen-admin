package cn.youai.xiuzhen.stat.mapper;

import cn.youai.entities.GamePlayer;
import cn.youai.xiuzhen.stat.entity.PayUserRank;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 充值用户排行数据统计
 * @date 2020-10-09
 */
public interface PayUserRankMapper extends BaseMapper<PayUserRank> {

    List<PayUserRank> queryUserRankByDateRange(@Param("payTimeBeginDate") Date payTimeBeginDate,
                                               @Param("payTimeEndDate") Date payTimeEndDate,
                                               @Param("serverId") Integer serverId,
                                               @Param("channel") String channel);

    List<PayUserRank> queryPayRankByDateRange(@Param("rangeDateBeginTime") Date rangeDateBeginTime,
                                              @Param("rangeDateEndTime") Date rangeDateEndTime,
                                              @Param("serverId") Integer serverId,
                                              @Param("channel") String channel);

    Date getPlayerLastLoginTime(@Param("playerId") Long playerId);

    List<GamePlayer> getPlayerLastLoginAndRegisterTime(@Param("serverId") int serverId, @Param("logTable") String logTable);
}

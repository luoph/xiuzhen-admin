/*
 * create by mybatis-plus-generator  https://github.com/xiweile
 */
package cn.youai.xiuzhen.stat.mapper;

import cn.youai.xiuzhen.stat.entity.GameStatRemain;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 留存统计 Mapper 接口
 * </p>
 *
 * @author buliangliang
 * @since 2020-08-26
 */
public interface GameDataRemainMapper extends BaseMapper<GameStatRemain> {
    /**
     * 留存统计
     */
    GameStatRemain gameRemainCount(@Param("serverId") int serverId, @Param("date") String date, @Param("statDate") Date statDate);

    /**
     * 批量插入更新
     */
    int updateOrInsert(List<GameStatRemain> list);

    /**
     * 从开服起统计留存
     * 优化-需要手动处理日期
     *
     * @param serverId          服务器
     * @param startRegisterDate 当天0点注册日期
     * @param endRegisterDate   当天24点注册日期
     * @param startLoginDay     统计时间的0点
     * @param endLoginDay       统计时间的24点
     * @param logTable          logTable
     * @return 玩家人数
     */
    int selectRemain(@Param("serverId") int serverId,
                     @Param("startRegisterDate") String startRegisterDate,
                     @Param("endRegisterDate") String endRegisterDate,
                     @Param("startLoginDay") String startLoginDay,
                     @Param("endLoginDay") String endLoginDay,
                     @Param("logTable") String logTable);
}

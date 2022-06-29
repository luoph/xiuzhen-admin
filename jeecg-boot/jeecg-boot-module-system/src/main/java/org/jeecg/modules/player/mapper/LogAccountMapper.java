/*
 * create by mybatis-plus-generator  https://github.com/xiweile
 */
package org.jeecg.modules.player.mapper;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.player.entity.LogAccount;
import org.jeecg.modules.player.entity.MergeServerVO;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 玩家登录、创角统计 Mapper 接口
 * </p>
 *
 * @author buliangliang
 * @since 2020-08-21
 */
public interface LogAccountMapper extends BaseMapper<LogAccount> {
    /**
     * 当天登录/注册玩家数
     */
    int gerLoginRegisterPlayerNum(@Param("serverId") int serverId, @Param("createTime") String createTime, @Param("type") int type, @Param("logTable") String logTable);

    /**
     * 新增注册付费总额
     *
     * @param channel
     * @param serverId
     * @param date
     * @return
     */
    double getRegisterPayAmount(@Param("serverId") int serverId, @Param("date") String date, @Param("logTable") String logTable);

    /**
     * 新增注册付费玩家
     *
     * @param channel
     * @param serverId
     * @param date
     * @return
     */
    int getRegisterPayPlayer(@Param("serverId") int serverId, @Param("date") String date, @Param("logTable") String logTable);

    /**
     * 注册二次付费玩家
     *
     * @param channel
     * @param serverId
     * @param date
     * @return
     */
    int getDoublePayRegisterPlayer(@Param("serverId") int serverId, @Param("date") String date, @Param("logTable") String logTable);

    /**
     * 当前登陆玩家ids
     *
     * @param serverId
     * @param date
     * @param logTable
     * @return
     */
    List<Long> getPlayerIdsByLoginDate(@Param("serverId") int serverId, @Param("date") String date, @Param("logTable") String logTable);

    /**
     * 当前登陆玩家ids
     */
    List<LogAccount> getPlayerIdsByLoginDates(@Param("serverId") int serverId, @Param("dateList") List<JSONObject> dateList, @Param("logTable") String logTable);

    /**
     * 指定日期没登陆玩家id
     */
    List<Long> getPlayerIdsByNoLoginRangeDate(@Param("serverId") int serverId, @Param("startDate") String startDate, @Param("endDate") String endDate, @Param("logTable") String logTable);

    List<MergeServerVO> getServerLoginNum(@Param("logTable") String logTable, @Param("startTime") Date startTime, @Param("endTime") Date endTime);
}

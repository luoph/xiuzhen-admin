/*
 * create by mybatis-plus-generator  https://github.com/xiweile
 */
package org.jeecg.modules.player.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.player.entity.LogAccount;
import org.jeecg.modules.player.entity.MergeServerVO;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 玩家登录、创角统计 服务类
 * </p>
 *
 * @author buliangliang
 * @since 2020-08-21
 */
public interface ILogAccountService extends IService<LogAccount> {
    /**
     * 登录注册角色数
     *
     * @param channel
     * @param serverId
     * @param date
     * @param type
     * @return
     */
    int loginRegisterPlayer(int serverId, String date, int type);

    /**
     * 新注册付费总额
     */
    double registerPayAmount(int serverId, String date);

    /**
     * 新注册付费玩家
     */
    int registerPayPlayer(int serverId, String date);

    /**
     * 二次付费玩家
     */
    int doublePayRegisterPlayer(int serverId, String date);

    /**
     * 当前登陆玩家ids
     */
    List<Long> getPlayerIdsByLoginDate(int serverId, Date date);

    /**
     * 批量日期，查询每日活跃玩家
     *
     * @param serverId 服务器id
     * @param dateList 批量日期
     * @return Map<String, List < Long>> 日期-活跃玩家s
     */
    Map<String, List<Long>> getPlayerIdsByLoginDates(int serverId, List<JSONObject> dateList);

    /**
     * 批量日期，查询每日活跃玩家数量
     *
     * @param serverId 服务器id
     * @param dateList 批量日期
     * @param type     统计类型 1-按天 2-按月 3-按年
     * @return Map<String, Integer> 日期-活跃玩家数量
     */
    Map<String, Integer> getPlayerNumByLoginDates(int serverId, List<Date> dateList, int type);

    /**
     * 指定日期没登陆玩家id
     *
     * @param serverId   服务器
     * @param srcDate    目标日期
     * @param beforeDate 指定前几天
     * @return 玩家ids
     */
    List<Long> getPlayerIdsByNoLoginRangeDate(int serverId, Date srcDate, int beforeDate);

    List<MergeServerVO> getServerLoginNum(Date startTime, Date endTime);
}

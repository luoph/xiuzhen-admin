/*
 * create by mybatis-plus-generator  https://github.com/xiweile
 */
package cn.youai.xiuzhen.stat.mapper;

import cn.youai.xiuzhen.game.entity.MergeServerVO;
import cn.youai.xiuzhen.stat.entity.LogAccount;
import cn.youai.xiuzhen.stat.entity.PlayerBehavior;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
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

    BigDecimal queryDau(@Param("getTime") Date getTime);

    List<Integer> selectRunningServerIdsByRange(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

    String queryPlayerIp(@Param("playerId") Long playerId, @Param("createDate") Date createDate);

    List<PlayerBehavior> selectBehaviorCount(@Param("serverId") Integer serverId,
                                             @Param("nickname") String nickname,
                                             @Param("playerId") Long playerId,
                                             @Param("start") Date start,
                                             @Param("end") Date end);

    /**
     * 当天登录/注册玩家数
     */
    int loginRegisterPlayerNum(@Param("channel") String channel,
                               @Param("sdkChannel") String sdkChannel,
                               @Param("serverId") Integer serverId,
                               @Param("date") Date createTime,
                               @Param("type") int type,
                               @Param("configAuth") String configAuth);

    /**
     * 新增注册付费总额
     */
    BigDecimal registerPayAmount(@Param("channel") String channel,
                                 @Param("sdkChannel") String sdkChannel,
                                 @Param("serverId") Integer serverId,
                                 @Param("date") Date date,
                                 @Param("configAuth") String configAuth);


    /**
     * 新增注册付费玩家
     */
    int registerPayPlayerNum(@Param("channel") String channel,
                             @Param("sdkChannel") String sdkChannel,
                             @Param("serverId") Integer serverId,
                             @Param("date") Date date,
                             @Param("configAuth") String configAuth);

    /**
     * 注册二次付费玩家
     */
    int doublePayRegisterPlayerNum(@Param("channel") String channel,
                                   @Param("sdkChannel") String sdkChannel,
                                   @Param("serverId") Integer serverId,
                                   @Param("date") Date date,
                                   @Param("configAuth") String configAuth);

    /**
     * 当前登陆玩家ids
     */
    List<Long> getPlayerIdsByLoginDate(@Param("serverId") int serverId, @Param("date") String date);

    /**
     * 当前登陆玩家ids
     */
    List<LogAccount> getPlayerIdsByLoginDates(@Param("serverId") int serverId, @Param("dateList") List<JSONObject> dateList);

    /**
     * 指定日期没登陆玩家id
     */
    List<Long> getPlayerIdsByNoLoginRangeDate(@Param("serverId") int serverId, @Param("startDate") String startDate, @Param("endDate") String endDate);

    List<MergeServerVO> getServerLoginNum(@Param("startTime") Date startTime, @Param("endTime") Date endTime);

    IPage<PlayerBehavior> selectBehaviorGroup(Page<?> page,
                                              @Param("serverId") Integer serverId,
                                              @Param("nickname") String nickname,
                                              @Param("playerId") Long playerId,
                                              @Param("start") Date start,
                                              @Param("end") Date end);
}

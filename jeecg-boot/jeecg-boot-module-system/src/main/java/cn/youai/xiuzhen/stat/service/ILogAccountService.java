/*
 * create by mybatis-plus-generator  https://github.com/xiweile
 */
package cn.youai.xiuzhen.stat.service;

import cn.youai.xiuzhen.game.entity.MergeServerVO;
import cn.youai.xiuzhen.stat.entity.LogAccount;
import cn.youai.xiuzhen.stat.entity.PlayerBehavior;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;

import java.math.BigDecimal;
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

    BigDecimal queryDau(Date getTime);

    List<Integer> selectRunningServerIdsByRange(Date startDate, Date endDate);

    String queryPlayerIp(Long playerId, Date createDate);

    List<PlayerBehavior> selectBehaviorCount(Integer serverId, String nickname, Long playerId, Date start, Date end);

    /**
     * 登录注册角色数
     */
    int channelLoginRegisterPlayerNum(String channel, String sdkChannel, Integer serverId, Date date, int type);

    /**
     * 新注册付费总额
     */
    BigDecimal channelRegisterPayAmount(String channel, Integer serverId, Date date);

    /**
     * 新注册付费玩家
     */
    int channelRegisterPayPlayerNum(String channel, String sdkChannel, Integer serverId, Date date);

    /**
     * 二次付费玩家
     */
    int channelDoublePayRegisterPlayer(String channel, Integer serverId, Date date);

    /**
     * 当前登陆玩家ids
     */
    List<Long> getPlayerIdsByLoginDate(int serverId, Date date);

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

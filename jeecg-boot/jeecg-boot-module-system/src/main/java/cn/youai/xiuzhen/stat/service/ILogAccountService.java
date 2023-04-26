/*
 * create by mybatis-plus-generator  https://github.com/xiweile
 */
package cn.youai.xiuzhen.stat.service;

import cn.youai.xiuzhen.game.entity.MergeServerVO;
import cn.youai.xiuzhen.stat.entity.LogAccount;
import cn.youai.xiuzhen.stat.entity.PlayerBehavior;
import com.baomidou.mybatisplus.extension.service.IService;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

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
    int loginRegisterPlayerNum(String channel, String sdkChannel, Integer serverId, Date date, int type, String configAuth);

    /**
     * 新注册付费总额
     */
    BigDecimal registerPayAmount(String channel, String sdkChannel, Integer serverId, Date date, String configAuth);

    /**
     * 新注册付费玩家
     */
    int registerPayPlayerNum(String channel, String sdkChannel, Integer serverId, Date date, String configAuth);

    /**
     * 二次付费玩家
     */
    int doublePayRegisterPlayerNum(String channel, String sdkChannel, Integer serverId, Date date, String configAuth);

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

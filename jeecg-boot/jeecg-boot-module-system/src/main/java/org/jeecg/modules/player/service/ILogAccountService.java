/*
 * create by mybatis-plus-generator  https://github.com/xiweile
 */
package org.jeecg.modules.player.service;

import org.jeecg.modules.player.entity.LogAccount;
import com.baomidou.mybatisplus.extension.service.IService;

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
     * 登陆注册角色数
     *
     * @param channel
     * @param serverId
     * @param date
     * @param type
     * @return
     */
    int loginRegisterPlayer(String channel, int serverId, String date, int type);

    /**
     * 新注册付费总额
     *
     * @param channel
     * @param serverId
     * @param date
     * @return
     */
    double registerPayAmount(String channel, int serverId, String date);

    /**
     * 新注册付费玩家
     *
     * @param channel
     * @param serverId
     * @param date
     * @return
     */
    int registerPayPlayer(String channel, int serverId, String date);

    /**
     * 二次付费玩家
     *
     * @param channel
     * @param serverId
     * @param date
     * @return
     */
    int doublePayRegisterPlayer(String channel, int serverId, String date);
}

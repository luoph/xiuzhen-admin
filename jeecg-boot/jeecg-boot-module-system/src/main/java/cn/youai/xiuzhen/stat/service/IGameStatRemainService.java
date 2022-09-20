/*
 * create by mybatis-plus-generator  https://github.com/xiweile
 */
package cn.youai.xiuzhen.stat.service;

import cn.youai.xiuzhen.stat.entity.GameStatRemain;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Date;

/**
 * <p>
 * 留存统计
 * </p>
 *
 * @author luopeihuan
 * @since 2020-08-26
 */
public interface IGameStatRemainService extends IService<GameStatRemain> {

    /**
     * 留存统计
     */
    GameStatRemain queryServerRemain(int serverId, Date registerDate);

    GameStatRemain queryChannelRemain(String channel, Date registerDate);

    GameStatRemain selectServerRemain(int serverId, Date registerDate);

    GameStatRemain selectChannelRemain(String channel, Date registerDate);

    void calcServerRemain(int serverId, Date registerDate);

    void calcChannelRemain(String channel, Date registerDate);

}

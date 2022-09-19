/*
 * create by mybatis-plus-generator  https://github.com/xiweile
 */
package cn.youai.xiuzhen.stat.service;

import cn.youai.xiuzhen.game.constant.RoleType;
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
    GameStatRemain queryServerRemain(int serverId, int roleType, Date registerDate);

    GameStatRemain selectServerRemain(int serverId, RoleType roleType, Date registerDate);

    void calcServerRemain(int serverId, RoleType roleType, Date registerDate, int days, boolean updateAll);

}

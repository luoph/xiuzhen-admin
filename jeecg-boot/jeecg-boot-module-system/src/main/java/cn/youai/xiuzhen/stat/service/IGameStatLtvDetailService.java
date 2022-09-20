/*
 * create by mybatis-plus-generator  https://github.com/xiweile
 */
package cn.youai.xiuzhen.stat.service;

import cn.youai.xiuzhen.stat.entity.GameStatLtvDetail;
import cn.youai.xiuzhen.stat.entity.ServerLtvAmount;
import com.baomidou.mybatisplus.extension.service.IService;
import com.googlecode.aviator.spring.SringContextFunctionLoader;

import java.util.Date;

/**
 * <p>
 * LTV统计 服务类
 * </p>
 *
 * @author buliangliang
 * @since 2021-12-22
 */
public interface IGameStatLtvDetailService extends IService<GameStatLtvDetail> {

    GameStatLtvDetail selectServerLtvDetail(int serverId, Date registerDate);

    GameStatLtvDetail selectChannelLtvDetail(String channel, Date registerDate);

    ServerLtvAmount queryServerLtvAmount(int serverId, Date registerDate, int days);

    ServerLtvAmount queryChannelLtvAmount(String channel, Date registerDate, int days);

    GameStatLtvDetail queryServerLtvDetail(int serverId, Date registerDate);

    GameStatLtvDetail queryChannelLtvDetail(String channel, Date registerDate);

    void calcServerLtvDetail(int serverId, Date registerDate, int days, boolean updateAll);

    void calcChannelLtvDetail(String channel, Date registerDate, int days, boolean updateAll);

}

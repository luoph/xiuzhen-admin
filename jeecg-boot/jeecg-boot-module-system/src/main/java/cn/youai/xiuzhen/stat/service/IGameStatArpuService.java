package cn.youai.xiuzhen.stat.service;

import cn.youai.xiuzhen.stat.entity.GameStatArpu;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Date;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 数据报表
 * @date 2020-10-10
 */
public interface IGameStatArpuService extends IService<GameStatArpu> {

    GameStatArpu selectServerArpu(int serverId, Date countDate);

    GameStatArpu selectChannelArpu(String channel, Date countDate);

    GameStatArpu queryServerArpu(int serverId, Date countDate);

    GameStatArpu queryChannelArpu(String channel, Date countDate);

    void calcServerArpu(int serverId, Date countDate);

    void calcChannelArpu(String channel, Date countDate);

}

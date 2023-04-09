package cn.youai.xiuzhen.game.service;

import cn.youai.xiuzhen.game.entity.GameSdkChannel;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 游戏SDK渠道
 * @date 2023/04/09
 */
public interface IGameSdkChannelService extends IService<GameSdkChannel> {

    List<GameSdkChannel> selectSdkChannelList();

}

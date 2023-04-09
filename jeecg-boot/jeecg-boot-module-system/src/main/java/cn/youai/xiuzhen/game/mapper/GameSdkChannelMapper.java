package cn.youai.xiuzhen.game.mapper;

import cn.youai.xiuzhen.game.entity.GameSdkChannel;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 游戏SDK渠道
 * @date 2023-04-09
 */
public interface GameSdkChannelMapper extends BaseMapper<GameSdkChannel> {

    List<GameSdkChannel> selectSdkChannelList();

}

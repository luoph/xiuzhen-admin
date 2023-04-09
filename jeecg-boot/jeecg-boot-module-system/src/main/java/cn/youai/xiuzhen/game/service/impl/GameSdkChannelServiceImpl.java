package cn.youai.xiuzhen.game.service.impl;

import cn.youai.xiuzhen.game.entity.GameSdkChannel;
import cn.youai.xiuzhen.game.mapper.GameSdkChannelMapper;
import cn.youai.xiuzhen.game.service.IGameSdkChannelService;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 游戏Sdk渠道
 * @date 2023-04-09
 */
@Service
@DS("master")
public class GameSdkChannelServiceImpl extends ServiceImpl<GameSdkChannelMapper, GameSdkChannel> implements IGameSdkChannelService {

    @Override
    public List<GameSdkChannel> selectSdkChannelList() {
        return getBaseMapper().selectSdkChannelList();
    }

}

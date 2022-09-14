package cn.youai.xiuzhen.game.service.impl;

import cn.youai.xiuzhen.game.entity.GameInfo;
import cn.youai.xiuzhen.game.mapper.GameInfoMapper;
import cn.youai.xiuzhen.game.service.IGameInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 游戏信息
 * @date 2019-12-11
 */
@Service
public class GameInfoServiceImpl extends ServiceImpl<GameInfoMapper, GameInfo> implements IGameInfoService {

}

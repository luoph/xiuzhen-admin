package org.jeecg.modules.game.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.game.entity.GameImage;
import org.jeecg.modules.game.mapper.GameImageMapper;
import org.jeecg.modules.game.service.IGameImageService;
import org.springframework.stereotype.Service;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 游戏图片
 * @date 2020-11-02
 */
@Service
public class GameImageServiceImpl extends ServiceImpl<GameImageMapper, GameImage> implements IGameImageService {

}

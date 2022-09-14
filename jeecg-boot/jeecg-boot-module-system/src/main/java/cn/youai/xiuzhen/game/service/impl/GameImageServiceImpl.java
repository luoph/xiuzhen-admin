package cn.youai.xiuzhen.game.service.impl;

import cn.youai.xiuzhen.game.entity.GameImage;
import cn.youai.xiuzhen.game.mapper.GameImageMapper;
import cn.youai.xiuzhen.game.service.IGameImageService;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 游戏图片
 * @date 2020-11-02
 */
@Service
@DS("master")
public class GameImageServiceImpl extends ServiceImpl<GameImageMapper, GameImage> implements IGameImageService {

}

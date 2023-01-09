package cn.youai.xiuzhen.game.service.impl;

import cn.youai.xiuzhen.game.entity.GameSensitiveWord;
import cn.youai.xiuzhen.game.mapper.GameSensitiveWordMapper;
import cn.youai.xiuzhen.game.service.IGameSensitiveWordService;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 敏感词
 * @date 2023-01-10
 */
@Service
@DS("master")
public class GameSensitiveWordServiceImpl extends ServiceImpl<GameSensitiveWordMapper, GameSensitiveWord> implements IGameSensitiveWordService {

}

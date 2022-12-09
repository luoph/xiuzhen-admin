package cn.youai.xiuzhen.game.service.impl;

import cn.youai.xiuzhen.game.entity.GameLampNotice;
import cn.youai.xiuzhen.game.mapper.GameLampNoticeMapper;
import cn.youai.xiuzhen.game.service.IGameLampNoticeService;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 跑马灯消息
 * @date 2020-08-10
 */
@Service
@DS("master")
public class GameLampNoticeServiceImpl extends ServiceImpl<GameLampNoticeMapper, GameLampNotice> implements IGameLampNoticeService {
    
}

package org.jeecg.modules.game.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.game.entity.GameNotice;
import org.jeecg.modules.game.mapper.GameNoticeMapper;
import org.jeecg.modules.game.service.IGameNoticeService;
import org.springframework.stereotype.Service;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 游戏公告
 * @date 2019-12-13
 */
@Service
public class GameNoticeServiceImpl extends ServiceImpl<GameNoticeMapper, GameNotice> implements IGameNoticeService {

}

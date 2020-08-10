package org.jeecg.modules.game.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.youai.xiuzhen.utils.DateUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.common.okhttp.OkHttpHelper;
import org.jeecg.modules.game.entity.GameLampNotice;
import org.jeecg.modules.game.entity.GameServer;
import org.jeecg.modules.game.mapper.GameLampNoticeMapper;
import org.jeecg.modules.game.service.IGameLampNoticeService;
import org.jeecg.modules.game.service.IGameServerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 跑马灯消息
 * @date 2020-08-10
 */
@Service
public class GameLampNoticeServiceImpl extends ServiceImpl<GameLampNoticeMapper, GameLampNotice> implements IGameLampNoticeService {

    @Autowired
    private IGameServerService gameServerService;

    @Value("${app.send-lamp-notice}")
    private String sendLampNoticePath;

    @Override
    public void sendLampNoticeToGameServer(List<Integer> serverIds, GameLampNotice gameLampNotice) {
        if (CollUtil.isNotEmpty(serverIds) && gameLampNotice != null) {
            for (Integer serverId : serverIds) {
                GameServer gameServer = gameServerService.getById(serverId);
                if (gameServer != null) {
                    OkHttpHelper.post(gameServer.getGmUrl() + sendLampNoticePath, gameLampNotice);
                }
            }
            updateById(new GameLampNotice().setId(gameLampNotice.getId()).setLastSendTime(DateUtils.now()));
        }
    }
}

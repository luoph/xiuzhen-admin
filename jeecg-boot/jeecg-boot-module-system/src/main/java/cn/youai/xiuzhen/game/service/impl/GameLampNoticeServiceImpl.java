package cn.youai.xiuzhen.game.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.youai.server.springboot.component.OkHttpHelper;
import cn.youai.server.utils.DateUtils;
import cn.youai.xiuzhen.game.entity.GameLampNotice;
import cn.youai.xiuzhen.game.entity.GameServer;
import cn.youai.xiuzhen.game.mapper.GameLampNoticeMapper;
import cn.youai.xiuzhen.game.service.IGameLampNoticeService;
import cn.youai.xiuzhen.game.service.IGameServerService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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
            gameLampNotice.setLastSendTime(DateUtils.now());
            updateById(gameLampNotice);
        }
    }
}

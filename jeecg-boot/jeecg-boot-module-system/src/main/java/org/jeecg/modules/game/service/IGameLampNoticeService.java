package org.jeecg.modules.game.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.game.entity.GameLampNotice;

import java.util.List;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description game_lamp_notice
 * @date 2020-08-10
 */
public interface IGameLampNoticeService extends IService<GameLampNotice> {
    /**
     * 向游戏服下发跑马灯消息
     *
     * @param serverIds
     * @param gameLampNotice
     */
    void sendLampNoticeToGameServer(List<Integer> serverIds, GameLampNotice gameLampNotice);

}

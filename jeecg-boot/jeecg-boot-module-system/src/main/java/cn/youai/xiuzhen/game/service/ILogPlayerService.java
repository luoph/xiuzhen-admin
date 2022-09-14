package cn.youai.xiuzhen.game.service;

import cn.youai.xiuzhen.game.entity.LogPlayer;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description log_player
 * @date 2021-01-14
 */
public interface ILogPlayerService extends IService<LogPlayer> {

    List<Map> selectMilitaryStrengVoAll(int serverId, String createDateBegin, String createDateEnd);

    List<Map> selectMilitaryStrengVoAllByPlayerId(int serverId, String playerId, String createDateBegin, String createDateEnd);

}

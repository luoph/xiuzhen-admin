package cn.youai.xiuzhen.game.service;

import cn.youai.xiuzhen.game.entity.GameRegisterInfo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 玩家注册信息
 * @date 2020-01-04
 */
public interface IGameRegisterInfoService extends IService<GameRegisterInfo> {

    /**
     * 登录流水
     *
     * @param rangeDateBegin
     * @param rangeDateEnd
     * @param playerId
     * @param serverId
     * @return
     */
    List<GameRegisterInfo> queryLoginList(String rangeDateBegin, String rangeDateEnd, Long playerId, Integer serverId);
}

package org.jeecg.modules.game.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.game.entity.GameAppUpdate;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 客户端更新配置
 * @date 2021-06-10
 */
public interface IGameAppUpdateService extends IService<GameAppUpdate> {

    /**
     * 刷新配置文件
     */
    void updateConfig();

}

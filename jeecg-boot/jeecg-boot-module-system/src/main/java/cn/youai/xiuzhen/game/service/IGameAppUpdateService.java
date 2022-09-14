package cn.youai.xiuzhen.game.service;

import cn.youai.xiuzhen.game.entity.GameAppUpdate;
import com.baomidou.mybatisplus.extension.service.IService;

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

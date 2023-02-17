package cn.youai.xiuzhen.game.service;

import cn.youai.xiuzhen.game.entity.GameInfo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 游戏信息
 * @date 2019-12-11
 */
public interface IGameInfoService extends IService<GameInfo> {

    void refreshConfig();

}

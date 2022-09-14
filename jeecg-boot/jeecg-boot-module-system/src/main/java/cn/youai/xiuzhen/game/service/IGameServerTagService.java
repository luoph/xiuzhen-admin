package cn.youai.xiuzhen.game.service;

import cn.youai.xiuzhen.game.entity.GameServerTag;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 游戏服标签
 * @date 2022-03-25
 */
public interface IGameServerTagService extends IService<GameServerTag> {

    List<GameServerTag> selectTags();

}

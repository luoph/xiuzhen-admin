package org.jeecg.modules.game.service;

import cn.youai.commons.model.Response;
import cn.youai.xiuzhen.entity.pojo.ConfItem;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.game.entity.GameEmail;

import java.util.List;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 游戏下发邮件
 * @date 2020-01-04
 */
public interface IGameEmailService extends IService<GameEmail> {

    /**
     * 存储邮件
     *
     * @param gameEmail
     * @return
     */
    Response saveEmail(GameEmail gameEmail);

    /**
     * 获取道具树形菜案
     *
     * @return
     */
    List<ConfItem> itemTree(Integer itemId, String itemName);
}

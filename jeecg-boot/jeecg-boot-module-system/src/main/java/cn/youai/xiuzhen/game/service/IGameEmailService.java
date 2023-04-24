package cn.youai.xiuzhen.game.service;

import cn.youai.basics.model.Response;
import cn.youai.server.conf.ConfItem;
import cn.youai.xiuzhen.game.entity.GameEmail;
import com.baomidou.mybatisplus.extension.service.IService;

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
     */
    Response saveEmail(GameEmail gameEmail);

    /**
     * 获取道具树形菜案
     */
    List<ConfItem> itemTree(Integer itemId, String itemName);

    /**
     * 发送邮件
     */
    Response sendEmail(GameEmail gameEmail);
}

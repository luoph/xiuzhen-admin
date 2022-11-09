package cn.youai.xiuzhen.game.service;

import cn.youai.xiuzhen.game.entity.GameRedeemCode;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 激活码
 * @date 2020-01-07
 */
public interface IGameRedeemCodeService extends IService<GameRedeemCode> {

    List<String> selectExistsRedeemCode();

}

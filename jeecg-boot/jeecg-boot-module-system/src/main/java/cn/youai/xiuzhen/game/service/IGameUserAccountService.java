/*
 * create by mybatis-plus-generator  https://github.com/xiweile
 */
package cn.youai.xiuzhen.game.service;

import cn.youai.xiuzhen.game.entity.GameUserAccount;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Date;

/**
 * <p>
 * 账号
 * </p>
 *
 * @author luopeihuan
 * @since 2023-01-17
 */
public interface IGameUserAccountService extends IService<GameUserAccount> {
    int userAccountNum(String channel, String sdkChannel, Date date);

}

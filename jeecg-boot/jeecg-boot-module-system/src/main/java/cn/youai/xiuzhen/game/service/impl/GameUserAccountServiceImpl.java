/*
 * create by mybatis-plus-generator  https://github.com/xiweile
 */
package cn.youai.xiuzhen.game.service.impl;

import cn.youai.xiuzhen.game.entity.GameUserAccount;
import cn.youai.xiuzhen.game.service.IGameUserAccountService;
import cn.youai.xiuzhen.stat.mapper.GameUserAccountMapper;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 账号
 * </p>
 *
 * @author luopeihuan
 * @since 2023-01-17
 */
@Service
@DS("master")
public class GameUserAccountServiceImpl extends ServiceImpl<GameUserAccountMapper, GameUserAccount> implements IGameUserAccountService {
    @Override
    public int queryUserAccountNum(String channel, String sdkChannel, Date date) {
        return getBaseMapper().queryUserAccountNum(channel, sdkChannel, date);
    }

    @Override
    public List<String> querySdkChannels() {
        return getBaseMapper().querySdkChannels();
    }

}

/*
 * create by mybatis-plus-generator  https://github.com/xiweile
 */
package cn.youai.xiuzhen.game.service.impl;

import cn.youai.server.utils.DateUtils;
import cn.youai.xiuzhen.game.entity.GameStatCmd;
import cn.youai.xiuzhen.game.mapper.GameStatCmdMapper;
import cn.youai.xiuzhen.game.service.IGameStatCmdService;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * <p>
 * 接口调用超时统计 服务实现类
 * </p>
 *
 * @author luopeihuan
 * @since 2023-07-11
 */
@Service
@DS("shardingSphere")
public class GameStatCmdServiceImpl extends ServiceImpl<GameStatCmdMapper, GameStatCmd> implements IGameStatCmdService {

    @Override
    public void delete(int serverId, Date date) {
        Wrapper<GameStatCmd> query = Wrappers.<GameStatCmd>lambdaQuery()
                .eq(GameStatCmd::getServerId, serverId)
                .eq(GameStatCmd::getCreateDate, DateUtils.dateOnly(date));
        remove(query);
    }
}

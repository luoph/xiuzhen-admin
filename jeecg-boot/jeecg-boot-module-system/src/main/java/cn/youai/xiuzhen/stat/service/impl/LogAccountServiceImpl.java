/*
 * create by mybatis-plus-generator  https://github.com/xiweile
 */
package cn.youai.xiuzhen.stat.service.impl;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.youai.server.utils.DateUtils;
import cn.youai.xiuzhen.game.entity.MergeServerVO;
import cn.youai.xiuzhen.stat.entity.LogAccount;
import cn.youai.xiuzhen.stat.entity.PlayerBehavior;
import cn.youai.xiuzhen.stat.mapper.LogAccountMapper;
import cn.youai.xiuzhen.stat.service.ILogAccountService;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

/**
 * <p>
 * 玩家登录、创角统计 服务实现类
 * </p>
 *
 * @author buliangliang
 * @since 2020-08-21
 */
@Service
@DS("shardingSphere")
public class LogAccountServiceImpl extends ServiceImpl<LogAccountMapper, LogAccount> implements ILogAccountService {

    @Override
    public BigDecimal queryDau(Date getTime) {
        return getBaseMapper().queryDau(getTime);
    }

    @Override
    public List<Integer> selectRunningServerIdsByRange(Date startDate, Date endDate) {
        return getBaseMapper().selectRunningServerIdsByRange(startDate, endDate);
    }

    @Override
    public String queryPlayerIp(Long playerId, Date createDate) {
        return getBaseMapper().queryPlayerIp(playerId, createDate);
    }

    @Override
    public List<PlayerBehavior> selectBehaviorCount(Integer serverId, String nickname, Long playerId, Date start, Date end) {
        return getBaseMapper().selectBehaviorCount(serverId, nickname, playerId, start, end);
    }

    @Override
    public int loginRegisterPlayerNum(String channel, String sdkChannel, Integer serverId, Date date, int type, String configAuth) {
        return getBaseMapper().loginRegisterPlayerNum(channel, sdkChannel, serverId, date, type, configAuth);
    }

    @Override
    public BigDecimal registerPayAmount(String channel, String sdkChannel, Integer serverId, Date date, String configAuth) {
        return getBaseMapper().registerPayAmount(channel, sdkChannel, serverId, date, configAuth);
    }

    @Override
    public int registerPayPlayerNum(String channel, String sdkChannel, Integer serverId, Date date, String configAuth) {
        return getBaseMapper().registerPayPlayerNum(channel, sdkChannel, serverId, date, configAuth);
    }

    @Override
    public int doublePayRegisterPlayerNum(String channel, String sdkChannel, Integer serverId, Date date, String configAuth) {
        return getBaseMapper().doublePayRegisterPlayerNum(channel, sdkChannel, serverId, date, configAuth);
    }

    @Override
    public List<Long> getPlayerIdsByLoginDate(int serverId, Date date) {
        return getBaseMapper().getPlayerIdsByLoginDate(serverId, DateUtils.formatDate(date, DatePattern.NORM_DATE_PATTERN));
    }

    @Override
    public List<Long> getPlayerIdsByNoLoginRangeDate(int serverId, Date srcDate, int beforeDate) {
        String startDate = DateUtil.formatDate(DateUtils.addDays(srcDate, -beforeDate));
        String endDate = DateUtil.formatDate(srcDate);
        return getBaseMapper().getPlayerIdsByNoLoginRangeDate(serverId, startDate, endDate);
    }

    @Override
    public List<MergeServerVO> getServerLoginNum(Date startTime, Date endTime) {
        return getBaseMapper().getServerLoginNum(DateUtils.dateOnly(startTime), DateUtils.dateOnly(endTime));
    }
}

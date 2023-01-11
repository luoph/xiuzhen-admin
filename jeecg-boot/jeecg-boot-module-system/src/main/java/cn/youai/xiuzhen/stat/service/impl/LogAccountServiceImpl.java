/*
 * create by mybatis-plus-generator  https://github.com/xiweile
 */
package cn.youai.xiuzhen.stat.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;
import cn.youai.server.utils.DateUtils;
import cn.youai.xiuzhen.game.entity.MergeServerVO;
import cn.youai.xiuzhen.stat.entity.LogAccount;
import cn.youai.xiuzhen.stat.entity.PlayerBehavior;
import cn.youai.xiuzhen.stat.mapper.LogAccountMapper;
import cn.youai.xiuzhen.stat.service.ILogAccountService;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.constant.TimeConstant;
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
    public List<Integer> selectRunningServerIds(Date countDate) {
        return getBaseMapper().selectRunningServerIds(countDate);
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
    public int serverLoginRegisterPlayerNum(int serverId, Date date, int type) {
        return getBaseMapper().serverLoginRegisterPlayerNum(serverId, date, type);
    }

    @Override
    public int channelLoginRegisterPlayerNum(String channel, Integer serverId, Date date, int type) {
        return getBaseMapper().channelLoginRegisterPlayerNum(channel, serverId, date, type);
    }

    @Override
    public BigDecimal serverRegisterPayAmount(int serverId, Date date) {
        return getBaseMapper().serverRegisterPayAmount(serverId, date);
    }

    @Override
    public BigDecimal channelRegisterPayAmount(String channel, Integer serverId, Date date) {
        return getBaseMapper().channelRegisterPayAmount(channel, serverId, date);
    }

    @Override
    public int serverRegisterPayPlayerNum(int serverId, Date date) {
        return getBaseMapper().serverRegisterPayPlayerNum(serverId, date);
    }

    @Override
    public int channelRegisterPayPlayerNum(String channel, Integer serverId, Date date) {
        return getBaseMapper().channelRegisterPayPlayerNum(channel, serverId, date);
    }

    @Override
    public int serverDoublePayRegisterPlayer(int serverId, Date date) {
        return getBaseMapper().serverDoublePayRegisterPlayer(serverId, date);
    }

    @Override
    public int channelDoublePayRegisterPlayer(String channel, Integer serverId, Date date) {
        return getBaseMapper().channelDoublePayRegisterPlayer(channel, serverId, date);
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
    public Map<String, List<Long>> getPlayerIdsByLoginDates(int serverId, List<JSONObject> dateList) {
        if (CollectionUtil.isEmpty(dateList)) {
            return null;
        }
        List<LogAccount> playerIdsByLoginDates = getBaseMapper().getPlayerIdsByLoginDates(serverId, dateList);
        if (CollUtil.isNotEmpty(playerIdsByLoginDates)) {
            Map<String, List<Long>> result = new HashMap<>(playerIdsByLoginDates.size());
            playerIdsByLoginDates.forEach(e -> {
                String formatDate = DateUtils.formatDate(e.getCreateDate(), DatePattern.NORM_DATE_PATTERN);
                List<Long> longs = result.get(formatDate);
                if (longs == null) {
                    longs = new ArrayList<>();
                }
                longs.add(e.getPlayerId());
                result.put(formatDate, longs);
            });
            return result;
        }
        return null;
    }

    @Override
    public Map<String, Integer> getPlayerNumByLoginDates(int serverId, List<Date> dateList, int type) {
        if (CollectionUtil.isEmpty(dateList)) {
            return null;
        }

        List<JSONObject> dateJsonList = new ArrayList<>(dateList.size());
        dateList.forEach(e -> {
            Date[] startAndEnd;
            Date now = DateUtils.now();
            if (type == CommonConstant.PAY_ORDER_STAT_TYPE_MONTH) {
                startAndEnd = new Date[]{DateUtil.beginOfMonth(now), DateUtil.endOfMonth(now)};
            } else if (type == CommonConstant.PAY_ORDER_STAT_TYPE_YEAR) {
                startAndEnd = new Date[]{DateUtil.beginOfYear(now), DateUtil.endOfYear(now)};
            } else {
                startAndEnd = new Date[]{DateUtil.beginOfDay(now), DateUtil.endOfDay(now)};
            }
            JSONObject jsonParam = new JSONObject();
            jsonParam.put("startTime", startAndEnd[0]);
            jsonParam.put("endTime", startAndEnd[1]);
            dateJsonList.add(jsonParam);
        });

        Map<String, List<Long>> playerIdsByLoginDates = getPlayerIdsByLoginDates(serverId, dateJsonList);
        if (MapUtil.isNotEmpty(playerIdsByLoginDates)) {
            Map<String, Integer> map = new HashMap<>(dateList.size());
            playerIdsByLoginDates.forEach((key, value) -> {
                if (type == CommonConstant.PAY_ORDER_STAT_TYPE_MONTH || type == CommonConstant.PAY_ORDER_STAT_TYPE_YEAR) {
                    String dateKey;
                    if (type == CommonConstant.PAY_ORDER_STAT_TYPE_MONTH) {
                        dateKey = DateUtils.formatDate(DateUtils.parseDate(key), TimeConstant.CHINESE_YEAR_MONTH_PATTERN);
                    } else {
                        dateKey = DateUtils.formatDate(DateUtils.parseDate(key), TimeConstant.CHINESE_YEAR_PATTERN);
                    }
                    Integer num = map.get(dateKey);
                    if (num == null) {
                        num = 0;
                    }
                    num += value == null ? 0 : value.size();
                    map.put(dateKey, num);
                } else {
                    map.put(key, value == null ? 0 : value.size());
                }
            });
            return map;
        }
        return null;
    }

    @Override
    public List<MergeServerVO> getServerLoginNum(Date startTime, Date endTime) {
        return getBaseMapper().getServerLoginNum(DateUtils.dateOnly(startTime), DateUtils.dateOnly(endTime));
    }
}

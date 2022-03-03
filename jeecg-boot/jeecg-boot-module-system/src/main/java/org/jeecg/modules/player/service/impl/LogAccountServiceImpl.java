/*
 * create by mybatis-plus-generator  https://github.com/xiweile
 */
package org.jeecg.modules.player.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;
import cn.youai.server.utils.DateUtils;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.constant.TimeConstant;
import org.jeecg.modules.player.entity.LogAccount;
import org.jeecg.modules.player.entity.MergeServerVO;
import org.jeecg.modules.player.mapper.LogAccountMapper;
import org.jeecg.modules.player.service.ILogAccountService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
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
public class LogAccountServiceImpl extends ServiceImpl<LogAccountMapper, LogAccount> implements ILogAccountService {

    @Resource
    private LogAccountMapper logAccountMapper;

    @Value("${app.log.db.table}")
    private String logTable;

    @Override
    public int loginRegisterPlayer(int serverId, String date, int type) {
        return logAccountMapper.gerLoginRegisterPlayerNum(serverId, date, type, logTable);
    }

    @Override
    public double registerPayAmount(int serverId, String date) {
        return logAccountMapper.getRegisterPayAmount(serverId, date, logTable);
    }

    @Override
    public int registerPayPlayer(int serverId, String date) {
        return logAccountMapper.getRegisterPayPlayer(serverId, date, logTable);
    }

    @Override
    public int doublePayRegisterPlayer(int serverId, String date) {
        return logAccountMapper.getDoublePayRegisterPlayer(serverId, date, logTable);
    }

    @Override
    public List<Long> getPlayerIdsByLoginDate(int serverId, Date date) {
        return logAccountMapper.getPlayerIdsByLoginDate(serverId, DateUtils.formatDate(date, DatePattern.NORM_DATE_PATTERN), logTable);
    }

    @Override
    public List<Long> getPlayerIdsByNoLoginRangeDate(int serverId, Date srcDate, int beforeDate) {
        String startDate = DateUtil.formatDate(DateUtils.addDays(srcDate, -beforeDate));
        String endDate = DateUtil.formatDate(srcDate);
        return logAccountMapper.getPlayerIdsByNoLoginRangeDate(serverId, startDate, endDate, logTable);
    }

    @Override
    public Map<String, List<Long>> getPlayerIdsByLoginDates(int serverId, List<JSONObject> dateList) {
        if (CollectionUtil.isEmpty(dateList)) {
            return null;
        }
        List<LogAccount> playerIdsByLoginDates = logAccountMapper.getPlayerIdsByLoginDates(serverId, dateList, logTable);
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
        return logAccountMapper.getServerLoginNum(logTable, DateUtils.dateOnly(startTime), DateUtils.dateOnly(endTime));
    }
}

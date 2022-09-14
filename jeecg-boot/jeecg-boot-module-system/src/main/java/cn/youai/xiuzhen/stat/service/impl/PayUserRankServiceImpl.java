package cn.youai.xiuzhen.stat.service.impl;

import cn.youai.server.utils.DateUtils;
import cn.youai.xiuzhen.stat.entity.GameRegisterInfoVO;
import cn.youai.xiuzhen.stat.entity.PayUserRank;
import cn.youai.xiuzhen.stat.mapper.PayUserRankMapper;
import cn.youai.xiuzhen.stat.service.IPayUserRankService;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 充值用户排行数据统计
 * @date 2020-10-09
 */
@Service
@DS("shardingSphere")
public class PayUserRankServiceImpl extends ServiceImpl<PayUserRankMapper, PayUserRank> implements IPayUserRankService {

    @Override
    public List<PayUserRank> queryUserRankByDateRange(String payTimeBegin, String payTimeEnd, Integer serverId, String channel) {
        // 如果选择开始时间和结束时间是同一天
        Date payTimeBeginDate = null;
        Date payTimeEndDate = null;
        payTimeBeginDate = DateUtils.parseDate(payTimeBegin);
        payTimeEndDate = DateUtils.parseDate(payTimeEnd);

        if (payTimeBegin.equals(payTimeEnd)) {
            Date[] dates = DateUtils.dateStartAndEnd(payTimeBeginDate);
            payTimeBeginDate = dates[0];
            payTimeEndDate = dates[1];
        }
        return getBaseMapper().queryUserRankByDateRange(payTimeBeginDate, payTimeEndDate, serverId, channel);
    }

    @Override
    public List<PayUserRank> queryPayRankByDateRange(String rangeDateBegin, String rangeDateEnd, int days, Integer serverId, String channel) {
        List<PayUserRank> list = null;
        Date nowDate = new Date();
        if (days == 0) {
            // 如果选择开始时间和结束时间是同一天
            Date rangeDateBeginTime = null;
            Date rangeDateEndTime = null;
            rangeDateBeginTime = DateUtils.parseDate(rangeDateBegin);
            rangeDateEndTime = DateUtils.parseDate(rangeDateEnd);
            if (rangeDateBegin.equals(rangeDateEnd)) {
                Date[] dates = DateUtils.dateStartAndEnd(rangeDateBeginTime);
                rangeDateBeginTime = dates[0];
                rangeDateEndTime = dates[1];
            }
            list = getBaseMapper().queryPayRankByDateRange(rangeDateBeginTime, rangeDateEndTime, serverId, channel);
            return getDataTreating(list);
        }
        //如果有选天数,就使用就近天数查询
        //获取过去第几天的日期
        Date pastDate = DateUtils.addDays(nowDate, days * (-1));
        list = getBaseMapper().queryPayRankByDateRange(pastDate, nowDate, serverId, channel);
        return getDataTreating(list);
    }

    /**
     * 获取数据处理后的list
     *
     * @param list
     * @return
     */
    public List<PayUserRank> getDataTreating(List<PayUserRank> list) {
        Date nowDate = new Date();
        for (PayUserRank payUserRank : list) {
            //获取玩家注册信息
            GameRegisterInfoVO playerRegisterInfo = payUserRank.getGameRegisterInfoVO();

            //获取玩家最后的充值时间
            Date payTimeMax = payUserRank.getPayTimeMax();

            int payWarningDays = DateUtils.daysBetween(payTimeMax, nowDate);
            //设置充值预警天数
            playerRegisterInfo.setPayWarningDays(payWarningDays);

            //获取玩家最后登录时间和注册时间
            Date loginDate = getBaseMapper().getPlayerLastLoginTime(payUserRank.getPlayerId());

            int loginWarningDays = DateUtils.daysBetween(loginDate, nowDate);
            //设置最后登录时间
            playerRegisterInfo.setLoginDate(loginDate);
            //设置登录预警天数
            playerRegisterInfo.setLoginWarningDays(loginWarningDays);

            payUserRank.setGameRegisterInfoVO(playerRegisterInfo);
        }
        return list;
    }

}

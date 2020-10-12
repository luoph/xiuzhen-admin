package org.jeecg.modules.game.service.impl;

import cn.youai.xiuzhen.utils.DateUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.game.entity.PayUserRank;
import org.jeecg.modules.game.entity.PlayerRegisterInfo;
import org.jeecg.modules.game.mapper.PayUserRankMapper;
import org.jeecg.modules.game.service.IPayUserRankService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 充值用户排行数据统计
 * @date 2020-10-09
 */
@Service
public class PayUserRankServiceImpl extends ServiceImpl<PayUserRankMapper, PayUserRank> implements IPayUserRankService {

    @Resource
    private PayUserRankMapper payUserRankMapper;

    @Override
    public List<PayUserRank> queryUserRankByDateRange(String payTimeBegin, String payTimeEnd, Integer serverId, String channel) {

        Date payTimeBeginDate = DateUtils.parseDate(payTimeBegin);
        Date payTimeEndDate = DateUtils.parseDate(payTimeEnd);

        return payUserRankMapper.queryUserRankByDateRange(payTimeBeginDate, payTimeEndDate, serverId, channel);
    }

    @Override
    public List<PayUserRank> queryPayRankByDateRange(String rangeDateBegin, String rangeDateEnd, int days, Integer serverId, String channel) {
        List<PayUserRank> list = null;
        Date nowDate = new Date();
        if (days == 0){
            Date rangeDateBeginTime = DateUtils.parseDate(rangeDateBegin);
            Date rangeDateEndTime = DateUtils.parseDate(rangeDateEnd);
            list = payUserRankMapper.queryPayRankByDateRange(rangeDateBeginTime, rangeDateEndTime, serverId, channel);
            for (PayUserRank payUserRank : list) {
                //获取玩家注册信息
                PlayerRegisterInfo playerRegisterInfo = payUserRank.getPlayerRegisterInfo();
                Date createDate = playerRegisterInfo.getCreateDate();
                int payWarningDays = DateUtils.daysBetween(createDate, nowDate);
                //设置充值预警天数
                playerRegisterInfo.setPayWarningDays(payWarningDays);
                payUserRank.setPlayerRegisterInfo(playerRegisterInfo);
            }
            return list;
        }
        //如果有选天数,就使用就近天数查询
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        //获取过去第几天的日期
        Date pastDate = DateUtils.getPastDate(days, sdf);
        list = payUserRankMapper.queryPayRankByDateRange(pastDate, nowDate, serverId, channel);
        for (PayUserRank payUserRank : list) {
            //获取玩家注册信息
            PlayerRegisterInfo playerRegisterInfo = payUserRank.getPlayerRegisterInfo();
            Date createDate = playerRegisterInfo.getCreateDate();
            int payWarningDays = DateUtils.daysBetween(createDate, nowDate);
            //设置充值预警天数
            playerRegisterInfo.setPayWarningDays(payWarningDays);
            payUserRank.setPlayerRegisterInfo(playerRegisterInfo);
        }
        return list;
    }
}

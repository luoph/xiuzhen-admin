package org.jeecg.modules.game.service.impl;

import cn.youai.xiuzhen.utils.BigDecimalUtil;
import cn.youai.xiuzhen.utils.DateUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.game.entity.GameDataReportCount;
import org.jeecg.modules.game.mapper.GameDataReportCountMapper;
import org.jeecg.modules.game.service.IGameDataReportCountService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 数据报表
 * @date 2020-10-10
 */
@Service
public class GameDataReportCountServiceImpl extends ServiceImpl<GameDataReportCountMapper, GameDataReportCount> implements IGameDataReportCountService {

    @Resource
    private GameDataReportCountMapper gameDataReportCountMapper;

    @Override
    public List<GameDataReportCount> queryDataReportByDateRange(String rangeDateBegin, String rangeDateEnd, int days, Integer serverId, String channel) {
        List<GameDataReportCount> list = null;
        // 如果没有选天数,就使用传入的时间查询
        if (days == 0){
            Date rangeDateBeginTime = DateUtils.parseDate(rangeDateBegin);
            Date rangeDateEndTime = DateUtils.parseDate(rangeDateEnd);
            list = getCompleteList(gameDataReportCountMapper.queryDataReportByDateRange(rangeDateBeginTime, rangeDateEndTime, serverId, channel));
            return list;
        }
        // 如果有选天数,就使用就近天数查询
        //SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        // 获取过去第几天的日期
        Date pastDate = DateUtils.getPastDate(days);
        Date nowDate = new Date();
        list = getCompleteList(gameDataReportCountMapper.queryDataReportByDateRange(pastDate, nowDate, serverId, channel));
        return list;
    }

    /**
     *  获取处理好的list集合
     * @param list
     * @return
     */
    public List<GameDataReportCount> getCompleteList(List<GameDataReportCount> list){
        // child 为汇总变量,需要单独对其进行计算
        GameDataReportCount child = new GameDataReportCount();
        // 封装数据用于计算的临时变量
        double loginNumSum = 0;
        double payNumSum = 0;

        BigDecimal payRateSum = null;

        double payAmountSum = 0;
        BigDecimal payAmountSumBigDecimal = null;

        BigDecimal arpuSum = null;
        BigDecimal arppuSum = null;

        double addNumSum = 0;
        double addPayNumSum = 0;

        BigDecimal addPayRateSumBigDecimal= null;

        double addPayAmountSum = 0;
        BigDecimal addPayAmountSumBigDecimal = null;

        BigDecimal addArpuSum= null;
        BigDecimal addArppuSum= null;

        double oldNumSum = 0;
        double oldPayNumSum = 0;

        BigDecimal oldPayRateSumBigDecimal= null;

        BigDecimal oldPayAmountSumBigDecimal = null;

        BigDecimal oldArpuSum= null;
        BigDecimal oldArppuSum= null;

        // 数据处理
        for (GameDataReportCount gameDataReportCount : list) {
            // 日期变为字符串显示
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String format = sdf.format(gameDataReportCount.getCountDate());
            gameDataReportCount.setDateStr(format);

            // 数据为空处理
            if (gameDataReportCount.getOldPayRate() == null){
                gameDataReportCount.setOldPayRate(new BigDecimal(0));
            }
            if (gameDataReportCount.getOldArpu() == null){
                gameDataReportCount.setOldArpu(new BigDecimal(0));
            }
            if (gameDataReportCount.getOldArppu() == null){
                gameDataReportCount.setOldArppu(new BigDecimal(0));
            }
            // 数据格式处理
            BigDecimal payRate = BigDecimalUtil.divideFour(gameDataReportCount.getPayRate().doubleValue(), 1,true);
            gameDataReportCount.setPayRate(payRate);

            BigDecimal addPayRate = BigDecimalUtil.divideFour(gameDataReportCount.getAddPayRate().doubleValue(), 1,true);
            gameDataReportCount.setAddPayRate(addPayRate);

            BigDecimal oldPayRate = BigDecimalUtil.divideFour(gameDataReportCount.getOldPayRate().doubleValue(), 1,true);
            gameDataReportCount.setOldPayRate(oldPayRate);

            // 计算child对象中的各属性值
            // 活跃玩家汇总
            loginNumSum = loginNumSum + gameDataReportCount.getLoginNum();
            //活跃付费数汇总
            payNumSum = payNumSum + gameDataReportCount.getPayNum();
            // 充值总金额
            BigDecimal payAmount = gameDataReportCount.getPayAmount();
            payAmountSum = payAmountSum + payAmount.doubleValue();
            // 新增玩家汇总
            addNumSum = addNumSum + gameDataReportCount.getAddNum();
            // 新增付费数汇总
            addPayNumSum = addPayNumSum = gameDataReportCount.getAddPayNum();
            // 新增充值金额汇总
            BigDecimal addPayAmount = gameDataReportCount.getAddPayAmount();
            addPayAmountSum = addPayAmountSum + addPayAmount.doubleValue();

        }
        // 活跃付费率汇总
        payRateSum = BigDecimalUtil.divideFour(payNumSum, loginNumSum, true);

        // 充值金额
        payAmountSumBigDecimal = new BigDecimal(payAmountSum);

        // ARPU汇总
        arpuSum = BigDecimalUtil.divideFour(payAmountSum, loginNumSum, true);

        // ARPPU汇总
        arppuSum = BigDecimalUtil.divideZero(payAmountSum, payNumSum,false);

        // 新增付费率汇总
        addPayRateSumBigDecimal = BigDecimalUtil.divideFour(addPayNumSum, addNumSum, true);

        // 新增充值金额
        addPayAmountSumBigDecimal = new BigDecimal(addPayAmountSum);

        // 新增ARPU汇总
        addArpuSum = BigDecimalUtil.divideFour(addPayAmountSum, addNumSum, true);

        // 新增ARPPU汇总
        addArppuSum = BigDecimalUtil.divideFour(addPayAmountSum, addPayNumSum,false);

        // 老玩家汇总
        oldNumSum = loginNumSum - addNumSum;
        // 老玩家付费数汇总
        oldPayNumSum = payNumSum - addPayNumSum;

        // 老玩家付费率汇总
        oldPayRateSumBigDecimal = BigDecimalUtil.divideFour(oldPayNumSum, oldNumSum, true);

        // 老玩家充值金额
        oldPayAmountSumBigDecimal = new BigDecimal(payAmountSum - addPayAmountSum);

        // 老玩家ARPU汇总
        oldArpuSum = BigDecimalUtil.divideFour((payAmountSum - addPayAmountSum), oldNumSum, false);

        // 老玩家ARPPU汇总
        oldArppuSum = BigDecimalUtil.divideFour((payAmountSum - addPayAmountSum), oldPayNumSum,false);

        // 封装child
        child.setLoginNum(new Double(loginNumSum).intValue());
        child.setPayNum(new Double(payNumSum).intValue());
        child.setPayRate(payRateSum);
        child.setPayAmount(payAmountSumBigDecimal);
        child.setArpu(arpuSum);
        child.setArppu(arppuSum);
        child.setAddNum(new Double(addNumSum).intValue());
        child.setAddPayNum(new Double(addPayNumSum).intValue());
        child.setAddPayRate(addPayRateSumBigDecimal);
        child.setAddPayAmount(addPayAmountSumBigDecimal);
        child.setAddArpu(addArpuSum);
        child.setAddArppu(addArppuSum);
        child.setOldNum(new Double(oldNumSum).intValue());
        child.setOldPayNum(new Double(oldPayNumSum).intValue());
        child.setOldPayRate(oldPayRateSumBigDecimal);
        child.setOldPayAmount(oldPayAmountSumBigDecimal);
        child.setOldArpu(oldArpuSum);
        child.setOldArppu(oldArppuSum);
        child.setDateStr("汇总");

        //封装成list集合的第一个元素
        list.add(0, child);

        return list;
    }
}

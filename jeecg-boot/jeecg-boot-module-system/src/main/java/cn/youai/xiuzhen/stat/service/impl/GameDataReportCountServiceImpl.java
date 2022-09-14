package cn.youai.xiuzhen.stat.service.impl;

import cn.youai.server.utils.DateUtils;
import cn.youai.xiuzhen.stat.entity.GameDataReportCount;
import cn.youai.xiuzhen.stat.mapper.GameDataReportCountMapper;
import cn.youai.xiuzhen.stat.service.IGameDataReportCountService;
import cn.youai.xiuzhen.utils.BigDecimalUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

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

    @Override
    public List<GameDataReportCount> queryDataReportByDateRange(String rangeDateBegin, String rangeDateEnd, int days, Integer serverId, String channel) {
        List<GameDataReportCount> list = null;
        // 如果没有选天数,就使用传入的时间查询
        if (days == 0) {
            Date rangeDateBeginTime = DateUtils.parseDate(rangeDateBegin);
            Date rangeDateEndTime = DateUtils.parseDate(rangeDateEnd);
            list = getCompleteList(getBaseMapper().queryDataReportByDateRange(rangeDateBeginTime, rangeDateEndTime, serverId, channel));
            return list;
        }
        // 如果有选天数,就使用就近天数查询
        // 获取过去第几天的日期
        Date nowDate = new Date();
        Date pastDate = DateUtils.addDays(nowDate, days * (-1));
        list = getCompleteList(getBaseMapper().queryDataReportByDateRange(pastDate, nowDate, serverId, channel));
        return list;
    }

    /**
     * 获取处理好的list集合
     *
     * @param list
     * @return
     */
    private List<GameDataReportCount> getCompleteList(List<GameDataReportCount> list) {
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

        BigDecimal addPayRateSumBigDecimal = null;

        double addPayAmountSum = 0;
        BigDecimal addPayAmountSumBigDecimal = null;

        BigDecimal addArpuSum = null;
        BigDecimal addArppuSum = null;

        double oldNumSum = 0;
        double oldPayNumSum = 0;

        BigDecimal oldPayRateSumBigDecimal = null;

        BigDecimal oldPayAmountSumBigDecimal = null;

        BigDecimal oldArpuSum = null;
        BigDecimal oldArppuSum = null;

        // 数据处理
        for (GameDataReportCount gameDataReportCount : list) {
            // 日期变为字符串显示
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String format = sdf.format(gameDataReportCount.getCountDate());
            gameDataReportCount.setDateStr(format);

            // 数据为空处理
            if (gameDataReportCount.getOldPayRate() == null) {
                gameDataReportCount.setOldPayRate(BigDecimal.ZERO);
            }

            if (gameDataReportCount.getOldArpu() == null) {
                gameDataReportCount.setOldArpu(BigDecimal.ZERO);
            }

            if (gameDataReportCount.getOldArppu() == null) {
                gameDataReportCount.setOldArppu(BigDecimal.ZERO);
            }

            // 数据格式处理
            BigDecimal payRate = BigDecimalUtils.dividePercent(gameDataReportCount.getPayRate().doubleValue());
            gameDataReportCount.setPayRate(payRate);

            BigDecimal addPayRate = BigDecimalUtils.dividePercent(gameDataReportCount.getAddPayRate().doubleValue());
            gameDataReportCount.setAddPayRate(addPayRate);

            BigDecimal oldPayRate = BigDecimalUtils.dividePercent(gameDataReportCount.getOldPayRate().doubleValue());
            gameDataReportCount.setOldPayRate(oldPayRate);

            BigDecimal oldArpu = BigDecimalUtils.divideTwo(gameDataReportCount.getOldArpu().doubleValue());
            gameDataReportCount.setOldArpu(oldArpu);

            BigDecimal oldArppu = BigDecimalUtils.divideTwo(gameDataReportCount.getOldArppu().doubleValue());
            gameDataReportCount.setOldArppu(oldArppu);

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
            addPayNumSum = addPayNumSum + gameDataReportCount.getAddPayNum();
            // 新增充值金额汇总
            BigDecimal addPayAmount = gameDataReportCount.getAddPayAmount();
            addPayAmountSum = addPayAmountSum + addPayAmount.doubleValue();

        }
        // 活跃付费率汇总
        payRateSum = BigDecimalUtils.divideFour(payNumSum, loginNumSum, true);

        // 充值金额
        payAmountSumBigDecimal = new BigDecimal(payAmountSum);

        // ARPU汇总
        arpuSum = BigDecimalUtils.divideZero(payAmountSum, loginNumSum, false);

        // ARPPU汇总
        arppuSum = BigDecimalUtils.divideZero(payAmountSum, payNumSum, false);

        // 新增付费率汇总
        addPayRateSumBigDecimal = BigDecimalUtils.divideFour(addPayNumSum, addNumSum, true);

        // 新增充值金额
        addPayAmountSumBigDecimal = new BigDecimal(addPayAmountSum);

        // 新增ARPU汇总
        addArpuSum = BigDecimalUtils.divideZero(addPayAmountSum, addNumSum, false);

        // 新增ARPPU汇总
        addArppuSum = BigDecimalUtils.divideZero(addPayAmountSum, addPayNumSum, false);

        // 老玩家汇总
        oldNumSum = loginNumSum - addNumSum;
        // 老玩家付费数汇总
        oldPayNumSum = payNumSum - addPayNumSum;

        // 老玩家付费率汇总
        oldPayRateSumBigDecimal = BigDecimalUtils.divideFour(oldPayNumSum, oldNumSum, true);

        // 老玩家充值金额
        oldPayAmountSumBigDecimal = new BigDecimal(payAmountSum - addPayAmountSum);

        // 老玩家ARPU汇总
        oldArpuSum = BigDecimalUtils.divideZero((payAmountSum - addPayAmountSum), oldNumSum, false);

        // 老玩家ARPPU汇总
        oldArppuSum = BigDecimalUtils.divideZero((payAmountSum - addPayAmountSum), oldPayNumSum, false);

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
        //child.setOldPayAmount(oldPayAmountSumBigDecimal);
        child.setOldPayAmount(BigDecimalUtils.divideTwo(oldPayAmountSumBigDecimal.doubleValue()));
        //child.setOldArpu(oldArpuSum);
        child.setOldArpu(BigDecimalUtils.divideTwo(oldArpuSum.doubleValue()));
        //child.setOldArppu(oldArppuSum);
        child.setOldArppu(BigDecimalUtils.divideTwo(oldArppuSum.doubleValue()));
        child.setDateStr("汇总");


        //封装成list集合的第一个元素
        list.add(0, child);

        return list;
    }
}

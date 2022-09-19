package cn.youai.xiuzhen.stat.service;

import cn.youai.xiuzhen.game.constant.CoreStatisticType;

import java.util.Date;

/**
 * @ClassName IGameDataCountService
 * @Description 数据统计
 * @Author buliangliang
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2020-08-21 11:05
 */
public interface IGameDataCountService {

    /**
     * 定时统计任务
     * 默认 时间：当天
     * 类型：全类型
     */
    void doJobDataCount();

    /**
     * 定时统计任务 --指定日期
     *
     * @param registerDate 统计日期
     * @param type         2-留存 3-ltv
     */
    void doJobDataCount(Date registerDate, CoreStatisticType type);

    /**
     * 获取k-v ltv数据列表
     * Map<String, GameStatLtv> ltvCountMap(boolean isReturnIndex);
     * <p>
     * /**
     * 按类型-统计数据更新
     */
    void doJobDataCountUpdateByType(CoreStatisticType type, Date currentDate);

}

package cn.youai.xiuzhen.stat.service.impl;

import cn.youai.xiuzhen.stat.constant.StatisticType;
import cn.youai.xiuzhen.stat.service.IGameDataCountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @ClassName IGameDataCountServiceImpl
 * @Description 数据统计业务实现
 * @Author buliangliang
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2020-08-21 11:07
 */
@Service
@Slf4j
public class GameDataCountServiceImpl implements IGameDataCountService {

    @Override
    public void doJobDataCount() {
    }

    @Override
    public void doJobDataCount(Date registerDate, StatisticType type) {
    }

    @Override
    public void doJobDataCountUpdateByType(StatisticType type, Date current) {
    }

}

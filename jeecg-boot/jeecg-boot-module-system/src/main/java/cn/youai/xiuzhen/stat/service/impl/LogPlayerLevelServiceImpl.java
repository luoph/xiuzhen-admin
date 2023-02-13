package cn.youai.xiuzhen.stat.service.impl;

import cn.youai.basics.model.DateRange;
import cn.youai.server.model.RangeValue;
import cn.youai.xiuzhen.stat.entity.LogPlayerLevel;
import cn.youai.xiuzhen.stat.mapper.LogPlayerLevelMapper;
import cn.youai.xiuzhen.stat.service.ILogPlayerLevelService;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * @Description: 境界日志
 * @Author: jeecg-boot
 * @Date: 2023-02-10
 * @Version: V1.0
 */
@Service
@DS("shardingSphere")
public class LogPlayerLevelServiceImpl extends ServiceImpl<LogPlayerLevelMapper, LogPlayerLevel> implements ILogPlayerLevelService {

    @Override
    public IPage<LogPlayerLevel> queryList(Page<?> page, LogPlayerLevel entity, RangeValue<BigDecimal> levelRange, RangeValue<BigDecimal> combatPowerRange, DateRange createDateRange) {
        return getBaseMapper().queryList(page, entity, levelRange, combatPowerRange, createDateRange);
    }
}

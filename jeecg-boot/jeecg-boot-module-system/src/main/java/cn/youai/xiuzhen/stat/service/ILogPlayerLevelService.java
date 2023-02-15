package cn.youai.xiuzhen.stat.service;

import cn.youai.basics.model.DateRange;
import cn.youai.server.model.RangeValue;
import cn.youai.xiuzhen.stat.entity.LogPlayerLevel;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.math.BigDecimal;

/**
 * @Description: 境界日志
 * @Author: jeecg-boot
 * @Date: 2023-02-10
 * @Version: V1.0
 */
public interface ILogPlayerLevelService extends IService<LogPlayerLevel> {

    IPage<LogPlayerLevel> queryList(Page<?> page, LogPlayerLevel entity, RangeValue<BigDecimal> levelRange, RangeValue<BigDecimal> combatPowerRange, DateRange createDateRange);
}

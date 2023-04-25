package cn.youai.xiuzhen.stat.mapper;

import cn.youai.basics.model.DateRange;
import cn.youai.server.model.RangeValue;
import cn.youai.xiuzhen.stat.entity.LogPlayerLevel;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;

/**
 * @Description: 境界日志
 * @Author: jeecg-boot
 * @Date: 2023-02-10
 * @Version: V1.0
 */
public interface LogPlayerLevelMapper extends BaseMapper<LogPlayerLevel> {

    IPage<LogPlayerLevel> queryList(Page<?> page,
                                    @Param("entity") LogPlayerLevel entity,
                                    @Param("levelRange") RangeValue<BigDecimal> levelRange,
                                    @Param("combatPowerRange") RangeValue<BigDecimal> combatPowerRange,
                                    @Param("dateRange") DateRange dateRange,
                                    @Param("configAuth") String configAuth);
}

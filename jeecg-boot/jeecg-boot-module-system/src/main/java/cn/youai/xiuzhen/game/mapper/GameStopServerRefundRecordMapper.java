package cn.youai.xiuzhen.game.mapper;

import cn.youai.basics.model.DateRange;
import cn.youai.server.model.RangeValue;
import cn.youai.xiuzhen.game.entity.GameStopServerRefundRecord;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;

/**
 * @Description: 删档返还
 * @Author: jeecg-boot
 * @Date: 2022-12-14
 * @Version: V1.0
 */
public interface GameStopServerRefundRecordMapper extends BaseMapper<GameStopServerRefundRecord> {

    IPage<GameStopServerRefundRecord> pageList(Page<?> page, @Param("entity") GameStopServerRefundRecord entity,
                                               @Param("sourceAmountRange") RangeValue<BigDecimal> sourceAmountRange,
                                               @Param("targetNumRange") RangeValue<BigDecimal> targetNumRange,
                                               @Param("createTimeRange") DateRange createTimeRange);
}

package cn.youai.xiuzhen.stat.mapper;

import cn.youai.basics.model.DateRange;
import cn.youai.xiuzhen.stat.entity.CombatPowerLog;
import cn.youai.xiuzhen.stat.entity.LogPlayer;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description log_player
 * @date 2021-01-14
 */
public interface LogPlayerMapper extends BaseMapper<LogPlayer> {

    IPage<CombatPowerLog> selectCombatPowerLogList(Page<?> page,
                                                   @Param("searchObj") CombatPowerLog searchObj,
                                                   @Param("range") DateRange range);
}

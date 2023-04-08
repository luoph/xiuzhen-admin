package cn.youai.xiuzhen.game.mapper;

import cn.youai.basics.model.DateRange;
import cn.youai.server.model.RangeValue;
import cn.youai.xiuzhen.game.entity.GameInfo;
import cn.youai.xiuzhen.game.entity.GameVps;
import cn.youai.xiuzhen.game.entity.GameVps;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 虚拟主机
 * @date 2023-04-08
 */
public interface GameVpsMapper extends BaseMapper<GameVps> {
    IPage<GameVps> queryList(Page<GameVps> page,
                             @Param("entity") GameVps entity,
                             @Param("createTimeRange") DateRange createTimeRange);

}

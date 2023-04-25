package cn.youai.xiuzhen.stat.mapper;

import cn.youai.basics.model.DateRange;
import cn.youai.xiuzhen.stat.entity.ItemLog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description player_item_log
 * @date 2020-07-21
 */
public interface ItemLogMapper extends BaseMapper<ItemLog> {

    IPage<ItemLog> queryList(Page<ItemLog> page, @Param("entity") ItemLog entity, @Param("dateRange") DateRange createTimeRange, @Param("configAuth") String configAuth);
}

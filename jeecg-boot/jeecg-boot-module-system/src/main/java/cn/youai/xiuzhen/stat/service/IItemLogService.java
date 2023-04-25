package cn.youai.xiuzhen.stat.service;

import cn.youai.basics.model.DateRange;
import cn.youai.xiuzhen.stat.entity.ItemLog;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author jeecg-boot
 * @version V1.0
 * @date 2020-07-21
 */
public interface IItemLogService extends IService<ItemLog> {

    IPage<ItemLog> queryList(Page<ItemLog> page, ItemLog entity, DateRange createTimeRange);

}

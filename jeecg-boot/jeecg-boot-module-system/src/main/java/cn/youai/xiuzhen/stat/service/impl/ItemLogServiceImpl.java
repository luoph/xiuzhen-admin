package cn.youai.xiuzhen.stat.service.impl;

import cn.youai.basics.model.DateRange;
import cn.youai.xiuzhen.stat.entity.ItemLog;
import cn.youai.xiuzhen.stat.mapper.ItemLogMapper;
import cn.youai.xiuzhen.stat.service.IItemLogService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.common.system.query.QueryGenerator;
import org.springframework.stereotype.Service;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description player_item_log
 * @date 2020-07-21
 */
@Service
public class ItemLogServiceImpl extends ServiceImpl<ItemLogMapper, ItemLog> implements IItemLogService {

    @Override
    public IPage<ItemLog> queryList(Page<ItemLog> page, ItemLog entity, DateRange createTimeRange) {
        String configAuth = QueryGenerator.getAllConfigAuth();
        return getBaseMapper().queryList(page, entity, createTimeRange, configAuth);
    }

}

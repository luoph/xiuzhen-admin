package cn.youai.xiuzhen.stat.service.impl;

import cn.youai.xiuzhen.stat.entity.ItemLog;
import cn.youai.xiuzhen.stat.mapper.ItemLogMapper;
import cn.youai.xiuzhen.stat.service.IItemLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description player_item_log
 * @date 2020-07-21
 */
@Service
public class ItemLogServiceImpl extends ServiceImpl<ItemLogMapper, ItemLog> implements IItemLogService {
}

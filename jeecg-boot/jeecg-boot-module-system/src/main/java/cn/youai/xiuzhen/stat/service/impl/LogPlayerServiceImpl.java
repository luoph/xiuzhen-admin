package cn.youai.xiuzhen.stat.service.impl;

import cn.youai.basics.model.DateRange;
import cn.youai.xiuzhen.stat.entity.CombatPowerLog;
import cn.youai.xiuzhen.stat.entity.LogPlayer;
import cn.youai.xiuzhen.stat.mapper.LogPlayerMapper;
import cn.youai.xiuzhen.stat.service.ILogPlayerService;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description log_player
 * @date 2021-01-14
 */
@Service
@DS("shardingSphere")
public class LogPlayerServiceImpl extends ServiceImpl<LogPlayerMapper, LogPlayer> implements ILogPlayerService {

    @Override
    public IPage<CombatPowerLog> selectCombatPowerLogList(Page<?> page, CombatPowerLog searchObj, DateRange range) {
        return getBaseMapper().selectCombatPowerLogList(page, searchObj, range);
    }

}

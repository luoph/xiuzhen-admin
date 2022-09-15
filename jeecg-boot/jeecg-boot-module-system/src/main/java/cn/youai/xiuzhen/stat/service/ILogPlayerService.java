package cn.youai.xiuzhen.stat.service;

import cn.youai.basics.model.DateRange;
import cn.youai.xiuzhen.stat.entity.CombatPowerLog;
import cn.youai.xiuzhen.stat.entity.LogPlayer;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description log_player
 * @date 2021-01-14
 */
public interface ILogPlayerService extends IService<LogPlayer> {

    IPage<CombatPowerLog> selectCombatPowerLogList(Page<?> page, CombatPowerLog searchObj, DateRange range);

}

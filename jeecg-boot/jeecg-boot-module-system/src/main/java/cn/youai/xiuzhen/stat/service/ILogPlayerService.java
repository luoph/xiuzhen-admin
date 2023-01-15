package cn.youai.xiuzhen.stat.service;

import cn.youai.xiuzhen.stat.entity.CombatPowerLog;
import cn.youai.xiuzhen.stat.entity.LogPlayer;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Date;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description log_player
 * @date 2021-01-14
 */
public interface ILogPlayerService extends IService<LogPlayer> {

    IPage<CombatPowerLog> selectCombatPowerLogList(Page<?> page, String channel, Integer serverId, Long playerId, Date start, Date end);

}
